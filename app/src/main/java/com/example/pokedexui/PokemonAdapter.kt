package com.example.pokedexui
import android.media.MediaPlayer


import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

class PokemonAdapter(private val context: Context, val pokemonList: List<Pokemon>, val itemLayout: Int) : RecyclerView.Adapter<PokemonAdapter.PokemonViewHolder>() {
    private val typeColors = mapOf(
        "grass" to R.color.grass,
        "fire" to R.color.fire,
        "water" to R.color.water,
        "electric" to R.color.electric,
        "bug" to R.color.bug,
        "normal" to R.color.normal,
        "poison" to R.color.poison,
        "rock" to R.color.rock,
        "fighting" to R.color.fighting,
        "psychic" to R.color.psychic,
        "ground" to R.color.ground,
        "flying" to R.color.flying,
        "ghost" to R.color.ghost,
        "steel" to R.color.steel,
        "ice" to R.color.ice,
        "dragon" to R.color.dragon,
        "fairy" to R.color.fairy
    )

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

            val mediaPlayer = MediaPlayer.create(context, R.raw.pokemon_button)

            // Riproduci il suono
            mediaPlayer.start()
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

        // Imposta il colore dello sfondo in base al tipo del Pokémon
        val typeColorRes = typeColors[pokemon.type.split(',')[0].toLowerCase()] ?: R.color.white
        val color = ContextCompat.getColor(context, typeColorRes)
        holder.itemView.setBackgroundColor(color)

    }

    override fun getItemCount(): Int {
        return pokemonList.size
    }

    inner class PokemonViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private var pokemonNameTextView: TextView? = null
        private var pokemonNumberTextView: TextView? = null
        private var pokemonTypeTextView: TextView? = null
        private var pokemonImageView: ImageView? = null

        init {
            pokemonNameTextView = itemView.findViewById(R.id.pokemonName)
            pokemonImageView = itemView.findViewById(R.id.pokemonImage) // Associa l'ImageView
        }

        fun updatePokemon(pokemon: Pokemon) {
            pokemonNameTextView?.text = pokemon.name
        }

        fun getPokemonImageView(): ImageView? {
            return this.pokemonImageView
        }
    }
}

