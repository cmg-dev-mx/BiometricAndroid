package mx.dev.cmg.android.biometrics.repository

import android.content.Context
import androidx.biometric.BiometricManager
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import mx.dev.cmg.android.biometrics.repository.biometric.BiometricRepository
import mx.dev.cmg.android.biometrics.repository.biometric.BiometricRepositoryImpl
import mx.dev.cmg.android.biometrics.repository.session.SessionRepository
import mx.dev.cmg.android.biometrics.repository.session.SessionRepositoryImpl
import mx.dev.cmg.android.biometrics.source.shared.LocalSource
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindSessionRepository(impl: SessionRepositoryImpl): SessionRepository

    @Binds
    @Singleton
    abstract fun bindBiometricRepository(impl: BiometricRepositoryImpl): BiometricRepository

    companion object {
        @Provides
        @Singleton
        fun provideBiometricManager(@ApplicationContext context: Context): BiometricManager {
            return BiometricManager.from(context)
        }
    }
}