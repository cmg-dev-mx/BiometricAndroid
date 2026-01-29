package mx.dev.cmg.android.biometrics.repository.session

interface SessionRepository {
    suspend fun isUserAlreadyLogged(): Result<Boolean>
}
