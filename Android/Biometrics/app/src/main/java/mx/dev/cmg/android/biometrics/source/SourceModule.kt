package mx.dev.cmg.android.biometrics.source

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import mx.dev.cmg.android.biometrics.source.shared.LocalSource
import mx.dev.cmg.android.biometrics.source.shared.LocalSourceImpl
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class SourceModule {

    @Binds
    @Singleton
    abstract fun bindLocalSource(impl: LocalSourceImpl): LocalSource
}