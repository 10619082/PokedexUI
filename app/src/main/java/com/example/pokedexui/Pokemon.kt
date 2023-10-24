package com.example.pokedexui

import com.google.gson.annotations.SerializedName
import java.io.Serializable

class Pokemon (

    @SerializedName("abilities"                ) var abilities              : ArrayList<String>?   = arrayListOf(),
    //@SerializedName("base_experience"          ) var baseExperience         : Int?                   = null,
    @SerializedName("forms"                    ) var forms                  : ArrayList<String> ?      = arrayListOf(),
   // @SerializedName("game_indices"             ) var gameIndices            : ArrayList<GameIndices> = arrayListOf(),
    //@SerializedName("height"                   ) var height                 : Int?                   = null,
    //@SerializedName("held_items"               ) var heldItems              : ArrayList<HeldItems>   = arrayListOf(),
    @SerializedName("number"                       ) var number                     : Int?                   = null,
    //@SerializedName("is_default"               ) var isDefault              : Boolean?               = null,
    @SerializedName("location_area_encounters" ) var locationAreaEncounters : String?                = null,
    @SerializedName("moves"                    ) var moves                  : ArrayList<String> ?      = arrayListOf(),
    @SerializedName("name"                     ) var name                   : String?                = null,
    @SerializedName("order"                    ) var order                  : Int?                   = null,
    //@SerializedName("past_abilities"           ) var pastAbilities          : ArrayList<String>      = arrayListOf(),
    //@SerializedName("past_types"               ) var pastTypes              : ArrayList<String>      = arrayListOf(),
    //@SerializedName("species"                  ) var species                : Species?               = Species(),
    //@SerializedName("sprites"                  ) var sprites                : Sprites?               = Sprites(),
    @SerializedName("stats"                    ) var stats                  : ArrayList<String>?    = arrayListOf(),
    @SerializedName("type"                    ) var type : String? = null,
    @SerializedName("weight"                   ) var weight                 : Int?                   = null,
    val imageUrl: String
): Serializable
