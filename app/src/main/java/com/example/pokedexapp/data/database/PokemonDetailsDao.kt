package com.example.pokedexapp.data.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.pokedexapp.data.model.PokemonDetails

@Dao
interface PokemonDetailsDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(pokemon: PokemonDetails)

    @Query("SELECT * FROM pokemon_details WHERE pokemonName = :pokemonName")
    fun getPokemonDetail(pokemonName: String): LiveData<PokemonDetails>

    @Query("DELETE FROM pokemon_details")
    fun clearPokemonDetails()
}