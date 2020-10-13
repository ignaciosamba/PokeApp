package com.example.pokedexapp.data.database

import androidx.room.TypeConverter
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types

class DataConverter {
    val moshi = Moshi.Builder().build()
    val type = Types.newParameterizedType(List::class.java, String::class.java)
    val adapter: JsonAdapter<List<String>> = moshi.adapter(type)

    @TypeConverter
    fun fromAuthorsList(value: List<String>?): String? {
        return adapter.toJson(value)
    }

    @TypeConverter
    fun toAuthorsList(value: String?): List<String>? {
        return value?.let { adapter.fromJson(value) }
    }
}