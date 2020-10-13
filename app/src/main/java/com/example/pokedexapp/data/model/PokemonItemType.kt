package com.example.pokedexapp.data.model

import com.squareup.moshi.Json
import java.io.Serializable

data class PokemonItemType(
    @field:Json(name = "name") val typeName: String,
    @field:Json(name = "url") val typeUrl: String
): Serializable