package com.example.mecanicsync.reparaciones.data.network

import com.google.gson.annotations.SerializedName

data class ReparacionesVehiculoResponse(
    @SerializedName("idCliente")
    val idCliente: Int,
    @SerializedName("nombreCliente")
    val nombreCliente: String,
    @SerializedName("idVehiculo")
    val idVehiculo: Int,
    @SerializedName("matricula")
    val matricula: String,
    @SerializedName("modelo")
    val modelo: String?,
    @SerializedName("marca")
    val marca: String?,
    @SerializedName("reparaciones")
    val reparaciones: List<ReparacionesResponse> = emptyList()
)
