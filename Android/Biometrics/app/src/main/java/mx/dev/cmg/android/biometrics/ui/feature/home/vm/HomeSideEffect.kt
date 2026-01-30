package mx.dev.cmg.android.biometrics.ui.feature.home.vm

sealed interface HomeSideEffect {
    data object ToggleBiometricSuccess : HomeSideEffect
    data object LogoutSuccess : HomeSideEffect
    data object ShowBiometricEnrollment : HomeSideEffect
    data class ShowError(val message: String) : HomeSideEffect
}
