package com.example.mecanicsync.tiporeparacion.data

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface TipoReparacionCliente {
    @GET("/api/tipos_reparacion")
    suspend fun getTiposReparacion(): Response<List<TipoReparacionResponse>>
    @GET("/api/tipos_reparacion/{id}")
    suspend fun getTipoReparacionById(@Path("id") id: Int): Response<TipoReparacionResponse>

}