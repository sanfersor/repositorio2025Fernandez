package com.example.mecanicsync.modelo.data.network

import com.google.gson.annotations.SerializedName

data class ModeloResponse (
    @SerializedName("idModelo") val idModelo: Int,
    @SerializedName("modelo") val nombreModelo: String
)