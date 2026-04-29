package com.example.mecanicsync.tiporeparacion.data

import com.example.mecanicsync.tiporeparacion.ui.model.TipoReparacion
import com.example.mecanicsync.tiporeparacion.data.TipoReparacionResponse
import retrofit2.Response
import javax.inject.Inject

class TipoReparacionRepository @Inject constructor(
    private val tipoReparacionCliente: TipoReparacionCliente

) {

    // 1) Obtener todos los tipos de reparación
    suspend fun getTiposReparacion(): Response<List<TipoReparacionResponse>> {
        return  tipoReparacionCliente.getTiposReparacion()

    }

    // 2) Obtener tipo por ID (si lo usas)
    suspend fun getTipoReparacionById(id: Int): Result<TipoReparacion> {
        return try {
            val response = tipoReparacionCliente.getTipoReparacionById(id)

            if (response.isSuccessful && response.body() != null) {
                Result.success(response.body()!!.toModel())
            } else {
                Result.failure(Exception("Tipo de reparación no encontrado: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }


}
