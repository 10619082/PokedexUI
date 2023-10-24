package com.example.pokedexui

import com.google.gson.annotations.SerializedName
import java.io.Serializable

class Pokemon (
    @SerializedName("number") var number : Int? = null,
    @SerializedName("name") var name : String? = null,
    @SerializedName("type") var type : String? = null,
    val imageUrl: String
): Serializable
