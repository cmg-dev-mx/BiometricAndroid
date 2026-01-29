package mx.dev.cmg.android.biometrics.ui.feature.splash.vm

sealed interface SplashSideEffect {
    data object NavigateToLogin : SplashSideEffect
    data object NavigateToHome : SplashSideEffect
}