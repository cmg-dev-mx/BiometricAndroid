package mx.dev.cmg.android.biometrics.ui.feature.splash.vm

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import mx.dev.cmg.android.biometrics.repository.biometric.BiometricRepository
import mx.dev.cmg.android.biometrics.repository.session.SessionRepository
import javax.inject.Inject
import kotlin.time.Duration.Companion.seconds

@HiltViewModel
class SplashViewModel @Inject constructor(
    private val sessionRepository: SessionRepository,
    private val biometricRepository: BiometricRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(SplashUiState())
    val uiState = _uiState.asStateFlow()

    private val _sideEffect = Channel<SplashSideEffect>(Channel.BUFFERED)
    val sideEffect = _sideEffect.receiveAsFlow()

    init {
        viewModelScope.launch {
            delay(2.seconds)
            validateUserAlreadyLogged()
        }
    }

    private fun validateUserAlreadyLogged() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)

            sessionRepository.isUserAlreadyLogged().onSuccess { isLogged ->
                if (isLogged) {
                    biometricRepository.isUserEnrolled().onSuccess { isEnrolled ->
                        if (isEnrolled) {
                            _sideEffect.send(SplashSideEffect.ShowBiometricPrompt)
                        } else {
                            _sideEffect.send(SplashSideEffect.NavigateToHome)
                        }
                    }
                } else {
                    _sideEffect.send(SplashSideEffect.NavigateToLogin)
                }
            }

            _uiState.value = _uiState.value.copy(isLoading = false)
        }
    }
}