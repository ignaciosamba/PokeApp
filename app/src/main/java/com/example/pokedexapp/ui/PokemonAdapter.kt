package com.example.pokedexapp.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.pokedexapp.data.model.Pokemon
import com.example.pokedexapp.databinding.PokeListItemBinding

class PokemonAdapter : PagingDataAdapter<Pokemon, RecyclerView.ViewHolder>(POKEMON_COMPARATOR) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return PokemonViewHolder.create(parent)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val pokemon = getItem(position)
        if (pokemon != null) {
            (holder as PokemonViewHolder).apply {
                bind(createOnClickListener(pokemon), pokemon)
            }
        }
    }

    private fun createOnClickListener(pokemon: Pokemon): View.OnClickListener {
        return View.OnClickListener {
            val direction = PokeListFragmentDirections.actionPokemonlistDestToPokemonDetailDest(pokemon.name)
            it.findNavController().navigate(direction)
        }
    }

    companion object {
        private val POKEMON_COMPARATOR = object : DiffUtil.ItemCallback<Pokemon>() {
            override fun areItemsTheSame(oldItem: Pokemon, newItem: Pokemon): Boolean =
                oldItem.pokemonId == newItem.pokemonId

            override fun areContentsTheSame(oldItem: Pokemon, newItem: Pokemon): Boolean =
                oldItem == newItem
        }
    }
}

class PokemonViewHolder(private val binding: PokeListItemBinding) : RecyclerView.ViewHolder(binding.root) {
    fun bind(listener: View.OnClickListener, item: Pokemon) {
        binding.apply {
            clickListener = listener
            pokemon = item
            executePendingBindings()
        }
    }

    companion object {
        fun create(parent: ViewGroup): PokemonViewHolder {
            val view = PokeListItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent, false)
            return PokemonViewHolder(view)
        }
    }
}