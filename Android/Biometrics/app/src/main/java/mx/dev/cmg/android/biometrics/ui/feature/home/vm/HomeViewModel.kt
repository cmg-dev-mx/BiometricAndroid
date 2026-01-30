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

    init {
        loadBiometricStatus()
    }

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

    private fun loadBiometricStatus() {
        viewModelScope.launch {
            biometricRepository.getBiometricEnabledStatus().onSuccess { isEnabled ->
                _uiState.value = _uiState.value.copy(isBiometricEnabled = isEnabled)
            }
        }
    }

    private fun toggleBiometrics(enable: Boolean) {
        viewModelScope.launch {
            if (enable) {
                biometricRepository.needsToEnrollBiometric().onSuccess { needsEnrollment ->
                    if (needsEnrollment) {
                        _sideEffect.send(HomeSideEffect.ShowBiometricEnrollment)
                    } else {
                        biometricRepository.setBiometricEnabled(true).onSuccess {
                            _uiState.value = _uiState.value.copy(isBiometricEnabled = true)
                            _sideEffect.send(HomeSideEffect.ToggleBiometricSuccess)
                        }.onFailure { error ->
                            _sideEffect.send(HomeSideEffect.ShowError(error.message ?: "Error al habilitar biometría"))
                        }
                    }
                }.onFailure { error ->
                    _sideEffect.send(HomeSideEffect.ShowError(error.message ?: "Error al verificar biometría"))
                }
            } else {
                biometricRepository.setBiometricEnabled(false).onSuccess {
                    _uiState.value = _uiState.value.copy(isBiometricEnabled = false)
                    _sideEffect.send(HomeSideEffect.ToggleBiometricSuccess)
                }.onFailure { error ->
                    _sideEffect.send(HomeSideEffect.ShowError(error.message ?: "Error al deshabilitar biometría"))
                }
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