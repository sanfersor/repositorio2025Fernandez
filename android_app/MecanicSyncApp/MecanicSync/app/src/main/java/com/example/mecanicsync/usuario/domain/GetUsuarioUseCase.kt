package com.example.mecanicsync.usuario.domain

import com.example.mecanicsync.usuario.data.UsuarioRepository
import com.example.mecanicsync.usuario.ui.model.Usuario
import javax.inject.Inject

class GetUsuarioUseCase @Inject constructor(private val usuarioRepository: UsuarioRepository) {
    suspend operator fun invoke(): Result<List<Usuario>>{
        return try {
            val response = usuarioRepository.getUsuarios()
            if(response.isSuccessful)
                Result.success((response.body()!!.map{it.toModel()}))
            else
                Result.failure((Exception("Error en la peticion: ${response.code()}")))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}