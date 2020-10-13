package com.example.pokedexapp.data.model

import com.squareup.moshi.Json
import java.io.Serializable

data class PokemonAbility(
    @field:Json(name = "ability") val ability: Ability,
    @field:Json(name = "is_hidden") val isHiden: Boolean,
    @field:Json(name = "slot") val slot: Int
): Serializable