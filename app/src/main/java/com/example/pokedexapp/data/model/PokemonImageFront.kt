package com.example.pokedexapp.data.model

import com.squareup.moshi.Json
import java.io.Serializable

data class PokemonImageFront(
    @field:Json(name = "front_default") val image: String
): Serializable