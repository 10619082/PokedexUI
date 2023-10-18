package com.example.pokedexui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView


class PokemonAdapter(val pokemonList : List<Pokemon>, val itemLayout: Int) : RecyclerView.Adapter<PokemonAdapter.PokemonViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PokemonViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(itemLayout, parent, false)
        return PokemonViewHolder(view)
    }

    override fun onBindViewHolder(holder: PokemonViewHolder, position: Int) {
        val pokemon = pokemonList.get(position)
        holder.updatePokemon(pokemon)
    }

    override fun getItemCount(): Int {
        return pokemonList.size
    }

    inner class PokemonViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private var pokemonNameTextView: TextView? = null
        private var pokemonNumberTextView: TextView? = null
        private var pokemonTypeTextView: TextView? = null

        init{
            pokemonNameTextView= itemView.findViewById(R.id.pokemonName)
            pokemonNumberTextView= itemView.findViewById(R.id.pokemonNumber)
            pokemonTypeTextView= itemView.findViewById(R.id.pokemonType)
        }

        fun updatePokemon(pokemon: Pokemon) {
            pokemonNameTextView?.text = pokemon.name
            pokemonNumberTextView?.text = "No. ${pokemon.number}"
            pokemonTypeTextView?.text = "Type: ${pokemon.type}"
        }
    }
}

