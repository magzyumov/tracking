package ru.magzyumov.coordinates.di

import dagger.Component
import ru.magzyumov.coordinates.ui.main.MainPresenter
import ru.magzyumov.coordinates.util.ResourceManager
import javax.inject.Singleton

@Singleton
@Component(modules = [AppModule::class])
interface AppComponent {
    fun inject(resources: ResourceManager)
    fun inject(presenter: MainPresenter)
}