package mx.dev.cmg.android.biometrics.ui.feature.login.vm

sealed interface LoginEvent {
    object OnLoginButtonClicked : LoginEvent
    data class OnUserInputChanged(val userInput: String) : LoginEvent
    data class OnPasswordInputChanged(val passwordInput: String) : LoginEvent
}