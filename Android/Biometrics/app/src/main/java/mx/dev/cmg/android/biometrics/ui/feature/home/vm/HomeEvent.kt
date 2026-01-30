package mx.dev.cmg.android.biometrics.ui.feature.home.vm

sealed interface HomeEvent {
    object EnableBiometrics : HomeEvent
    object DisableBiometrics : HomeEvent
    object Logout : HomeEvent
}