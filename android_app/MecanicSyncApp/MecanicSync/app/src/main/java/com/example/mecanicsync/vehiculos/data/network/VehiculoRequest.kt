package com.example.mecanicsync.vehiculos.data.network

import com.google.gson.annotations.SerializedName

data class VehiculoRequest(
    @SerializedName("id_cliente") val idCliente: Int,
    @SerializedName("id_modelo") val idModelo: Int,
    val matricula: String,
    @SerializedName("anio_matriculacion") val anioMatriculacion: Int
)
