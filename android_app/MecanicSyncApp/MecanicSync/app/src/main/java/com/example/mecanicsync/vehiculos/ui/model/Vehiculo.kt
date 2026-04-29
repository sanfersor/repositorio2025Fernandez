package com.example.mecanicsync.vehiculos.ui.model

import com.example.mecanicsync.reparaciones.ui.model.Reparacion

data class Vehiculo(
    val id_vehiculo: Int = 0,
    val id_cliente: Int = 0,
   val id_modelo: Int = 0,
   val matricula: String= "",
    val marca: String= "",
    val modelo: String= "",
   val anioMatriculacion: Int= 0,
   val reparaciones: List<Reparacion> = emptyList()
)
