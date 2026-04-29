package com.example.mecanicsync.usuario.domain

import com.example.mecanicsync.clientes.data.ClienteRepository
import com.example.mecanicsync.clientes.data.network.ClienteResponse
import com.example.mecanicsync.usuario.data.UsuarioRepository
import com.example.mecanicsync.usuario.data.network.UsuarioResponse
import javax.inject.Inject

class UpdateUsuarioUseCase @Inject constructor(
    private val usuarioRepository: UsuarioRepository
) {
    suspend operator fun invoke(
        idUsuario: Int,
        usuarioResponse: UsuarioResponse
    ): Result<Unit> {
        return try {
            usuarioRepository.updateUsuario(idUsuario, usuarioResponse)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
