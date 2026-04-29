package com.example.mecanicsync.clientes.data

import android.util.Log

import com.example.mecanicsync.clientes.data.network.ClienteClient
import com.example.mecanicsync.clientes.data.network.ClienteResponse
import com.example.mecanicsync.clientes.ui.model.Cliente
import retrofit2.Response
import javax.inject.Inject




class ClienteRepository @Inject constructor(
    private val clienteClient: ClienteClient,

) {
    // 1) Obtener todos los clientes
    suspend fun getCliente(): Response<List<ClienteResponse>> {
        return clienteClient.getCliente()
    }


    // 2) Obtener clientes en taller
    suspend fun getClientesEnTaller(): List<Cliente> {
        val response = clienteClient.getClientesEnTaller() // Llama al endpoint optimizado

        if (!response.isSuccessful) {
            throw Exception("Error obteniendo clientes en taller: ${response.code()}")
        }

        // El servidor ya nos devuelve solo los clientes filtrados.
        return response.body()?.map { it.toModel() } ?: emptyList()
    }

    //3) Nuevo cliente
    suspend fun createCliente(cliente: Cliente): Result<Cliente> {
        return try {
            val response = clienteClient.createCliente(cliente)

            if (response.isSuccessful && response.body() != null) {
                // Mapeamos el ClienteResponse devuelto por el servidor al Cliente de Dominio
                val nuevoCliente = response.body()!!.toModel()
                Result.success(nuevoCliente)
            } else {
                Result.failure(Exception("Fallo al crear cliente: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun deleteCliente(id: Int): Result<Unit> {
        return try {
            val response = clienteClient.deleteCliente(id)
            Log.d("ClienteRepository", "DELETE response: $response")
            if (response.isSuccessful) {
                Result.success(Unit)
            } else {
                Result.failure(Exception("Error eliminando cliente: ${response.code()}"))
            }
        } catch (e: Exception) {
            Log.e("ClienteRepository", "Error en DELETE", e)
            Result.failure(e)
        }
    }
    suspend fun updateCliente(id: Int, clienteResponse: ClienteResponse): Result<Unit> {
        return try {
            val response = clienteClient.updateCliente(id, clienteResponse)
            if (response.isSuccessful) Result.success(Unit)
            else Result.failure(Exception("Error actualizando cliente: ${response.code()}"))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getClienteById(id: Int): Result<Cliente> {
        return try {
            val response = clienteClient.getClienteById(id)
            if (response.isSuccessful && response.body() != null) {
                Result.success(response.body()!!.toModel())
            } else {
                Result.failure(Exception("Cliente no encontrado: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

}
