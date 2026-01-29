package mx.dev.cmg.android.biometrics.source.shared

interface LocalSource {
    suspend fun clearAllData(): Result<Boolean>
    suspend fun isUserLogged(): Result<Boolean>
    suspend fun login(user: String, password: String): Result<Boolean>
    suspend fun isBiometricEnrolled(): Result<Boolean>
    suspend fun enrollBiometric(): Result<Boolean>
}