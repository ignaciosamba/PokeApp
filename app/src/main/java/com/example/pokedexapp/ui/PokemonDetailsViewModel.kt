package com.example.pokedexapp.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pokedexapp.data.database.PokeDataBase
import com.example.pokedexapp.data.repository.PokeDetailsRepository
import kotlinx.coroutines.launch
import timber.log.Timber
import java.io.IOException

class PokemonDetailsViewModel (private val repository: PokeDetailsRepository,
                               private val database: PokeDataBase,
                                private val name: String) : ViewModel() {

    val pokemonDetails = database.pokemonDetailsDao.getPokemonDetail(name)

    /**
     * Event triggered for network error.
     */
    private var _eventNetworkError = MutableLiveData<Boolean>(false)

    /**
     * Event triggered for network error.
     */
    val eventNetworkError: LiveData<Boolean>
        get() = _eventNetworkError

    /**
     * Flag to display the error message.
     */
    private var _isNetworkErrorShown = MutableLiveData<Boolean>(false)

    /**
     * Flag to display the error message.
     */
    val isNetworkErrorShown: LiveData<Boolean>
        get() = _isNetworkErrorShown

    /**
     * init{} Called immediately when this ViewModel is created.
     */
    init {
        refreshDataFromRepository(name)
    }

    /**
     * Refresh data from the repository.
     */
    fun refreshDataFromRepository(name: String) {
        viewModelScope.launch {
            try {
                repository.refreshPokemon(name)
                _eventNetworkError.value = false
                _isNetworkErrorShown.value = false

            } catch (networkError: IOException) {
                Timber.d(networkError)
            }
            if (pokemonDetails.value == null) {
                // TODO: because the first thing is search into the database,
                // the app always sets the _eventNetworkError as a true,
                // and because of that there toast will always search.
                // It must be fixed.
                _eventNetworkError.value = true
            }
        }
    }

    /**
     * Resets the network error flag.
     */
    fun onNetworkErrorShown() {
        _isNetworkErrorShown.value = true
    }
}