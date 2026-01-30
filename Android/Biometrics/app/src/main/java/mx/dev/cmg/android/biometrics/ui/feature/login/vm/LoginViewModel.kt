package mx.dev.cmg.android.biometrics.ui.feature.login.vm

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import mx.dev.cmg.android.biometrics.repository.session.SessionRepository
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val repository: SessionRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(LoginUiState())
    val uiState = _uiState.asStateFlow()

    private val _sideEffect = Channel<LoginSideEffect>(Channel.BUFFERED)
    val sideEffect = _sideEffect.receiveAsFlow()

    fun onEvent(event: LoginEvent) {
        when (event) {
            is LoginEvent.OnLoginButtonClicked -> {
                handleLogin()
            }
            is LoginEvent.OnUserInputChanged -> {
                _uiState.value = _uiState.value.copy(user = event.userInput)
            }
            is LoginEvent.OnPasswordInputChanged -> {
                _uiState.value = _uiState.value.copy(pass = event.passwordInput)
            }
        }
    }

    private fun handleLogin() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, errorMessage = null)

            val user = _uiState.value.user
            val pass = _uiState.value.pass

            repository.login(user, pass).onSuccess {
                _sideEffect.send(LoginSideEffect.NavigateToHome)
            }.onFailure { error ->
                _uiState.value = _uiState.value.copy(errorMessage = error.message)
                _sideEffect.send(LoginSideEffect.ErrorLoggingIn(error.message ?: "Unknown error"))
            }

            _uiState.value = _uiState.value.copy(isLoading = false)
        }
    }
}