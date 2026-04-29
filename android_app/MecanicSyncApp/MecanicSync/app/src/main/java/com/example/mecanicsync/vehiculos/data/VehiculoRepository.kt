package com.example.mecanicsync.vehiculos.data

import android.util.Log
import com.example.mecanicsync.clientes.data.network.ClienteResponse
import com.example.mecanicsync.clientes.ui.model.Cliente
import com.example.mecanicsync.marca.data.network.MarcaResponse
import com.example.mecanicsync.modelo.data.network.ModeloResponse
import com.example.mecanicsync.vehiculos.data.network.VehiculoCliente
import com.example.mecanicsync.vehiculos.data.network.VehiculoRequest
import com.example.mecanicsync.vehiculos.data.network.VehiculoResponse
import com.example.mecanicsync.vehiculos.ui.model.Vehiculo
import retrofit2.Response
import javax.inject.Inject

class VehiculoRepository @Inject constructor(
    private val vehiculoCliente: VehiculoCliente
) {
    suspend fun getVehiculos(): Response<List<VehiculoResponse>> {
        return vehiculoCliente.getVehiculos()
    }

    suspend fun getVehiculosEnTaller(): List<Vehiculo> {
        val response = vehiculoCliente.getVehiculosEnTaller() // Llama al endpoint optimizado

        if (!response.isSuccessful) {
            throw Exception("Error obteniendo vehiculos en taller: ${response.code()}")
        }

        // El servidor ya nos devuelve solo los vehiculos filtrados.
        return response.body()?.map { it.toModel() } ?: emptyList()
    }

    //Crear nuevo vehiculo
    // Lógica para crear un vehículo
    suspend fun createVehiculo(vehiculoRequest: VehiculoRequest): Result<Vehiculo> {
        return try {
            val response = vehiculoCliente.createVehiculo(vehiculoRequest)

            if (response.isSuccessful && response.body() != null) {
                val nuevoVehiculo = response.body()!!.toModel()
                Result.success(nuevoVehiculo)
            } else {
                Result.failure(Exception("Fallo al crear el vehiculo: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    // Métodos para cargar datos de los Spinners (usando la función auxiliar)
    suspend fun getClientes(): Result<List<ClienteResponse>> = safeApiCall {
        vehiculoCliente.getClientes()
    }

    suspend fun getMarcas(): Result<List<MarcaResponse>> = safeApiCall {
        vehiculoCliente.getMarcas()
    }

    suspend fun getModelosPorMarca(idMarca: Int): Result<List<ModeloResponse>> = safeApiCall {
        vehiculoCliente.getModelosPorMarca(idMarca)
    }

    // Función auxiliar para manejar Try-Catch y excepciones de la API de forma genérica
    private suspend inline fun <T> safeApiCall(crossinline call: suspend () -> T): Result<T> = try {
        Result.success(call())
    } catch (e: Exception) {
        // Podrías mapear 'e' a un mensaje de error específico para la UI
        Result.failure(e)
    }

    suspend fun deleteVehiculo(id: Int): Result<Unit> {
        return try {
            val response = vehiculoCliente.deleteVehiculo(id)
            Log.d("VehiculoRepository", "DELETE response: $response")
            if (response.isSuccessful) {
                Result.success(Unit)
            } else {
                Result.failure(Exception("Error eliminando vehículo: ${response.code()}"))
            }
        } catch (e: Exception) {
            Log.e("VehiculoRepository", "Error en DELETE", e)
            Result.failure(e)
        }
    }

    suspend fun updateVehiculo(id: Int, vehiculoRequest: VehiculoRequest): Result<Unit> {
        return try {
            val response = vehiculoCliente.updateVehiculo(id, vehiculoRequest)
            if (response.isSuccessful) Result.success(Unit)
            else Result.failure(Exception("Error actualizando vehículo: ${response.code()}"))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getVehiculoPorId(idVehiculo: Int): Result<Vehiculo> {
        return try {
            val response = vehiculoCliente.getVehiculoPorId(idVehiculo)
            if (response.isSuccessful && response.body() != null) {
                Result.success(response.body()!!.toModel())
            } else {
                Result.failure(Exception("Vehículo no encontrado: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
