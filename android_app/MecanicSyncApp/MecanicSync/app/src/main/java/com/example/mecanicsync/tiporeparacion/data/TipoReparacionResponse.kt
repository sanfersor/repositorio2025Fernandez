package com.example.mecanicsync.tiporeparacion.data

import com.example.mecanicsync.tiporeparacion.ui.model.TipoReparacion
import com.google.gson.annotations.SerializedName

data class TipoReparacionResponse(
    @SerializedName("idTipo") val idTipo: Int,
    @SerializedName("reparacion") val reparacion: String,
    @SerializedName("descripcion") val descripcion: String?,
    @SerializedName("horasEstimadas") val horasEstimadas: Double?,
    @SerializedName("precioHora") val precioHora: Double?,
    @SerializedName("precioMateriales") val precioMateriales: Double?
) {
    fun toModel() = TipoReparacion(
        idTipo = idTipo,
        reparacion = reparacion,
        descripcion = descripcion,
        horasEstimadas = horasEstimadas,
        precioHora = precioHora,
        precioMateriales = precioMateriales
    )
}

