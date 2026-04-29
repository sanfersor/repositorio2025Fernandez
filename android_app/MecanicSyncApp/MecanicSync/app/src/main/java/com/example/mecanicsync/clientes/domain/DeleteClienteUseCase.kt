package com.example.mecanicsync.clientes.domain

import com.example.mecanicsync.clientes.data.ClienteRepository
import javax.inject.Inject

class DeleteClienteUseCase @Inject constructor(
    private val clienteRepository: ClienteRepository
) {
    suspend operator fun invoke(idCliente: Int): Result<Unit>{
        return clienteRepository.deleteCliente(idCliente)
    }
}