package com.example.mecanicsync.dashboard.data.network

import retrofit2.Response
import retrofit2.http.GET

interface DashboardClient {
    @GET("api/clientes/dash")
    suspend fun getDashboard(): Response<DashboardResponse>
}
