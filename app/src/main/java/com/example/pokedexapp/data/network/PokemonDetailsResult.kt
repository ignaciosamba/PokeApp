package com.example.pokedexapp.data.network

import com.example.pokedexapp.data.model.*
import com.squareup.moshi.Json
import java.io.Serializable


data class PokemonDetailsResult(
    @field:Json(name = "id") val pokemonId: Int,
    @field:Json(name = "abilities") val pokemonAbilities: List<PokemonAbility> = emptyList(),
    @field:Json(name = "name") val pokemonName : String,
    @field:Json(name = "types") val pokemonTypes: List<PokemonTypes> = emptyList(),
    @field:Json(name = "sprites") val pokemonSprites: PokemonSprites
) : Serializable

/**
 * Convert Network results to database objects
 */
fun PokemonDetailsResult.asDatabaseModel(): PokemonDetails {
    var abilities = StringBuilder()
    var types = StringBuilder()
    var stats = StringBuilder()
    for (ability: PokemonAbility in pokemonAbilities) {
        abilities.append(ability.ability.abilityName).append(" ")
    }
    for (type: PokemonTypes in pokemonTypes) {
        types.append(type.pokemonType.typeName).append(" ")
    }
    return PokemonDetails(
            pokemonId = pokemonId,
            pokemonName = pokemonName,
            pokemonType = types.toString(),
            pokemonAbility = abilities.toString(),
            pokemonSprites = pokemonSprites.other.pokemonImgOfficial.image)
}