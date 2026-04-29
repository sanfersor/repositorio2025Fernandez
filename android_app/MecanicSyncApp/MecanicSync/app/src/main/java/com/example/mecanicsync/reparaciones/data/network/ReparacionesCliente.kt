package com.example.mecanicsync.reparaciones.data.network

import com.example.mecanicsync.clientes.data.network.ClienteResponse
import com.example.mecanicsync.clientes.ui.model.Cliente
import com.example.mecanicsync.reparaciones.ui.model.Reparacion
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface ReparacionesCliente {
    @GET("/api/reparaciones")
    suspend fun getReparaciones(): Response<List<ReparacionesResponse>>

    @GET("/api/vehiculos/{id}/reparaciones")
    suspend fun getReparacionesVehiculo(@Path("id") idVehiculo: Int): Response<List<ReparacionesResponse>>


    @GET("/api/reparaciones/matricula/{matricula}")
    suspend fun getReparacionesPorMatricula(
        @Path("matricula") matricula: String
    ): Response<ReparacionesVehiculoResponse>

    @GET("api/reparaciones/{id}")
    suspend fun getReparacionById(@Path("id")idReparacion: Int): Response<ReparacionesResponse>

    //Crear reparacion
    @POST("api/reparaciones")
    suspend fun createReparacion(
        @Body reparacion: ReparacionesResponse
    ): Response<ReparacionesResponse>

    @DELETE("api/reparaciones/{id}")
    suspend fun deleteReparacion(@Path("id") id: Int): Response<Void>

    @PUT("api/reparaciones/{id}")
    suspend fun updateReparacion(
        @Path("id") id: Int,
        @Body reparacion: ReparacionesResponse
    ): Response<ReparacionesResponse>
}