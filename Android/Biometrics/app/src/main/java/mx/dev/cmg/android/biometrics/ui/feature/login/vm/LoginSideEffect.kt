package mx.dev.cmg.android.biometrics.ui.feature.login.vm

sealed interface LoginSideEffect {
    data object NavigateToHome : LoginSideEffect
    data class ErrorLoggingIn(val error: String) : LoginSideEffect
}