package com.example.pokedexui

import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide

class PokemonDetailActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pokemon_detail)

        // Recupera i dati del Pokémon dall'intent
        val pokemonName = intent.getStringExtra("pokemon_name")
        val pokemonType = intent.getStringExtra("pokemon_type")
        val pokemonImageUrl = intent.getStringExtra("pokemon_image_url")

        // Mostra i dati nella tua interfaccia utente, inclusa l'immagine
        val nameTextView = findViewById<TextView>(R.id.pokemonNameTextView)
        val typeTextView = findViewById<TextView>(R.id.pokemonTypeTextView)
        val imageView = findViewById<ImageView>(R.id.pokemonImageView)

        nameTextView.text = pokemonName
        typeTextView.text = "Type: $pokemonType"

        // Carica l'immagine utilizzando Glide o un'altra libreria di caricamento immagini
        Glide.with(this)
            .load(pokemonImageUrl)
            .into(imageView)
    }
}
