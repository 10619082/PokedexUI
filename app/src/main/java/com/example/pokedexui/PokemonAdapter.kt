package com.example.pokedexui

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide



class PokemonAdapter(private val context: Context, val pokemonList : List<Pokemon>, val itemLayout: Int) : RecyclerView.Adapter<PokemonAdapter.PokemonViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PokemonViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(itemLayout, parent, false)
        return PokemonViewHolder(view)
    }

    // All'interno del metodo onBindViewHolder del tuo adapter
    override fun onBindViewHolder(holder: PokemonViewHolder, position: Int) {
        val pokemon = pokemonList[position]
        val capitalizedName = pokemon.name.capitalize() // Converti la prima lettera in maiuscolo
        val displayName = "No. ${pokemon.number}: $capitalizedName" // Concatena numero e nome

        // Imposta il clic su un elemento del RecyclerView
        holder.itemView.setOnClickListener {
            // Quando un Pokémon viene cliccato, crea un intent per avviare l'Activity dei dettagli
            val intent = Intent(context, PokemonDetailActivity::class.java)

            // Passa i dati del Pokémon come extras nell'intent
            intent.putExtra("pokemon_name", displayName)
            intent.putExtra("pokemon_type", pokemon.type)
            intent.putExtra("pokemon_image_url", pokemon.imageUrl)

            context.startActivity(intent)
        }

        holder.updatePokemon(pokemon)

        Glide.with(holder.itemView)
            .load(pokemon.imageUrl)
            .into(holder.getPokemonImageView()!!)
    }

    override fun getItemCount(): Int {
        return pokemonList.size
    }

    inner class PokemonViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private var pokemonNameTextView: TextView? = null
        private var pokemonNumberTextView: TextView? = null
        private var pokemonTypeTextView: TextView? = null
        private var pokemonImageView: ImageView? = null



        init{
            pokemonNameTextView= itemView.findViewById(R.id.pokemonName)
            pokemonImageView = itemView.findViewById(R.id.pokemonImage) // Associa l'ImageView

            //pokemonNumberTextView= itemView.findViewById(R.id.pokemonNumber)
            //pokemonTypeTextView= itemView.findViewById(R.id.pokemonType)
        }

        fun updatePokemon(pokemon: Pokemon) {
            pokemonNameTextView?.text = pokemon.name
            pokemonNumberTextView?.text = "No. ${pokemon.number}"
            pokemonTypeTextView?.text = "Type: ${pokemon.type}"
        }
        fun getPokemonImageView() : ImageView? {
            return this.pokemonImageView
        }

        fun getPokemonNameTextView() : TextView? {
            return this.pokemonNameTextView
        }
    }
}

