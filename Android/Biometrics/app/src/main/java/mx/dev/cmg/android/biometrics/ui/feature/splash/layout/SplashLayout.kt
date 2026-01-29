package mx.dev.cmg.android.biometrics.ui.feature.splash.layout

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import mx.dev.cmg.android.biometrics.ui.feature.splash.vm.SplashUiState

@Composable
fun SplashLayout(
    uiState: SplashUiState,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .background(color = MaterialTheme.colorScheme.background)
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            style = MaterialTheme.typography.displayMedium,
            text = "Splash screen"
        )

        if (uiState.isLoading) {
            CircularProgressIndicator(
                modifier = Modifier.size(48.dp)
            )
        }
    }
}

@Preview
@Composable
private fun SplashPreview() {
    SplashLayout(
        uiState = SplashUiState(isLoading = true),
        modifier = Modifier.fillMaxSize()
    )
}