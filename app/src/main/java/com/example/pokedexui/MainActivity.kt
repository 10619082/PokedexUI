package com.example.pokedexui

import android.media.MediaPlayer
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley



class MainActivity : AppCompatActivity() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: PokemonAdapter
    private var pokemonList = ArrayList<Pokemon>()
    private var requestQueue: RequestQueue? = null
    private var mediaPlayer: MediaPlayer? = null



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //recyclerView = findViewById(R.id.pokemonRecyclerView)
        //recyclerView.layoutManager = LinearLayoutManager(this)

        // Inizializza il MediaPlayer e carica il file audio
        mediaPlayer = MediaPlayer.create(this, R.raw.pokemon_ruby_sapphire_emerald_littleroot_town) // Sostituisci "nome_file_audio" con il nome del tuo file audio
        mediaPlayer?.isLooping = true // Per far ripetere la musica
        mediaPlayer?.start()


        recyclerView = findViewById(R.id.pokemonRecyclerView)
        val gridLayoutManager = GridLayoutManager(this, 3) // Imposta il numero di colonne desiderato (3 in questo caso)
        recyclerView.layoutManager = gridLayoutManager

        // Crea un adapter personalizzato per il RecyclerView
        adapter = PokemonAdapter(this,pokemonList,R.layout.pokemon_list_item)
        recyclerView.adapter = adapter




        // Recupera e visualizza i dati dei Pokémon nel tuo adapter
        requestQueue = Volley.newRequestQueue(this)
        fetchDataAndPopulateList()
    }

    private fun fetchDataAndPopulateList() {
        val url = "https://pokeapi.co/api/v2/pokemon/?limit=151"
        val totalPokemonCount = 151 // Numero totale di Pokémon che stai cercando
        var completedApiCalls = 0

        val jsonObjectRequest = JsonObjectRequest(
            Request.Method.GET, url, null,
            { response ->
                // Gestisci la risposta JSON per ottenere l'elenco di Pokémon
                val results = response.getJSONArray("results")
                for (i in 0 until results.length()) {
                    val pokemonObject = results.getJSONObject(i)
                    val name = pokemonObject.getString("name")
                    val url = pokemonObject.getString("url")
                    val number = extractPokemonNumberFromUrl(url)

                    val detailsUrl = "https://pokeapi.co/api/v2/pokemon/$number/"

                    // Esegui una seconda chiamata API per ottenere le informazioni dettagliate del Pokémon
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

                            // Crea un oggetto Pokemon con il nome, il numero e i tipi recuperati
                            val pokemon = Pokemon(name.capitalize(), number, typeNames.joinToString(", "), imageUrl)
                            //pokemonList.add(pokemon)

                            completedApiCalls ++
                            pokemonList.add(pokemon)

                            if (completedApiCalls == totalPokemonCount){
                                pokemonList.sortBy { it.number }
                                adapter.notifyDataSetChanged()
                            }

                        },
                        { detailError ->
                            // Gestisci eventuali errori nella chiamata dettagliata
                        }
                    )

                    requestQueue?.add(detailJsonObjectRequest)
                }
            },
            { error ->
                // Gestisci eventuali errori nella chiamata principale
            }
        )

        requestQueue?.add(jsonObjectRequest)

    }

    private fun extractPokemonNumberFromUrl(url: String): Int {
        // L'URL ha il formato "https://pokeapi.co/api/v2/pokemon/{numero}/"
        val parts = url.split("/")
        return parts[parts.size - 2].toInt()
    }
}