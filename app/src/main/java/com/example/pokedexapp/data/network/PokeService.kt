package com.example.pokedexapp.data.network

import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface PokeService {
    @GET("pokemon")
    suspend fun searchPokemons(@Query("limit") queryLimit: String): PokeListResult

    @GET("pokemon/{name}")
    suspend fun searchPokemonDetail(@Path("name") name: String): PokemonDetailsResult
}