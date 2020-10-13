package com.example.pokedexapp.data.repository

import com.example.pokedexapp.data.database.PokeDataBase
import com.example.pokedexapp.data.network.PokeService
import com.example.pokedexapp.data.network.asDatabaseModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import timber.log.Timber

class PokeDetailsRepository (private val service: PokeService,
                             private val database: PokeDataBase) {

    /**
     * Refresh Pokemon details stored in the offline cache.
     */
    suspend fun refreshPokemon(name: String) {
        withContext(Dispatchers.IO) {
            Timber.d("refresh pokemon details");
            val pokemonInfo = service.searchPokemonDetail(name)
            database.pokemonDetailsDao.insertAll(pokemonInfo.asDatabaseModel())
        }
    }

}