package br.com.victorhgo.movieapp

import android.app.Application
import br.com.victorhgo.movieapp.di.AppModule
import br.com.victorhgo.movieapp.di.NetworkModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

class MainApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidLogger()
            androidContext(this@MainApplication)
            modules(listOf(AppModule, NetworkModule))
        }

    }
}