package mx.dev.cmg.android.biometrics

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.ui.Modifier
import dagger.hilt.android.AndroidEntryPoint
import mx.dev.cmg.android.biometrics.helper.biometric.BiometricPromptManager
import mx.dev.cmg.android.biometrics.ui.navigation.MainNavHost
import mx.dev.cmg.android.biometrics.ui.theme.BiometricsTheme

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            BiometricsTheme {
                MainNavHost(
                    modifier = Modifier.systemBarsPadding()
                        .fillMaxSize()
                )
            }
        }
    }
}