package com.example.pokedexapp.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.paging.LoadState
import com.example.pokedexapp.databinding.FragmentPokeListBinding
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class PokeListFragment : Fragment() {

    private val pokemonListViewModel by viewModel<PokeListViewModel>()

    private lateinit var binding: FragmentPokeListBinding
    private val adapter = PokemonAdapter()
    private var searchJob: Job? = null


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        setHasOptionsMenu(true)
        binding = FragmentPokeListBinding.inflate(inflater, container, false).apply {
            lifecycleOwner = viewLifecycleOwner
            viewModel = pokemonListViewModel
        }

        initAdapter()
        search()

        setHasOptionsMenu(true)
        return binding.root
    }

    private fun search() {
        // Cancel the previous job before creating a new one
        searchJob?.cancel()
        searchJob = lifecycleScope.launch {
            pokemonListViewModel.searchRepo().collectLatest{
                adapter.submitData(it)
            }
        }
    }

    private fun initAdapter() {
        binding.recyclerPokemons.adapter = adapter.withLoadStateHeaderAndFooter(
            header = PokemonLoadsStateAdapter { adapter.retry() },
            footer = PokemonLoadsStateAdapter { adapter.retry() }
        )

        adapter.addLoadStateListener { loadState ->
            // Only show the list if refresh succeeds.
            binding.recyclerPokemons.isVisible = loadState.refresh is LoadState.NotLoading
            // Show loading spinner during initial load or refresh.
            binding.pbLoading.isVisible = loadState.refresh is LoadState.Loading
            // Show the retry state if initial load or refresh fails.
            binding.retryButton.isVisible = loadState.refresh is LoadState.Error

            val errorState = loadState.source.append as? LoadState.Error
                ?: loadState.source.prepend as? LoadState.Error
                ?: loadState.append as? LoadState.Error
                ?: loadState.prepend as? LoadState.Error
            errorState?.let {
                Toast.makeText(
                    context,
                    "\uD83D\uDE28 Error: ${it.error}",
                    Toast.LENGTH_LONG
                ).show()
            }
        }
    }
}