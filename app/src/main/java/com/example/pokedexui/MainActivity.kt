package com.example.pokedexui

import android.media.MediaPlayer
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import pl.droidsonroids.gif.GifDrawable


class MainActivity : AppCompatActivity() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: PokemonAdapter
    private var pokemonList = ArrayList<Pokemon>()
    private var requestQueue: RequestQueue? = null
    private var mediaPlayer: MediaPlayer? = null



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        // Inizializzo il MediaPlayer e carico il file audio
        mediaPlayer = MediaPlayer.create(this, R.raw.pokemon_ruby_sapphire_emerald_littleroot_town) // Sostituisci "nome_file_audio" con il nome del tuo file audio
        mediaPlayer?.isLooping = true
        mediaPlayer?.start()

        val loadingImageView = findViewById<ImageView>(R.id.loadingImageView)

        // Mostro l'ImageView con la GIF di caricamento
        val imageResourceId = resources.getIdentifier("loading", "raw", packageName) // Trova l'ID della risorsa
        val gifDrawable = GifDrawable(resources, imageResourceId)
        loadingImageView.setImageDrawable(gifDrawable)
        loadingImageView.visibility = View.VISIBLE


        recyclerView = findViewById(R.id.pokemonRecyclerView)
        val gridLayoutManager = GridLayoutManager(this, 3) // Imposta il numero di colonne desiderato (3 in questo caso)
        recyclerView.layoutManager = gridLayoutManager



        adapter = PokemonAdapter(this,pokemonList,R.layout.pokemon_list_item)
        recyclerView.adapter = adapter


        requestQueue = Volley.newRequestQueue(this)

        fetchDataAndPopulateList(loadingImageView)

    }

    private fun fetchDataAndPopulateList(loadingImageView: ImageView) {
        val url = "https://pokeapi.co/api/v2/pokemon/?limit=151"
        val totalPokemonCount = 151 // Numero totale di Pokémon che stai cercando
        var completedApiCalls = 0

        val jsonObjectRequest = JsonObjectRequest(
            Request.Method.GET, url, null,
            { response ->

                val results = response.getJSONArray("results")
                for (i in 0 until results.length()) {
                    val pokemonObject = results.getJSONObject(i)
                    val name = pokemonObject.getString("name")
                    val url = pokemonObject.getString("url")
                    val number = extractPokemonNumberFromUrl(url)

                    val detailsUrl = "https://pokeapi.co/api/v2/pokemon/$number/"

                    // Eseguo una seconda chiamata API per ottenere la descrizione dei pokemon
                    val detailJsonObjectRequest = JsonObjectRequest(
                        Request.Method.GET, detailsUrl, null,
                        { detailResponse ->
                            val types = detailResponse.getJSONArray("types")
                            val typeNames = ArrayList<String>()
                            for (j in 0 until types.length()) {
                                val typeObject = types.getJSONObject(j)
                                val typeName = typeObject.getJSONObject("type").getString("name")
                                typeNames.add(typeName)
                            }

                            val imageUrl = "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/$number.png"

                            val pokemon = Pokemon(name = name.capitalize(), number = number, type = typeNames.joinToString(", "), imageUrl = imageUrl)

                            completedApiCalls ++
                            pokemonList.add(pokemon)

                            if (completedApiCalls == totalPokemonCount){
                                pokemonList.sortBy { it.number }
                                adapter.notifyDataSetChanged()
                                loadingImageView.visibility = View.GONE
                                recyclerView.visibility = View.VISIBLE

                            }

                        },
                        { detailError ->
                        }
                    )

                    requestQueue?.add(detailJsonObjectRequest)
                }
            },
            { error ->
            }
        )

        requestQueue?.add(jsonObjectRequest)

    }

    fun extractPokemonNumberFromUrl(url: String): Int {
        // L'URL ha il formato "https://pokeapi.co/api/v2/pokemon/{numero}/"
        val parts = url.split("/")
        return parts[parts.size - 2].toInt()
    }

    fun pauseAudio() {
        mediaPlayer?.pause()
    }

    fun stopAudio() {
        mediaPlayer?.stop()
        mediaPlayer?.release()
        mediaPlayer = null
    }

}