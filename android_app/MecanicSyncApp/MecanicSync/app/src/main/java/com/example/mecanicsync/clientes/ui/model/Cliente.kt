package com.example.mecanicsync.clientes.ui.model

import com.example.mecanicsync.vehiculos.ui.model.Vehiculo

data class Cliente(
    val id_cliente: Int = 0,
    val cliente: String = "",
    val telefono: String = "",
    val email: String = "",
    val direccion: String? = null,
    val fechaAlta: String? = null,
    val vehiculos: List<Vehiculo> = emptyList()
)