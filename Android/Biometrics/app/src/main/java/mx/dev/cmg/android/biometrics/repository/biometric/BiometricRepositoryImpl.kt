package mx.dev.cmg.android.biometrics.repository.biometric

import javax.inject.Inject

class BiometricRepositoryImpl @Inject constructor() : BiometricRepository {

    override suspend fun isUserEnrolled(): Result<Boolean> {
        // TODO Not yet implemented
        return Result.success(false)
    }
}