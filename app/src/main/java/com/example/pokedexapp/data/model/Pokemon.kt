package com.example.pokedexapp.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.squareup.moshi.Json
import java.io.Serializable

@Entity(tableName = "pokemon_list")
data class Pokemon(
    @PrimaryKey(autoGenerate = true)
    val pokemonId: Int,
    @Json(name = "name")val name: String,
    @Json(name = "url")val url: String
): Serializable