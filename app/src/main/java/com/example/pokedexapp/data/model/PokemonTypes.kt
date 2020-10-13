package com.example.pokedexapp.data.model

import com.squareup.moshi.Json
import java.io.Serializable

data class PokemonTypes(
    @field:Json(name = "slot") val typeSlot: Int,
    @field:Json(name = "type") val pokemonType: PokemonItemType
): Serializable