package com.example.pokedexapp.data.model

import com.squareup.moshi.Json
import java.io.Serializable

data class PokemonItemStat(
    @Json(name = "name") val statName: String,
    @Json(name = "url") val statUrl: String
): Serializable