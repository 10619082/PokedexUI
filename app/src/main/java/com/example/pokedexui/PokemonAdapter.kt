package com.example.pokedexui


import android.content.Context
import android.content.Intent
import android.graphics.drawable.GradientDrawable
import android.media.MediaPlayer
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.gson.Gson

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

    override fun onBindViewHolder(holder: PokemonViewHolder, position: Int) {
        val pokemon = pokemonList[position]
        val capitalizedName = pokemon.name!!.capitalize() // Converto la prima lettera in maiuscolo


        // Imposo il clic su un elemento del RecyclerView
        holder.itemView.setOnClickListener {

            val mediaPlayer = MediaPlayer.create(context, R.raw.pokemon_button)

            // Riproduco la canzone all'avvio
            mediaPlayer.start()
            // Quando un Pokémon viene cliccato, creo un intent per avviare l'Activity specifica del pokemon
            val intent = Intent(context, PokemonDetailActivity::class.java)

            //Passo l'oggetto pokemon
            val gson = Gson()
            val pokemonJson = gson.toJson(pokemon)
            intent.putExtra("selectedPokemon", pokemonJson)

            context.startActivity(intent)
        }




        // Imposto il colore dello sfondo in base al tipo del Pokémon


        val types = pokemon.type!!.split(",")
        if (types.size == 2) {
            val type1ColorRes = typeColors[types[0].trim().toLowerCase()] ?: R.color.default_color
            val type2ColorRes = typeColors[types[1].trim().toLowerCase()] ?: R.color.default_color

            val color1 = ContextCompat.getColor(context, type1ColorRes)
            val color2 = ContextCompat.getColor(context, type2ColorRes)

            // Imposto il colore dello sfondo diviso in due parti
            holder.itemView.background = GradientDrawable(
                GradientDrawable.Orientation.LEFT_RIGHT, intArrayOf(color1, color2)
            )
        } else {
            val typeColorRes = typeColors[pokemon.type!!.split(',')[0].toLowerCase()] ?: R.color.white
            val color = ContextCompat.getColor(context, typeColorRes)
            holder.itemView.setBackgroundColor(color)
        }

        //Carico immagine pokemon

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
        private var pokemonImageView: ImageView? = null

        init {
            pokemonNameTextView = itemView.findViewById(R.id.pokemonName)
            pokemonImageView = itemView.findViewById(R.id.pokemonImage)
            pokemonNumberTextView = itemView.findViewById(R.id.pokemonNumber)// Associa l'ImageView
        }

        fun updatePokemon(pokemon: Pokemon) {
            pokemonNameTextView?.text = pokemon.name
            pokemonNumberTextView?.text = "#${pokemon.number.toString().padStart(3, '0')}"
        }

        fun getPokemonImageView(): ImageView? {
            return this.pokemonImageView
        }
    }
}

