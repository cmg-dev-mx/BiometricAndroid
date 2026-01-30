package mx.dev.cmg.android.biometrics.source

import android.content.Context
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
    fun provideLocalSource(@ApplicationContext context: Context): LocalSource {
        return LocalSourceImpl(context)
    }
}