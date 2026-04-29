package com.example.mecanicsync.clientes.domain

import com.example.mecanicsync.clientes.data.ClienteRepository
import com.example.mecanicsync.clientes.ui.model.Cliente
import javax.inject.Inject




class GetClienteUseCase @Inject constructor(private val clienteRepository: ClienteRepository) {
    suspend operator fun invoke(): Result<List<Cliente>> {
        return try {
            val response = clienteRepository.getCliente()
            if (response.isSuccessful)
                Result.success(response.body()!!.map { it.toModel() })
            else
                Result.failure((Exception("Error en la peticion: ${response.code()}")))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}