package com.example.pokedexui

import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import pl.droidsonroids.gif.GifDrawable
class PokemonDetailActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pokemon_detail)

        // Recupera i dati del Pokémon dall'intent
        val pokemonName = intent.getStringExtra("pokemon_name")
        val pokemonType = intent.getStringExtra("pokemon_type")
        val pokemonImageUrl = intent.getStringExtra("pokemon_image_url")
        val pokemonNumber = 't' + pokemonName!!.split(':')[0].split('.')[1].split(' ')[1]



        // Mostra i dati nella tua interfaccia utente, inclusa l'immagine
        val nameTextView = findViewById<TextView>(R.id.pokemonNameTextView)
        val typeTextView = findViewById<TextView>(R.id.pokemonTypeTextView)
        val imageView = findViewById<ImageView>(R.id.pokemonImageView)

        //GIF
        nameTextView.text = pokemonName
        typeTextView.text = "Type: $pokemonType"

        val imageResourceId = resources.getIdentifier(pokemonNumber, "raw", packageName) // Trova l'ID della risorsa

        if (imageResourceId != 0) {
            val gifDrawable = GifDrawable(resources, imageResourceId)
            imageView.setImageDrawable(gifDrawable)
        } else {
            // GIF non trovata, puoi impostare un'immagine predefinita o un messaggio di errore
            imageView.setImageResource(R.drawable.placeholder_image)
        }

    }
}
