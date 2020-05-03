package br.com.victorhgo.movieapp.data.model

import com.google.gson.annotations.SerializedName

data class SpokenLanguages(
    @SerializedName("iso_639_1") val iso_639_1: String,
    @SerializedName("name") val name: String
)