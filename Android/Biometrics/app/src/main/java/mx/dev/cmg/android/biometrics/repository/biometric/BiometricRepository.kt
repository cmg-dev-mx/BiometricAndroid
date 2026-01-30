package mx.dev.cmg.android.biometrics.repository.biometric

interface BiometricRepository {
    suspend fun isUserEnrolled(): Result<Boolean>
    suspend fun setBiometricEnabled(enabled: Boolean): Result<Unit>
    suspend fun needsToEnrollBiometric(): Result<Boolean>
    suspend fun getBiometricEnabledStatus(): Result<Boolean>
}