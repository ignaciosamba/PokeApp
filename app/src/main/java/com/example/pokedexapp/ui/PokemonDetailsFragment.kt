package com.example.pokedexapp.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.example.pokedexapp.R
import com.example.pokedexapp.data.model.PokemonDetails
import com.example.pokedexapp.data.repository.PokeDetailsRepository
import com.example.pokedexapp.databinding.FragmentPokeDetailsBinding
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class PokemonDetailsFragment : Fragment(){

    companion object {
        fun newInstance(pokemonName: String) = PokemonDetailsFragment().apply {
            arguments = bundleOf("pokemon" to pokemonName)
        }
    }
    private val pokemonDetailRepository: PokeDetailsRepository by inject {
           parametersOf(pokemonName)
    }
    private val pokemonDetailViewModel: PokemonDetailsViewModel by viewModel {
        parametersOf(pokemonDetailRepository ,pokemonName)
    }

    private lateinit var pokemonName: String
    private lateinit var binding: FragmentPokeDetailsBinding
    private var searchJob: Job? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_poke_details,
            container,
            false)

        val safeArgs: PokemonDetailsFragmentArgs by navArgs()
        pokemonName = safeArgs.pokemon

        binding.viewModel = pokemonDetailViewModel

        // Observer for the network error.
        pokemonDetailViewModel.eventNetworkError.observe(viewLifecycleOwner, Observer<Boolean> { isNetworkError ->
            if (isNetworkError) onNetworkError()
        })

        searchPokemon(pokemonName)

        // Observer for the selected pokemon details.
        pokemonDetailViewModel.pokemonDetails.observe(viewLifecycleOwner, Observer<PokemonDetails> { pokemon ->
            pokemon?.apply {
                binding.pokemonDetailName.text = pokemonName
                binding.pokemonDetailTypesTxt.text = pokemonType
                binding.pokemonDetailStatTxt.text = pokemonAbility
                context?.let { Glide.with(it).load(pokemonSprites).into(binding.pokemonDetailImg) }
            }
        })

        return binding.root
    }

    private fun searchPokemon(pokemonName: String) {
        // Cancel the previous job before creating a new one
        searchJob?.cancel()
        searchJob = lifecycleScope.launch {
            pokemonDetailViewModel.refreshDataFromRepository(pokemonName)
        }
    }

    /**
     * Method for displaying a Toast error message for network errors.
     */
    private fun onNetworkError() {
        if(!pokemonDetailViewModel.isNetworkErrorShown.value!!) {
            Toast.makeText(activity, "Network Error", Toast.LENGTH_LONG).show()
            pokemonDetailViewModel.onNetworkErrorShown()
        }
    }
}