package com.example.mecanicsync.usuario.data

import android.util.Log
import com.example.mecanicsync.clientes.data.network.ClienteResponse
import com.example.mecanicsync.clientes.ui.model.Cliente
import com.example.mecanicsync.usuario.data.network.UsuarioClient
import com.example.mecanicsync.usuario.data.network.UsuarioResponse
import com.example.mecanicsync.usuario.ui.model.Usuario
import retrofit2.Response
import javax.inject.Inject

class UsuarioRepository @Inject constructor(
    private val usuarioClient: UsuarioClient
) {
    //Obtener todos los Usuarios
    suspend fun getUsuarios(): Response<List<UsuarioResponse>>{
        return usuarioClient.getUsuario()
    }
    suspend fun getUsuarioPorEmail(email: String): Result<Usuario> {
        return try {
            val response = usuarioClient.getUsuarioPorEmail(email)
            if (response.isSuccessful) {
                val body = response.body()
                if (body != null) {
                    Result.success(
                        Usuario(
                            idUsuario = body.idUsuario,
                            usuario = body.usuario,
                            email = body.email,
                            rol = body.rol
                        )
                    )
                } else {
                    Result.failure(Exception("Respuesta vacía del servidor"))
                }
            } else {
                Result.failure(Exception("Error al obtener datos: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    suspend fun deleteUsuario(id: Int): Result<Unit>{
        return try {
            val response = usuarioClient.deleteUsuario(id)
            Log.d("UsuarioRepository", "DELETE response: $response")
            if (response.isSuccessful) {
                Result.success(Unit)
            } else {
                Result.failure(Exception("Error eliminando usuario: ${response.code()}"))
            }
        } catch (e: Exception) {
            Log.e("UsuarioRepository", "Error en DELETE", e)
            Result.failure(e)
        }
    }
    suspend fun updateUsuario(id: Int, usuarioResponse: UsuarioResponse): Result<Unit> {
        return try {
            val response = usuarioClient.updateUsuario(id, usuarioResponse)
            if (response.isSuccessful) Result.success(Unit)
            else Result.failure(Exception("Error actualizando usuario: ${response.code()}"))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    suspend fun createUsuario(usuario: Usuario): Result<Usuario> {
        return try {
            val response = usuarioClient.createUsuario(usuario)

            if (response.isSuccessful && response.body() != null) {
                // Mapeamos el ClienteResponse devuelto por el servidor al Cliente de Dominio
                val nuevoCliente = response.body()!!.toModel()
                Result.success(nuevoCliente)
            } else {
                Result.failure(Exception("Fallo al crear cliente: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    suspend fun getUsuarioById(id: Int): Result<Usuario> {
        return try {
            val response = usuarioClient.getUsuarioById(id)
            if (response.isSuccessful && response.body() != null) {
                Result.success(response.body()!!.toModel())
            } else {
                Result.failure(Exception("Usuario no encontrado: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

}