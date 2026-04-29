package com.example.mecanicsync.usuario.domain

import com.example.mecanicsync.usuario.data.UsuarioRepository
import com.example.mecanicsync.usuario.ui.model.Usuario
import javax.inject.Inject

class GetDatosUsuarioUseCase @Inject constructor(
    private val repository: UsuarioRepository
) {
    suspend operator fun invoke(email: String): Result<Usuario> {
        return repository.getUsuarioPorEmail(email)
    }
}