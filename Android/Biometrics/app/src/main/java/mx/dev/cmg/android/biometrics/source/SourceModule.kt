package mx.dev.cmg.android.biometrics.source

import android.app.Application
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import mx.dev.cmg.android.biometrics.source.shared.LocalSource
import mx.dev.cmg.android.biometrics.source.shared.LocalSourceImpl

@Module
@InstallIn(SingletonComponent::class)
class SourceModule {

    @Provides
    fun provideLocalSource(@ApplicationContext application: Application): LocalSource {
        return LocalSourceImpl(application)
    }
}