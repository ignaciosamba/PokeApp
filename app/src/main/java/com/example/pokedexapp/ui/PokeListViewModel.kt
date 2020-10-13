package com.example.pokedexapp.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.pokedexapp.data.model.Pokemon
import com.example.pokedexapp.data.repository.PokeRepository
import kotlinx.coroutines.flow.Flow

class PokeListViewModel (private val repository: PokeRepository) : ViewModel() {

    private var currentSearchResult: Flow<PagingData<Pokemon>>? = null

    /**
     * Refresh data from the repository.
     */
    fun searchRepo(): Flow<PagingData<Pokemon>> {
        val newResult: Flow<PagingData<Pokemon>> = repository.getSearchResultStream()
            .cachedIn(viewModelScope)
        currentSearchResult = newResult
        return newResult
    }
}