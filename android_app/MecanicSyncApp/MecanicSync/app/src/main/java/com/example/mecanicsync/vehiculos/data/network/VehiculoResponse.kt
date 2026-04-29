package com.example.mecanicsync.vehiculos.data.network

import com.example.mecanicsync.vehiculos.ui.model.Vehiculo
import com.google.gson.annotations.SerializedName

data class VehiculoResponse (
    @SerializedName("idVehiculo")
    val id_vehiculo: Int = 0,
    @SerializedName("id_cliente")
    val id_cliente: Int = 0,
    @SerializedName("id_modelo")
    val id_modelo: Int = 0,
    @SerializedName("matricula")
    val matricula: String= "",
    @SerializedName("marca")
    val marca: String= "",
    @SerializedName("modelo")
    val modelo: String= "",
    @SerializedName("anioMatriculacion")
    val anioMatriculacion: Int=0,

){
    fun toModel(): Vehiculo{
        return Vehiculo(
            id_vehiculo,
            id_cliente,
            id_modelo,
            matricula,
            marca,
            modelo,
            anioMatriculacion)

    }
}
