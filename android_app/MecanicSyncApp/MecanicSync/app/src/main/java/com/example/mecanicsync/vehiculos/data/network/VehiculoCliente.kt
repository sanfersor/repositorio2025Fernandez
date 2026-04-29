package com.example.mecanicsync.vehiculos.data.network

import com.example.mecanicsync.clientes.data.network.ClienteResponse
import com.example.mecanicsync.marca.data.network.MarcaResponse
import com.example.mecanicsync.modelo.data.network.ModeloResponse
import com.example.mecanicsync.vehiculos.data.network.VehiculoResponse
import com.example.mecanicsync.vehiculos.ui.model.Vehiculo
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface VehiculoCliente {
    @GET("api/vehiculos")
    suspend fun getVehiculos(): Response<List<VehiculoResponse>>

    @GET("api/vehiculos/taller")
    suspend fun getVehiculosEnTaller(): Response<List<VehiculoResponse>>

    //  Obtener lista de clientes para el Spinner
    @GET("/api/clientes")
    suspend fun getClientes(): List<ClienteResponse>

    // Obtener lista de marcas para el primer Spinner
    @GET("/api/marcas")
    suspend fun getMarcas(): List<MarcaResponse>

    //  Obtener lista de modelos por marca ID
    @GET("/api/modelos/marca/{idMarca}")
    suspend fun getModelosPorMarca(@Path("idMarca") idMarca: Int): List<ModeloResponse>

    // Crear un nuevo vehiculo usando la clase Request
    @POST("api/vehiculos")
    suspend fun createVehiculo(@Body vehiculoRequest: VehiculoRequest): Response<VehiculoResponse>

    @DELETE("api/vehiculos/{id}")
    suspend fun deleteVehiculo(@Path("id") id: Int): Response<Void>


    @PUT("api/vehiculos/{id}")
    suspend fun updateVehiculo(
        @Path("id") id: Int,
        @Body vehiculoRequest: VehiculoRequest
    ): Response<VehiculoResponse>

    @GET("api/vehiculos/{id}")
    suspend fun getVehiculoPorId(@Path("id") idVehiculo: Int): Response<VehiculoResponse>


}
