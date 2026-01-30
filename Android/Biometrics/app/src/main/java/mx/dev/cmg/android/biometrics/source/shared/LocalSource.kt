package mx.dev.cmg.android.biometrics.source.shared

interface LocalSource {
    suspend fun clearAllData(): Result<Unit>
    suspend fun isUserLogged(): Result<Boolean>
    suspend fun login(user: String, password: String): Result<Unit>
    suspend fun isBiometricEnrolled(): Result<Boolean>
    suspend fun enrollBiometric(): Result<Boolean>
}