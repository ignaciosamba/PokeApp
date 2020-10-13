package com.example.pokedexapp.data.network

import com.example.pokedexapp.data.model.Pokemon
import com.squareup.moshi.Json
import java.io.Serializable

data class PokeListResult(
    @field:Json(name = "results") val items : List<Pokemon> = emptyList()
) : Serializable