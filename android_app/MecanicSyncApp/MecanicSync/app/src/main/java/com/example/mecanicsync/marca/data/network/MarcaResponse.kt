package com.example.mecanicsync.marca.data.network

import com.google.gson.annotations.SerializedName

data class MarcaResponse (
    @SerializedName("idMarca") val idMarca: Int,
    @SerializedName("marca") val nombreMarca: String
)