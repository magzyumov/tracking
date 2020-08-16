package ru.magzyumov.coordinates

import android.app.Application
import ru.magzyumov.coordinates.di.AppComponent
import ru.magzyumov.coordinates.di.AppModule
import ru.magzyumov.coordinates.di.DaggerAppComponent

class App: Application() {

    companion object {
        private lateinit var appComponent: AppComponent

        fun getComponent(): AppComponent {return appComponent}

    }

    override fun onCreate() {
        super.onCreate()

        appComponent = generateAppComponent()

    }

    private fun generateAppComponent(): AppComponent{
        return DaggerAppComponent
            .builder()
            .appModule(AppModule(this))
            .build()
    }

}