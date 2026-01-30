package mx.dev.cmg.android.biometrics.ui.feature.home.vm

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import mx.dev.cmg.android.biometrics.repository.biometric.BiometricRepository
import mx.dev.cmg.android.biometrics.repository.session.SessionRepository
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val sessionRepository: SessionRepository,
    private val biometricRepository: BiometricRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState = _uiState.asStateFlow()

    private val _sideEffect = Channel<HomeSideEffect>(Channel.BUFFERED)
    val sideEffect = _sideEffect.receiveAsFlow()

    fun onEvent(event: HomeEvent) {
        when (event) {
            is HomeEvent.EnableBiometrics -> {
                toggleBiometrics(true)
            }
            is HomeEvent.DisableBiometrics -> {
                toggleBiometrics(false)
            }
            is HomeEvent.Logout -> {
                logout()
            }
        }
    }

    private fun toggleBiometrics(enable: Boolean) {
        viewModelScope.launch {
            biometricRepository.setBiometricEnabled(enable).onSuccess {
                _uiState.value = _uiState.value.copy(isBiometricEnabled = enable)
                _sideEffect.send(HomeSideEffect.ToggleBiometricSuccess)
            }
        }
    }

    private fun logout() {
        viewModelScope.launch {
            sessionRepository.logout().onSuccess {
                _sideEffect.send(HomeSideEffect.LogoutSuccess)
            }
        }
    }
}