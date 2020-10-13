package com.example.pokedexapp.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "pokemon_details")
data class PokemonDetails(
    @PrimaryKey
    val pokemonId: Int,
    val pokemonName: String,
    val pokemonType: String,
    val pokemonAbility: String,
    val pokemonSprites: String
)