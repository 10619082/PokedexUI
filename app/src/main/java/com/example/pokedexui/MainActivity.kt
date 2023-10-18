package com.example.pokedexui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley

class MainActivity : AppCompatActivity() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: PokemonAdapter
    private var pokemonList = ArrayList<Pokemon>()
    private var requestQueue: RequestQueue? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //recyclerView = findViewById(R.id.pokemonRecyclerView)
        //recyclerView.layoutManager = LinearLayoutManager(this)

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

                    val imageUrl = "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/$number.png"

                    //val pokemonName = pokemon.name //
                    //val displayName = "No. ${number}: $pokemonName" //
                    //Aggiungo pokemon all'adapter
                    val pokemon = Pokemon(name.capitalize(), number, "Tipo non ancora recuperato", imageUrl)
                    pokemonList.add(pokemon)
                }
                adapter.notifyDataSetChanged()
            },
            { error ->
                // Gestisci eventuali errori
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