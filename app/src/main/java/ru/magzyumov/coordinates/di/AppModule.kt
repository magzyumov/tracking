package ru.magzyumov.coordinates.di

import android.app.Application
import android.content.Context
import dagger.Module
import dagger.Provides
import ru.magzyumov.coordinates.util.ResourceManager
import javax.inject.Singleton

@Module
class AppModule(_application: Application) {
    private val application: Application = _application

    @Singleton
    @Provides
    fun provideContext(): Context { return application }

    @Singleton
    @Provides
    fun provideResourceManager(): ResourceManager { return ResourceManager() }
}