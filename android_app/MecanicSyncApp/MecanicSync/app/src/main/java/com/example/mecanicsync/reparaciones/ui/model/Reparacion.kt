package com.example.mecanicsync.reparaciones.ui.model

import android.os.Build
import androidx.annotation.RequiresApi
import com.example.mecanicsync.reparaciones.data.network.ReparacionesResponse
import java.time.LocalDate
import java.time.format.DateTimeFormatter

data class Reparacion(
    val id_reparacion: Int = 0,
    val id_vehiculo: Int = 0,
    val matricula: String = "",
    val marca: String = "",
    val modelo: String = "",
    val idTipo: Int? = null,
    val tipo_reparacion: String? = null,
    val descripcion_tipo: String? = null,
    val descripcion_adicional: String? = null,
    val estado: String = "",
    val fecha_inicio: String = "",
    val fecha_fin: String? = null,
    val horas_reales: Double? = null,
    val importe_total: Double? = null
) {

    @RequiresApi(Build.VERSION_CODES.O)
    fun formatDate(fecha: String): String {
        val input = DateTimeFormatter.ofPattern("yyyy-MM-dd")
        val output = DateTimeFormatter.ofPattern("dd/MM/yyyy")
        val date = LocalDate.parse(fecha, input)
        return date.format(output)
    }

    fun toResponse(): ReparacionesResponse {
        return ReparacionesResponse(
            id_reparacion = this.id_reparacion,
            id_vehiculo = this.id_vehiculo,
            matricula = this.matricula,
            marca = this.marca,
            modelo = this.modelo,
            id_tipo = this.idTipo ?: 0,
            tipo_reparacion = this.tipo_reparacion ?: "",
            descripcion_tipo = this.descripcion_tipo ?: "",
            descripcion_adicional = this.descripcion_adicional ?: "",
            estado = this.estado,
            fecha_inicio = this.fecha_inicio,
            fecha_fin = this.fecha_fin,
            horas_reales = this.horas_reales,
            importe_total = this.importe_total
        )
    }
}