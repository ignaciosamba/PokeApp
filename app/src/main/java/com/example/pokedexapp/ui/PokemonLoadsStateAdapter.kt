package com.example.pokedexapp.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.paging.LoadState
import androidx.paging.LoadStateAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.pokedexapp.R
import com.example.pokedexapp.databinding.PokemonLoadStateItemBinding

class PokemonLoadsStateAdapter(private val retry: () -> Unit) : LoadStateAdapter<PokemonLoadStateViewHolder>() {
    override fun onBindViewHolder(holder: PokemonLoadStateViewHolder, loadState: LoadState) {
        holder.bind(loadState)
    }

    override fun onCreateViewHolder(parent: ViewGroup, loadState: LoadState): PokemonLoadStateViewHolder {
        return PokemonLoadStateViewHolder.create(parent, retry)
    }
}

class PokemonLoadStateViewHolder(
    private val binding: PokemonLoadStateItemBinding,
    retry: () -> Unit
) : RecyclerView.ViewHolder(binding.root) {

    init {
        binding.retryButton.setOnClickListener { retry.invoke() }
    }

    fun bind(loadState: LoadState) {
        if (loadState is LoadState.Error) {
            binding.errorMsg.text = loadState.error.localizedMessage
        }
        binding.progressBar.isVisible = loadState is LoadState.Loading
        binding.retryButton.isVisible = loadState !is LoadState.Loading
        binding.errorMsg.isVisible = loadState !is LoadState.Loading
    }

    companion object {
        fun create(parent: ViewGroup, retry: () -> Unit): PokemonLoadStateViewHolder {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.pokemon_load_state_item, parent, false)
            val binding = PokemonLoadStateItemBinding.bind(view)
            return PokemonLoadStateViewHolder(binding, retry)
        }
    }
}