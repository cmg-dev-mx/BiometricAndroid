package mx.dev.cmg.android.biometrics.source.shared

import android.app.Application
import androidx.core.content.edit
import javax.inject.Inject

class LocalSourceImpl @Inject constructor(application: Application) : LocalSource {

    private val sharedPreferences by lazy {
        application.getSharedPreferences("biometric_prefs", Application.MODE_PRIVATE)
    }

    override suspend fun clearAllData(): Result<Boolean> {
        return try {
            val editor = sharedPreferences.edit()
            editor.clear()
            val success = editor.commit()
            Result.success(success)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun isUserLogged(): Result<Boolean> {
        return try {
            val isLogged = sharedPreferences.getBoolean("is_user_logged", false)
            Result.success(isLogged)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun login(
        user: String,
        password: String
    ): Result<Boolean> {
        return try {
            // Simulate login logic
            val isValidUser = user == "testUser" && password == "testPassword"
            if (isValidUser) {
                sharedPreferences.edit {
                    putBoolean("is_user_logged", true)
                }
            }
            Result.success(isValidUser)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun isBiometricEnrolled(): Result<Boolean> {
        return try {
            val isEnrolled = sharedPreferences.getBoolean("is_biometric_enrolled", false)
            Result.success(isEnrolled)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun enrollBiometric(): Result<Boolean> {
        return try {
            sharedPreferences.edit {
                putBoolean("is_biometric_enrolled", true)
            }
            Result.success(true)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}