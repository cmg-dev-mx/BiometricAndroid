package mx.dev.cmg.android.biometrics.repository.session

interface SessionRepository {
    suspend fun isUserAlreadyLogged(): Result<Boolean>
    suspend fun login(user: String, pass: String): Result<Unit>
}