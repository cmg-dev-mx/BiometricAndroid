package mx.dev.cmg.android.biometrics.repository.biometric

import androidx.biometric.BiometricManager
import androidx.biometric.BiometricManager.Authenticators.BIOMETRIC_STRONG
import mx.dev.cmg.android.biometrics.source.shared.LocalSource
import javax.inject.Inject

class BiometricRepositoryImpl @Inject constructor(
    private val biometricManager: BiometricManager,
    private val localSource: LocalSource
) : BiometricRepository {

    override suspend fun isUserEnrolled(): Result<Boolean> {
        return try {
            val isBiometricEnabledInApp = localSource.isBiometricEnrolled().getOrDefault(false)
            
            if (!isBiometricEnabledInApp) {
                return Result.success(false)
            }

            val canAuthenticate = biometricManager.canAuthenticate(BIOMETRIC_STRONG)
            
            when (canAuthenticate) {
                BiometricManager.BIOMETRIC_SUCCESS -> {
                    Result.success(true)
                }
                BiometricManager.BIOMETRIC_ERROR_NO_HARDWARE,
                BiometricManager.BIOMETRIC_ERROR_HW_UNAVAILABLE -> {
                    Result.failure(Exception("Hardware biométrico no disponible"))
                }
                else -> {
                    Result.success(false)
                }
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun setBiometricEnabled(enabled: Boolean): Result<Unit> {
        return try {
            if (enabled) {
                when (biometricManager.canAuthenticate(BIOMETRIC_STRONG)) {
                    BiometricManager.BIOMETRIC_SUCCESS -> {
                        localSource.enrollBiometric()
                        Result.success(Unit)
                    }
                    else -> {
                        Result.failure(Exception("No se puede habilitar la autenticación biométrica"))
                    }
                }
            } else {
                localSource.disableBiometric()
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun needsToEnrollBiometric(): Result<Boolean> {
        return try {
            val canAuthenticate = biometricManager.canAuthenticate(BIOMETRIC_STRONG)
            val needsEnrollment = canAuthenticate == BiometricManager.BIOMETRIC_ERROR_NONE_ENROLLED
            Result.success(needsEnrollment)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getBiometricEnabledStatus(): Result<Boolean> {
        return localSource.isBiometricEnrolled()
    }
}