package mx.dev.cmg.android.biometrics.repository.biometric

import javax.inject.Inject

class BiometricRepositoryImpl @Inject constructor() : BiometricRepository {

    override suspend fun isUserEnrolled(): Result<Boolean> {
        // TODO Not yet implemented
        return Result.success(false)
    }

    override suspend fun setBiometricEnabled(enabled: Boolean): Result<Unit> {
        TODO("Not yet implemented")
    }
}