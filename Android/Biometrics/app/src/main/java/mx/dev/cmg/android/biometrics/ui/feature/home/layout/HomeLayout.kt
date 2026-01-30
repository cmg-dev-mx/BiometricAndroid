package mx.dev.cmg.android.biometrics.ui.feature.home.layout

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import mx.dev.cmg.android.biometrics.ui.feature.home.vm.HomeEvent
import mx.dev.cmg.android.biometrics.ui.feature.home.vm.HomeUiState

@Composable
fun HomeLayout(
    uiState: HomeUiState,
    onEvent: (HomeEvent) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .background(color = MaterialTheme.colorScheme.background)
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            style = MaterialTheme.typography.displayMedium,
            text = "Home screen"
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    color = MaterialTheme.colorScheme.surfaceVariant,
                    shape = RoundedCornerShape(16.dp)
                )
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically

        ) {
            Text(
                style = MaterialTheme.typography.bodyLarge,
                text = "Biometric Authentication"
            )

            Switch(
                checked = uiState.isBiometricEnabled,
                onCheckedChange = { isChecked ->
                    if (isChecked) {
                        onEvent(HomeEvent.EnableBiometrics)
                    } else {
                        onEvent(HomeEvent.DisableBiometrics)
                    }
                }
            )
        }

        Button(
            modifier = Modifier.fillMaxWidth(),
            onClick = { onEvent(HomeEvent.Logout) }
        ) {
            Text(text = "Logout")
        }
    }
}

@Preview
@Composable
private fun HomePreview() {
    HomeLayout(
        modifier = Modifier.fillMaxSize(),
        uiState = HomeUiState(isBiometricEnabled = true),
        onEvent = {}
    )
}