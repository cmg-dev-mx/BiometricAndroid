package mx.dev.cmg.android.biometrics.ui.feature.login.vm

data class LoginUiState(
    val isLoading: Boolean = false,
    val user: String = "",
    val pass: String = "",
    val errorMessage: String? = null
)