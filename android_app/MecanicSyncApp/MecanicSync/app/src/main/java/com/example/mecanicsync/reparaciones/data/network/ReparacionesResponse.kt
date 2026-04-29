package com.example.mecanicsync.reparaciones.data.network

import com.example.mecanicsync.reparaciones.ui.model.Reparacion
import com.google.gson.annotations.SerializedName

data class ReparacionesResponse(
    @SerializedName("id_reparacion") val id_reparacion: Int,
    @SerializedName("id_vehiculo") val id_vehiculo: Int,
    @SerializedName("matricula") val matricula: String,
    @SerializedName("marca") val marca: String,
    @SerializedName("modelo") val modelo: String,
    @SerializedName("id_tipo") val id_tipo: Int?,
    @SerializedName("reparacion") val tipo_reparacion: String?,
    @SerializedName("descripcion") val descripcion_tipo: String?,
    @SerializedName("descripcion_adicional") val descripcion_adicional: String? = "",
    @SerializedName("estado") val estado: String,
    @SerializedName("fecha_inicio") val fecha_inicio: String,
    @SerializedName("fecha_fin") val fecha_fin: String?,
    @SerializedName("horas_reales") val horas_reales: Double?,
    @SerializedName("importe_total") val importe_total: Double?,
) {
    fun toModel() = Reparacion(
        id_reparacion = id_reparacion,
        id_vehiculo = id_vehiculo,
        matricula = matricula,
        marca = marca,
        modelo = modelo,
        idTipo = id_tipo,
        tipo_reparacion = tipo_reparacion ?: "Sin tipo",
        descripcion_tipo = descripcion_tipo,
        descripcion_adicional = descripcion_adicional,
        estado = estado,
        fecha_inicio = fecha_inicio,
        fecha_fin = fecha_fin,
        horas_reales = horas_reales,
        importe_total = importe_total
    )
}
