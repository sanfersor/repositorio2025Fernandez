package com.example.mecanicsync.clientes.domain

import com.example.mecanicsync.clientes.data.ClienteRepository
import com.example.mecanicsync.clientes.ui.model.Cliente
import javax.inject.Inject

class CreateClienteUseCase @Inject constructor(
    private val clienteRepository: ClienteRepository
) {
    suspend operator fun invoke(cliente: Cliente): Result<Cliente>{
        return clienteRepository.createCliente(cliente)
    }
}