package mx.dev.cmg.android.biometrics.ui.feature.login.layout

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import mx.dev.cmg.android.biometrics.ui.feature.login.vm.LoginEvent
import mx.dev.cmg.android.biometrics.ui.feature.login.vm.LoginUiState

@Composable
fun LoginLayout(
    uiState: LoginUiState,
    onEvent: (LoginEvent) -> Unit,
    modifier: Modifier = Modifier
) {
    val passwordFocusRequester = remember { FocusRequester() }

    Column(
        modifier = modifier.padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            style = MaterialTheme.typography.displayMedium,
            text = "Login"
        )
        TextField(
            modifier = Modifier.fillMaxWidth(),
            value = uiState.user,
            onValueChange = { onEvent(LoginEvent.OnUserInputChanged(it)) },
            label = { Text("User") },
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
            keyboardActions = KeyboardActions(
                onNext = { passwordFocusRequester.requestFocus() }
            )
        )
        TextField(
            modifier = Modifier
                .fillMaxWidth()
                .focusRequester(passwordFocusRequester),
            value = uiState.pass,
            onValueChange = { onEvent(LoginEvent.OnPasswordInputChanged(it)) },
            label = { Text("Password") },
            keyboardOptions = KeyboardOptions.Default.copy(
                autoCorrectEnabled = false,
                keyboardType = KeyboardType.Password,
                imeAction = ImeAction.Done
            ),
            keyboardActions = KeyboardActions(
                onDone = { onEvent(LoginEvent.OnLoginButtonClicked) }
            )
        )

        Button(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.CenterHorizontally),
            onClick = { onEvent(LoginEvent.OnLoginButtonClicked) },
            enabled = !uiState.isLoading
        ) {
            Text(text = if (uiState.isLoading) "Logging in..." else "Login")
        }
    }
}

@Preview
@Composable
private fun LoginPreview() {
    LoginLayout(
        modifier = Modifier.fillMaxSize(),
        uiState = LoginUiState(),
        onEvent = {}
    )
}