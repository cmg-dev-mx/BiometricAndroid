package mx.dev.cmg.android.biometrics.repository.session

import javax.inject.Inject

class SessionRepositoryImpl @Inject constructor() : SessionRepository {

    override suspend fun isUserAlreadyLogged(): Result<Boolean> {
        // TODO Not yet implemented
        return Result.success(false)
    }
}