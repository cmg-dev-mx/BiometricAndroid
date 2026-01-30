package mx.dev.cmg.android.biometrics.source.shared

import android.app.Application
import android.content.Context
import androidx.core.content.edit
import javax.inject.Inject

class LocalSourceImpl @Inject constructor(context: Context) : LocalSource {

    private val sharedPreferences by lazy {
        context.getSharedPreferences("biometric_prefs", Application.MODE_PRIVATE)
    }

    override suspend fun clearAllData(): Result<Unit> {
        return try {
            sharedPreferences.edit(commit = true) {
                clear()
            }
            Result.success(Unit)
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
    ): Result<Unit> {
        return try {
            if (user == "admin" && password == "admin") {
                sharedPreferences.edit {
                    putBoolean("is_user_logged", true)
                }
                Result.success(Unit)
            } else {
                Result.failure(Exception("Invalid credentials"))
            }
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