package com.example.pokedexapp.data.model

import androidx.room.Embedded
import com.squareup.moshi.Json
import java.io.Serializable

data class PokemonStats(
    @field:Json(name = "base_stat") val pokemonBaseStat: Int,
    @Embedded @field:Json(name = "stat") val pokemonStat: PokemonItemStat
): Serializable