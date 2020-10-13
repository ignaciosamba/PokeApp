package com.example.pokedexapp.app.koin

import android.app.Application
import androidx.room.Room
import com.example.pokedexapp.data.database.PokeDataBase
import com.example.pokedexapp.data.network.PokeService
import com.example.pokedexapp.data.repository.PokeDetailsRepository
import com.example.pokedexapp.data.repository.PokeRepository
import com.example.pokedexapp.ui.PokeListViewModel
import com.example.pokedexapp.ui.PokemonDetailsViewModel
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.android.ext.koin.androidApplication
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

const val BASE_URL = "https://pokeapi.co/api/v2/"

val viewModelModule = module {
    viewModel { PokeListViewModel(get())}
    viewModel { (repo: PokeDetailsRepository , pokemonName : String) ->PokemonDetailsViewModel(repo, get(), pokemonName) }
}

val apiModule = module {
    fun providePokeService(retrofit: Retrofit): PokeService = retrofit.create(PokeService::class.java)

    single { providePokeService(get()) }
}

val netModule = module {

    fun provideHttpClient(): OkHttpClient {
        val logger = HttpLoggingInterceptor()
        logger.level = HttpLoggingInterceptor.Level.BASIC
        val okHttPClientBuilder = OkHttpClient.Builder().addInterceptor(logger)
        return okHttPClientBuilder.build()
    }

    fun provideRetrofit(client: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(client)
            .addConverterFactory(MoshiConverterFactory.create())
            .addCallAdapterFactory(CoroutineCallAdapterFactory())
            .build()
    }

    single { provideHttpClient() }
    single { provideRetrofit(get()) }
}

val databaseModule = module {

    fun provideDatabase(application: Application): PokeDataBase {
        return Room.databaseBuilder(application,
            PokeDataBase::class.java,
            "Pokemons.db")
            .build()
    }
    single { provideDatabase(androidApplication()) }
}

val repositoryModule = module {
    fun providePokeListRepository(api: PokeService, database: PokeDataBase): PokeRepository {
        return PokeRepository(api, database)
    }

    fun providePokeDetailRepository(api: PokeService, database: PokeDataBase): PokeDetailsRepository {
        return PokeDetailsRepository(api, database)
    }

    single { providePokeListRepository(get(), get())}
    single { providePokeDetailRepository(get(), get()) }
}