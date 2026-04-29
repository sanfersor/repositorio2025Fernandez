package com.example.mecanicsync.usuario.domain


import com.example.mecanicsync.usuario.data.UsuarioRepository
import com.example.mecanicsync.usuario.ui.model.Usuario
import javax.inject.Inject

class GetUsuarioByIdUseCase @Inject constructor(
    private val usuarioRepository: UsuarioRepository
) {
    suspend operator fun invoke(idUsuario: Int): Usuario {
        return try {
            val result = usuarioRepository.getUsuarioById(idUsuario)
            result.fold(
                onSuccess = { it },
                onFailure = { throw it }
            )
        } catch (e: Exception) {
            throw Exception("Error al obtener usuario: ${e.message}", e)
        }
    }
}