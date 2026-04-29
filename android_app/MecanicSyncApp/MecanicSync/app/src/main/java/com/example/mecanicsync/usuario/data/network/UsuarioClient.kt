package com.example.mecanicsync.usuario.data.network

import com.example.mecanicsync.clientes.data.network.ClienteResponse
import com.example.mecanicsync.clientes.ui.model.Cliente
import com.example.mecanicsync.usuario.ui.model.Usuario
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface UsuarioClient {
    @GET("api/usuarios/email/{email}")
    suspend fun getUsuarioPorEmail(@Path("email") email: String): Response<UsuarioResponse>

    @GET("api/usuarios")
    suspend fun getUsuario(): Response<List<UsuarioResponse>>

    @DELETE("api/usuarios/{id}")
    suspend fun deleteUsuario(@Path("id") id: Int): Response<Void>

    @PUT("api/usuarios/{id}")
    suspend fun updateUsuario(
        @Path("id") id: Int,
        @Body usuario: UsuarioResponse
    ): Response<UsuarioResponse>

    @POST("api/usuarios")
    suspend fun createUsuario(@Body usuario: Usuario): Response<UsuarioResponse>

    @GET("api/usuarios/{id}")
    suspend fun getUsuarioById(@Path("id")idUsuario: Int): Response<UsuarioResponse>
}

