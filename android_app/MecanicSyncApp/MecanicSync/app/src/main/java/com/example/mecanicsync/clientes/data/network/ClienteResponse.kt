package com.example.mecanicsync.clientes.data.network

import com.example.mecanicsync.clientes.ui.model.Cliente
import com.example.mecanicsync.vehiculos.data.network.VehiculoResponse
import com.google.gson.annotations.SerializedName




data class ClienteResponse(
    @SerializedName("idCliente")
    val id_cliente: Int = 0,

    @SerializedName("cliente")
    val cliente: String = "",

    @SerializedName("telefono")
    val telefono: String = "",

    @SerializedName("email")
    val email: String = "",

    @SerializedName("direccion")
    val direccion: String? = null,

    @SerializedName("fechaAlta")
    val fechaAlta: String? = null,

    @SerializedName("vehiculos")
    val vehiculos: List<VehiculoResponse> = emptyList()
) {
    fun toModel(): Cliente {
        return Cliente(
            id_cliente = id_cliente,
            cliente = cliente,
            telefono = telefono,
            email = email,
            direccion = direccion,
            fechaAlta = fechaAlta,
            vehiculos = vehiculos.map { it.toModel() }
        )
    }
}