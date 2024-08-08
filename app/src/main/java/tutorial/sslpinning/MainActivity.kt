package tutorial.sslpinning

import android.os.Bundle
import android.util.Log
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
import tutorial.sslpinning.ui.theme.SSLPinningTheme
import java.net.URL
import java.util.Scanner


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
                                .height(10.dp)
                        )

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

                        FilledTonalButton(
                            onClick = { testUrlConnection("https://www.google.co.in/") },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color.Red,
                                contentColor = Color.White
                            )
                        ) {
                            Text("SSL Certificate Connection Fail")
                        }
                    }
                }
            }
        }
    }

    private fun testUrlConnection(webUrl: String) {
        lifecycleScope.launch(Dispatchers.IO) {
            val url = URL(webUrl)
            val openStream = url.openStream()
            val scanner = Scanner(openStream, "UTF-8")
            val out = scanner.useDelimiter("\\A").next();
            Log.d(TAG, "Output = $out")
        }
    }
}