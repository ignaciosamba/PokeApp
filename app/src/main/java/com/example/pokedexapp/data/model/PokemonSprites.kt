package com.example.pokedexapp.data.model

import com.squareup.moshi.Json
import java.io.Serializable

data class PokemonSprites(
    @field:Json(name = "other") val other: PokemonImages
): Serializable