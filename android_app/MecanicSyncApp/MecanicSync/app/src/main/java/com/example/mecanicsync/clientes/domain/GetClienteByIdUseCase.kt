package com.example.mecanicsync.clientes.domain

import com.example.mecanicsync.clientes.data.ClienteRepository
import com.example.mecanicsync.clientes.ui.model.Cliente
import javax.inject.Inject

class GetClienteByIdUseCase @Inject constructor(
    private val clienteRepository: ClienteRepository
) {
    suspend operator fun invoke(idCliente: Int): Cliente {
        return try {
            val result = clienteRepository.getClienteById(idCliente)
            result.fold(
                onSuccess = { it },
                onFailure = { throw it }
            )
        } catch (e: Exception) {
            throw Exception("Error al obtener cliente: ${e.message}", e)
        }
    }
}
