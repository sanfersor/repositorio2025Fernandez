package com.example.mecanicsync.clientes.domain

import com.example.mecanicsync.clientes.data.ClienteRepository
import com.example.mecanicsync.clientes.ui.model.Cliente
import javax.inject.Inject

class GetClientesEnTallerUseCase @Inject constructor(
    private val clienteRepository: ClienteRepository
) {
    suspend operator fun invoke(): Result<List<Cliente>> {
        return try {
            Result.success(clienteRepository.getClientesEnTaller())
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}