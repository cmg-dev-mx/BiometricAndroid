package mx.dev.cmg.android.biometrics.repository.session

import mx.dev.cmg.android.biometrics.source.shared.LocalSource
import javax.inject.Inject

class SessionRepositoryImpl @Inject constructor(
    private val source: LocalSource
) : SessionRepository {

    override suspend fun isUserAlreadyLogged(): Result<Boolean> =
        source.isUserLogged()

    override suspend fun login(
        user: String,
        pass: String
    ): Result<Unit> {
        return source.login(user, pass)
    }

    override suspend fun logout(): Result<Unit> {
        return source.clearAllData()
    }
}