package com.example.pokedexapp.data.model

import com.squareup.moshi.Json
import java.io.Serializable

data class Ability(
    @field:Json(name = "name") val abilityName: String,
    @field:Json(name = "url") val abilityUrl: String
): Serializable