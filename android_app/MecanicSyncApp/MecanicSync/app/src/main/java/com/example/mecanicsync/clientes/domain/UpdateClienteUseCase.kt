package com.example.mecanicsync.clientes.domain

import com.example.mecanicsync.clientes.data.ClienteRepository
import com.example.mecanicsync.clientes.data.network.ClienteResponse

import javax.inject.Inject

class UpdateClienteUseCase @Inject constructor(
    private val clienteRepository: ClienteRepository
) {
    suspend operator fun invoke(
        idCliente: Int,
        clienteResponse: ClienteResponse
    ): Result<Unit> {
        return try {
            clienteRepository.updateCliente(idCliente, clienteResponse)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
