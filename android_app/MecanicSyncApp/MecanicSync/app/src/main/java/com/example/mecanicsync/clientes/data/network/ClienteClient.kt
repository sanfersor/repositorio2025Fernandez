package com.example.mecanicsync.clientes.data.network

import com.example.mecanicsync.clientes.ui.model.Cliente
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface ClienteClient {
    @GET("api/clientes")
    suspend fun getCliente(): Response<List<ClienteResponse>>

    @GET("api/clientes/taller")
    suspend fun getClientesEnTaller(): Response<List<ClienteResponse>>

    //Crear cliente
    @POST("api/clientes")
    suspend fun createCliente(@Body cliente: Cliente): Response<ClienteResponse>
    //Eliminar cliente
    @DELETE("api/clientes/{id}")
    suspend fun deleteCliente(@Path("id") id: Int): Response<Void>

    @PUT("api/clientes/{id}")
    suspend fun updateCliente(
        @Path("id") id: Int,
        @Body cliente: ClienteResponse
    ): Response<ClienteResponse>

    @GET("api/clientes/{id}")
    suspend fun getClienteById(@Path("id") idCliente: Int): Response<ClienteResponse>


}