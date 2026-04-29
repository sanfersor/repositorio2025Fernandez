package com.example.mecanicsync.usuario.data.network

import com.example.mecanicsync.usuario.ui.model.Usuario
import com.google.gson.annotations.SerializedName

data class UsuarioResponse(
    @SerializedName("idUsuario") val idUsuario: Int,
    @SerializedName("usuario") val usuario: String,
    @SerializedName("email") val email: String,
    @SerializedName("rol") val rol: String
) {
    fun toModel(): Usuario {
        return Usuario(
            idUsuario = idUsuario,
            usuario = usuario,
            email = email,
            rol = rol
        )
    }
}
