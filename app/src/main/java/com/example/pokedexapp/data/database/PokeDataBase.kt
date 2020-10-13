package com.example.pokedexapp.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.pokedexapp.data.model.Pokemon
import com.example.pokedexapp.data.model.PokemonDetails
import com.example.pokedexapp.data.model.RemoteKeys

@Database(entities = [Pokemon::class, PokemonDetails::class, RemoteKeys::class], version = 1, exportSchema = false)
@TypeConverters(DataConverter::class)
abstract class PokeDataBase : RoomDatabase() {
    abstract val pokemonsDao: PokemonsDao
    abstract val remoteKeysDao: RemoteKeysDao
    abstract val pokemonDetailsDao: PokemonDetailsDao
}