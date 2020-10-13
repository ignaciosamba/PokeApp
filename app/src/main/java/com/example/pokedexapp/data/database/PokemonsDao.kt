package com.example.pokedexapp.data.database

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.pokedexapp.data.model.Pokemon

@Dao
interface PokemonsDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(books: List<Pokemon>)

    @Query("DELETE FROM pokemon_list")
    suspend fun clearPokemons()

    @Query("SELECT * FROM pokemon_list")
    fun getPokemons(): PagingSource<Int, Pokemon>

    @Query("SELECT * FROM pokemon_list")
    fun getPokemonsItems(): List<Pokemon>
}