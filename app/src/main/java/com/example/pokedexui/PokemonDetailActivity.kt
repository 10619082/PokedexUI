package com.example.pokedexui

import android.content.Intent
import android.media.MediaPlayer
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.google.gson.Gson
import org.json.JSONObject
import pl.droidsonroids.gif.GifDrawable


class PokemonDetailActivity : AppCompatActivity() {

    private var requestQueue: RequestQueue? = null
    private lateinit var descriptionTextView: TextView
    private var currNumber: Int = 0
    private var pendingRequests: Int = 0

    private lateinit var recyclerView: RecyclerView
    private lateinit var chainEvolutionAdapter: ChainEvolutionAdapter

    //Pokemon corrente e catena evolutiva corrente
    private lateinit var currentPokemon: Pokemon
    private var chainEvolution = ArrayList<Pokemon>()

    private lateinit var ivPreviousPokemon: ImageView
    private lateinit var ivNextPokemon: ImageView



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pokemon_detail)

        // Trova le ImageView nel layout
        ivPreviousPokemon = findViewById(R.id.btnPreviousPokemon)
        ivNextPokemon = findViewById(R.id.btnNextPokemon)

        ivPreviousPokemon.setOnClickListener {
            openPokemonDetailForPreviousPokemon()
        }

        ivNextPokemon.setOnClickListener {
            openPokemonDetailForNextPokemon()
        }

        // Recupero i dati del Pokémon dall'intent
        val currentPokemonSerialized  = intent.getStringExtra("selectedPokemon")
        currentPokemon = Gson().fromJson(currentPokemonSerialized , Pokemon::class.java)
        currNumber = currentPokemon.number!!


        val nameTextView = findViewById<TextView>(R.id.pokemonNameTextView)
        val typeTextView = findViewById<TextView>(R.id.pokemonTypeTextView)
        val numberTextView = findViewById<TextView>(R.id.pokemonNumberTextView)
        val imageView = findViewById<ImageView>(R.id.pokemonImageView)

        descriptionTextView = findViewById<TextView>(R.id.pokemonDescriptionTextView)

        //GIF
        nameTextView.text = currentPokemon.name!!
        typeTextView.text = "Type: ${currentPokemon.type}"
        numberTextView.text = "Number ID: " + "#${currentPokemon.number.toString().padStart(3, '0')}"
        val imageResourceId = resources.getIdentifier('t'+ currentPokemon.number.toString(), "raw", packageName) // Trova l'ID della risorsa

        if (imageResourceId != 0) {
            val gifDrawable = GifDrawable(resources, imageResourceId)
            imageView.setImageDrawable(gifDrawable)
        } else {
            imageView.setImageResource(R.drawable.placeholder_image)
        }

        requestQueue = Volley.newRequestQueue(this)
        getPokemonDescription(currentPokemon)
        getEvolutionChain(currentPokemon)

        getPokemonDescription(currentPokemon)

    }

    private fun openPokemonDetailForNextPokemon() {
        var nextNumber = 0
        if (currNumber == 151){
            nextNumber = 1

        }else{nextNumber = currNumber + 1}

        getPokemonNameAndTypeByNumber(nextNumber) { nameAndTypePair ->
            if (nameAndTypePair != null) {
                val pokemonName = nameAndTypePair.first
                val pokemonTypes = nameAndTypePair.second
                val imageUrl = "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/$nextNumber.png"
                val pokemon = Pokemon(name = pokemonName!!.capitalize(), number = nextNumber, imageUrl = imageUrl, type = pokemonTypes )
                val gson = Gson()
                val pokemonJson = gson.toJson(pokemon)
                intent.putExtra("selectedPokemon", pokemonJson)
                val mediaPlayer = MediaPlayer.create(this, R.raw.pokemon_button)
                mediaPlayer.start()
                startActivity(intent)

            } else {
                Toast.makeText(this, "Errore durante il recupero del nome del Pokémon", Toast.LENGTH_SHORT).show()
            }
        }

    }

    private fun openPokemonDetailForPreviousPokemon() {

        var nextNumber = 0
        if (currNumber == 1){
            nextNumber = 151

        }else{
            nextNumber = currNumber - 1
        }
        getPokemonNameAndTypeByNumber(nextNumber) { nameAndTypePair ->
            if (nameAndTypePair != null) {
                val pokemonName = nameAndTypePair.first
                val pokemonTypes = nameAndTypePair.second
                val imageUrl = "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/$nextNumber.png"
                val pokemon = Pokemon(name = pokemonName!!.capitalize(), number = nextNumber, imageUrl = imageUrl, type = pokemonTypes )
                val gson = Gson()
                val pokemonJson = gson.toJson(pokemon)
                intent.putExtra("selectedPokemon", pokemonJson)
                val mediaPlayer = MediaPlayer.create(this, R.raw.pokemon_button)
                mediaPlayer.start()
                startActivity(intent)

            } else {
                Toast.makeText(this, "Errore durante il recupero del nome del Pokémon", Toast.LENGTH_SHORT).show()
            }
        }
    }
    fun getPokemonNameAndTypeByNumber(pokemonNumber: Int, resultCallback: (Pair<String, String>?) -> Unit) {
        val url = "https://pokeapi.co/api/v2/pokemon/$pokemonNumber/"
        val jsonObjectRequest = JsonObjectRequest(
            Request.Method.GET, url, null,
            { response ->
                val pokemonName = response.getString("name")
                val types = response.getJSONArray("types")
                val pokemonTypes = ArrayList<String>()

                for (i in 0 until types.length()) {
                    val typeObject = types.getJSONObject(i)
                    val typeName = typeObject.getJSONObject("type").getString("name")
                    pokemonTypes.add(typeName)
                }

                val nameAndTypePair = Pair(pokemonName, pokemonTypes.joinToString(", "))

                resultCallback(nameAndTypePair)
            },
            { error ->
                resultCallback(null)
            }
        )
        requestQueue?.add(jsonObjectRequest)
    }
    fun getPokemonDescription(pokemon: Pokemon) {
        val url = "https://pokeapi.co/api/v2/pokemon-species/${pokemon.number}/"
        val jsonObjectRequest = JsonObjectRequest(
            Request.Method.GET, url, null,
            { response ->
                val descriptions = response.getJSONArray("flavor_text_entries")
                var englishDescription: String? = null

                for (i in 0 until descriptions.length()) {
                    val entry = descriptions.getJSONObject(i)
                    val language = entry.getJSONObject("language").getString("name")

                    if (language == "en") {
                        englishDescription = entry.getString("flavor_text")
                        break
                    }
                }

                if (englishDescription != null) {
                    descriptionTextView.text = englishDescription.replace("\n", " ")
                } else{
                    Toast.makeText(this, "Nessuna descrizione in italiano disponibile", Toast.LENGTH_SHORT).show()
                    finish()
                }
            },
            { error ->
                Toast.makeText(this, "Errore durante la connessione al database", Toast.LENGTH_SHORT).show()
                finish()
            }
        )
        requestQueue?.add(jsonObjectRequest)
    }

    private fun getEvolutionChain(pokemon: Pokemon) {

        val url = "https://pokeapi.co/api/v2/pokemon-species/${pokemon.number}/"

        val jsonObjectRequest = JsonObjectRequest(
            Request.Method.GET, url, null,
            { response ->
                val evolutionChainUrl = response.getJSONObject("evolution_chain")
                    .getString("url")

                fetchEvolutionChainDetails(evolutionChainUrl)

            },
            { error ->
            }
        )

        requestQueue?.add(jsonObjectRequest)
    }

    private fun fetchEvolutionChainDetails(url: String) {
        val jsonObjectRequest = JsonObjectRequest(
            Request.Method.GET, url, null,
            { response ->
                val chain = response.getJSONObject("chain")
                parseEvolutions(chain)
            },
            { error ->
            }
        )

        requestQueue?.add(jsonObjectRequest)
    }
    private fun parseEvolutions(chain: JSONObject) {

        pendingRequests++

        val species = chain.getJSONObject("species")
        val name = species.getString("name")
        val url = species.getString("url")
        val number = url.split('/')[url.split('/').size - 2].toInt()
        val imageUrl = "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/$number.png"




        //Voglio solo i primi 151 pokemon
        if(number<=151 && number != currNumber){
            //Ricavo il tipo del pokemon
            pendingRequests++

            getPokemonNameAndTypeByNumber(number) { nameAndTypePair ->
                if (nameAndTypePair != null) {
                    pendingRequests--
                    chainEvolution.add(
                        Pokemon(
                            name = name!!.capitalize(), number = number,
                            imageUrl = imageUrl, type = nameAndTypePair.second
                        )
                    )
                    checkPendingRequest()
                } else {
                    Toast.makeText(this@PokemonDetailActivity,
                        "Errore durante il recupero del nome del Pokémon",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }

        val evolvesTo = chain.getJSONArray("evolves_to")
        for (i in 0 until evolvesTo.length()) {
            val evolutionDetails = evolvesTo.getJSONObject(i)
            parseEvolutions(evolutionDetails)
        }
        pendingRequests --
        checkPendingRequest()
    }

    fun checkPendingRequest(){
        if (pendingRequests == 0){
            setupChainEvolutionRecyclerView()
        }
    }

    private fun setupChainEvolutionRecyclerView() {
        recyclerView = findViewById(R.id.chainEvolutionRecyclerView)
        val gridLayoutManager = GridLayoutManager(this, 2)
        recyclerView.layoutManager = gridLayoutManager

        chainEvolutionAdapter = ChainEvolutionAdapter(chainEvolution, this) { clickedPokemon ->
            // Creo un intent per aprire la pagina del Pokémon cliccato
            val intent = Intent(this, PokemonDetailActivity::class.java)

            // Passo l'oggetto Pokémon
            val gson = Gson()
            val pokemonJson = gson.toJson(clickedPokemon)
            intent.putExtra("selectedPokemon", pokemonJson)

            startActivity(intent)
        }
        recyclerView.adapter = chainEvolutionAdapter
    }

    override fun onBackPressed() {
        val intent = Intent(this, MainActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_REORDER_TO_FRONT
        startActivity(intent)
        finish()
    }


}
