package tutorial.sslpinning

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import tutorial.sslpinning.ui.theme.SSLPinningTheme
import java.net.URL
import java.util.Scanner
import javax.net.ssl.SSLHandshakeException

private const val TAG = "MainActivity"

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            SSLPinningTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Column(
                        modifier = Modifier
                            .padding(innerPadding)
                            .fillMaxSize(),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Spacer(
                            modifier = Modifier
                                .height(20.dp)
                        )

                        /**
                         * Showcase how website if pinned to proper certificate leads to successful
                         * Handshake
                         */
                        FilledTonalButton(
                            onClick = { testUrlConnection("https://www.wikipedia.org/") },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color.Green,
                                contentColor = Color.DarkGray
                            )
                        ) {
                            Text("SSL Certificate Connection Pass")
                        }

                        Spacer(
                            modifier = Modifier
                                .height(20.dp)
                        )

                        /**
                         * Showcase how website if pinned to wrong certificate leads to Handshake
                         * Fail
                         */
                        FilledTonalButton(
                            onClick = { testUrlConnection("https://www.google.co.in/") },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color.Red,
                                contentColor = Color.White
                            )
                        ) {
                            Text("SSL Certificate Connection Fail")
                        }

                        Spacer(
                            modifier = Modifier
                                .height(20.dp)
                        )

                        /**
                         * Showcase how website if pinned to correct SHA256 hash of public key
                         * leads to successful Handshake
                         */
                        FilledTonalButton(
                            onClick = { testUrlConnection("https://www.facebook.com/") },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color.Green,
                                contentColor = Color.DarkGray
                            )
                        ) {
                            Text("SSL Public Key Pinning Pass")
                        }

                        Spacer(
                            modifier = Modifier
                                .height(20.dp)
                        )

                        /**
                         * Showcase how website if pinned to correct SHA256 hash of public key
                         * leads to unsuccessful Handshake
                         */
                        FilledTonalButton(
                            onClick = { testUrlConnection("https://github.com/") },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color.Red,
                                contentColor = Color.White
                            )
                        ) {
                            Text("SSL Public Key Pinning Fail")
                        }
                    }
                }
            }
        }
    }

    private fun testUrlConnection(webUrl: String) {
        lifecycleScope.launch(Dispatchers.IO) {
            try {
                val url = URL(webUrl)
                val openStream = url.openStream()
                val scanner = Scanner(openStream, "UTF-8")
                val out = scanner.useDelimiter("\\A").next();
                Log.d(TAG, "Output = $out")
                showToast("Successful handshake")
            } catch (exception : SSLHandshakeException) {
                Log.d(TAG, "Exception cause = ${exception.message}")
                showToast(exception.message ?: "Handshake Failed")
            }
        }
    }

    private suspend fun showToast(message: String) = withContext(Dispatchers.Main) {
        Toast.makeText(application, message, Toast.LENGTH_LONG).show()
    }
}