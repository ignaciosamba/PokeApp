package com.example.pokedexapp.app

import android.app.Application
import com.example.pokedexapp.app.koin.*
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.logger.Level
import timber.log.Timber

class PokeDexApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        Timber.plant(Timber.DebugTree())
        startKoin {
            androidLogger(Level.DEBUG)
            androidContext(this@PokeDexApplication)
            modules(listOf(viewModelModule, repositoryModule, apiModule, netModule, databaseModule))
        }
    }
}