package com.example.pokedexapp.data.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.example.pokedexapp.data.database.PokeDataBase
import com.example.pokedexapp.data.model.Pokemon
import com.example.pokedexapp.data.network.PokeListResult
import com.example.pokedexapp.data.network.PokeService
import kotlinx.coroutines.flow.Flow
import timber.log.Timber

class PokeRepository(private val service: PokeService, private val database: PokeDataBase) {
    companion object {
        private const val NETWORK_PAGE_SIZE = 10
        private const val LIMIT = "150"
    }

    fun getSearchResultStream(
    ): Flow<PagingData<Pokemon>> {
        val pagingSourceFactory =  { database.pokemonsDao.getPokemons()}

        return Pager(
            config = PagingConfig(
                pageSize = NETWORK_PAGE_SIZE,
                enablePlaceholders = false
            ),
            remoteMediator = PokemonsRemoteMediator(LIMIT, service, database),
            pagingSourceFactory = pagingSourceFactory
        ).flow
    }
}