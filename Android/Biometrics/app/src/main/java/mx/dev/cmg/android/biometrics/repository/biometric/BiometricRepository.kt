package mx.dev.cmg.android.biometrics.repository.biometric

interface BiometricRepository {
    suspend fun isUserEnrolled(): Result<Boolean>
}