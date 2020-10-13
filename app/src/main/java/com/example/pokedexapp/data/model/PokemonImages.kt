package com.example.pokedexapp.data.model

import com.squareup.moshi.Json
import java.io.Serializable

data class PokemonImages(
    @field:Json(name = "official-artwork") val pokemonImgOfficial: PokemonImageFront
): Serializable