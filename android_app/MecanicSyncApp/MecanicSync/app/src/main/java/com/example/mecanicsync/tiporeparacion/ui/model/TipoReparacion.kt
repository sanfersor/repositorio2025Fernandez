package com.example.mecanicsync.tiporeparacion.ui.model

data class TipoReparacion(
    val idTipo: Int,
    val reparacion: String,
    val descripcion: String? = null,
    val horasEstimadas: Double? = null,
    val precioHora: Double? = null,
    val precioMateriales: Double? = null
)