package com.example.mecanicsync.usuario.domain

import com.example.mecanicsync.usuario.data.UsuarioRepository
import javax.inject.Inject

class DeleteUsuarioUseCase @Inject constructor(
    private val usuarioRepository: UsuarioRepository
) {
    suspend operator fun invoke(idUsuario: Int): Result<Unit>{
        return usuarioRepository.deleteUsuario(idUsuario)
    }
}