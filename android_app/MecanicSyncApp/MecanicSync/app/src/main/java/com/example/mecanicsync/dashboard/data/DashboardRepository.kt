package com.example.mecanicsync.dashboard.data

import com.example.mecanicsync.dashboard.data.network.DashboardClient
import com.example.mecanicsync.dashboard.data.network.DashboardResponse
import javax.inject.Inject

class DashboardRepository @Inject constructor(
    private val dashboardClient: DashboardClient
) {
    suspend fun getDashboard(): DashboardResponse {
        val response = dashboardClient.getDashboard() // Retrofit call
        return if (response.isSuccessful) {
            response.body() ?: DashboardResponse()
        } else {
            DashboardResponse()
        }
    }
}

