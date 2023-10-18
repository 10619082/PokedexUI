package com.example.pokedexui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView


class MainActivity : AppCompatActivity() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: PokemonAdapter
    private var pokemonList = ArrayList<Pokemon>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        recyclerView = findViewById(R.id.pokemonRecyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)

        // Crea un adapter personalizzato per il RecyclerView
        adapter = PokemonAdapter(pokemonList,R.layout.pokemon_list_item)
        recyclerView.adapter = adapter

        // Recupera e visualizza i dati dei Pokémon nel tuo adapter
        fetchDataAndPopulateList()
    }

    private fun fetchDataAndPopulateList() {
        // Qui dovresti effettuare le chiamate API per recuperare i dati dei primi 151 Pokémon
        // e quindi aggiungerli all'adapter per la visualizzazione nella lista.
        // Assicurati di implementare un metodo per scaricare i dati e un modello Pokemon.

        // Esempio di aggiunta di un Pokémon fittizio per scopi di demo:
        val charmander = Pokemon("Charmander", 4, "Fire")
        pokemonList.add(charmander)
        adapter.notifyDataSetChanged()
    }
}