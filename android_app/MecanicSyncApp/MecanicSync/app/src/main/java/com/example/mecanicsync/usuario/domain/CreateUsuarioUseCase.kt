package com.example.mecanicsync.usuario.domain


import com.example.mecanicsync.usuario.data.UsuarioRepository
import com.example.mecanicsync.usuario.ui.model.Usuario
import javax.inject.Inject

class CreateUsuarioUseCase @Inject constructor(
    private val usuarioRepository: UsuarioRepository
) {
    suspend operator fun invoke(usuario: Usuario): Result<Usuario>{
        return usuarioRepository.createUsuario(usuario)
    }
}