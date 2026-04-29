package com.example.mecanicsync.reparaciones.data

import android.util.Log
import com.example.mecanicsync.reparaciones.data.network.ReparacionesCliente
import com.example.mecanicsync.reparaciones.data.network.ReparacionesResponse
import com.example.mecanicsync.reparaciones.data.network.ReparacionesVehiculoResponse
import com.example.mecanicsync.reparaciones.ui.model.Reparacion
import retrofit2.Response
import javax.inject.Inject

@Suppress("UNCHECKED_CAST")
class ReparacionesRepository @Inject constructor(
    private val reparacionesCliente: ReparacionesCliente
) {
    suspend fun getReparaciones(): Response<List<ReparacionesResponse>> {
        return reparacionesCliente.getReparaciones()
    }

    suspend fun getReparacionesVehiculo(idVehiculo: Int): List<Reparacion> {
        val response = reparacionesCliente.getReparacionesVehiculo(idVehiculo)

        if (!response.isSuccessful) {
            throw Exception("Error al obtener reparaciones: ${response.code()}")
        }

        return response.body()?.map { it.toModel() } ?: emptyList()
    }

    // Obtener por matrícula
    suspend fun getReparacionesPorMatricula(matricula: String): Result<ReparacionesVehiculoResponse> {
        return try {
            val response = reparacionesCliente.getReparacionesPorMatricula(matricula)
            if (response.isSuccessful && response.body() != null) {
                Result.success(response.body()!!)
            } else {
                Result.failure(Exception("No encontrado o error: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun createReparacion(
        reparacion: ReparacionesResponse
    ): Result<Reparacion> {
        return try {
            val response = reparacionesCliente.createReparacion(reparacion)
            if (response.isSuccessful && response.body() != null) {
                Result.success(response.body()!!.toModel())
            } else {
                Result.failure(Exception("Error ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }



    suspend fun deleteReparacion(id: Int): Result<Unit> {
        return try {
            val response = reparacionesCliente.deleteReparacion(id)
            Log.d("ReparacionesRepository", "DELETE response: $response")
            if (response.isSuccessful) {
                Result.success(Unit)
            } else {
                Result.failure(Exception("Error eliminando reparacion: ${response.code()}"))
            }
        } catch (e: Exception) {
            Log.e("ReparacionRepositorio", "Error en DELETE", e)
            Result.failure(e)
        }
    }

    suspend fun updateReparacion(
        id: Int,
        reparacionesResponse: ReparacionesResponse
    ): Result<Unit> {
        return try {
            val response = reparacionesCliente.updateReparacion(id, reparacionesResponse)
            if (response.isSuccessful) Result.success(Unit)
            else Result.failure(Exception("Error actualizando reparacion: ${response.code()}"))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    suspend fun getReparacionById(id: Int): Result<Reparacion>{
        return try {
            val response = reparacionesCliente.getReparacionById(id)
            if (response.isSuccessful && response.body() != null) {
                Result.success(response.body()!!.toModel())
            } else {
                Result.failure(Exception("Reparacion no encontrado: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

}