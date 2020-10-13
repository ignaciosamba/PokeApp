package com.example.pokedexapp.data.repository

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import com.example.pokedexapp.data.database.PokeDataBase
import com.example.pokedexapp.data.model.Pokemon
import com.example.pokedexapp.data.model.RemoteKeys
import com.example.pokedexapp.data.network.PokeListResult
import com.example.pokedexapp.data.network.PokeService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.single
import retrofit2.HttpException
import timber.log.Timber
import java.io.IOException
import java.io.InvalidObjectException

private const val POKEMONS_STARTING_PAGE_INDEX = 0

@OptIn(ExperimentalPagingApi::class)
class PokemonsRemoteMediator (private val limit: String,
                              private val service: PokeService,
                              private val pokeDatabase: PokeDataBase
) : RemoteMediator<Int, Pokemon>() {
    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, Pokemon>
    ): MediatorResult {
        val page = when (loadType) {
            LoadType.REFRESH -> {
                Timber.d("LoadType = REFRESH")
                val remoteKeys = getRemoteKeyClosestToCurrentPosition(state)
                remoteKeys?.nextKey?.minus(1) ?: POKEMONS_STARTING_PAGE_INDEX
            }
            LoadType.PREPEND -> {
                //Because a race condition.
                val remoteKeys = flowOf(getRemoteKeyForFirstItem(state)).flowOn(Dispatchers.IO).single()
                if (remoteKeys == null) {
                    throw InvalidObjectException("Remote key and the prevKey should not be null")
                }
                // If the previous key is null, then we can't request more data
                val prevKey = remoteKeys.prevKey ?: return MediatorResult.Success(endOfPaginationReached = true)
                remoteKeys.prevKey
            }
            LoadType.APPEND -> {
                val remoteKeys = getRemoteKeyForLastItem(state)
                if (remoteKeys?.nextKey == null) {
                    throw InvalidObjectException("Remote key should not be null for $loadType")
                }
                remoteKeys.nextKey
            }

        }

        try {
            val apiResponse = service.searchPokemons(limit)

            val pokemons = apiResponse.items
            val endOfPaginationReached = pokemons.isEmpty()
            pokeDatabase.withTransaction {
                // clear all tables in the database
                if (loadType == LoadType.REFRESH) {
                    pokeDatabase.pokemonsDao.clearPokemons()
                    pokeDatabase.pokemonDetailsDao.clearPokemonDetails()
                }
                val prevKey = if (page == POKEMONS_STARTING_PAGE_INDEX) null else page - 1
                val nextKey = if (endOfPaginationReached) null else page + 1
                Timber.d("prevKey = $prevKey, nextKey = $nextKey")
                pokeDatabase.pokemonsDao.insert(pokemons)
                val keys = pokeDatabase.pokemonsDao.getPokemonsItems().map {
                    RemoteKeys(pokemonId = (it.pokemonId), prevKey = prevKey, nextKey = nextKey)
                }
                pokeDatabase.remoteKeysDao.insertAll(keys)
            }
            return MediatorResult.Success(endOfPaginationReached = endOfPaginationReached)
        } catch (exception: IOException) {
            return MediatorResult.Error(exception)
        } catch (exception: HttpException) {
            return MediatorResult.Error(exception)
        }
    }

    private suspend fun getRemoteKeyForLastItem(state: PagingState<Int, Pokemon>): RemoteKeys? {
        // Get the last page that was retrieved, that contained items.
        // From that last page, get the last item
        return state.pages.lastOrNull() { it.data.isNotEmpty() }?.data?.lastOrNull()
            ?.let { pokemon ->
                // Get the remote keys of the last item retrieved
                pokeDatabase.remoteKeysDao.remoteKeysPokemonId(pokemon.pokemonId)
            }
    }

    private suspend fun getRemoteKeyForFirstItem(state: PagingState<Int, Pokemon>): RemoteKeys? {
        // Get the first page that was retrieved, that contained items.
        // From that first page, get the first item
        return state.pages.firstOrNull { it.data.isNotEmpty() }?.data?.firstOrNull()
            ?.let { pokemon ->
                // Get the remote keys of the first items retrieved
                pokeDatabase.remoteKeysDao.remoteKeysPokemonId(pokemon.pokemonId)
            }
    }

    private suspend fun getRemoteKeyClosestToCurrentPosition(
        state: PagingState<Int, Pokemon>
    ): RemoteKeys? {
        // The paging library is trying to load data after the anchor position
        // Get the item closest to the anchor position
        return state.anchorPosition?.let { position ->
            state.closestItemToPosition(position)?.pokemonId?.let { pokemonId ->
                pokeDatabase.remoteKeysDao.remoteKeysPokemonId(pokemonId)
            }
        }
    }
}