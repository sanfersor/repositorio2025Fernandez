package com.example.mecanicsync.dashboard.data.network

import com.google.gson.annotations.SerializedName

data class DashboardResponse(
    @SerializedName("clientesEnTaller")
    val clientesEnTaller: Long = 0,

    @SerializedName("vehiculosEnTaller")
    val vehiculosEnTaller: Long = 0,

    @SerializedName("totalReparaciones")
    val totalReparaciones: Long = 0,

    @SerializedName("pendientes")
    val pendientes: Long = 0,

    @SerializedName("enProceso")
    val enProceso: Long = 0,

    @SerializedName("finalizadas")
    val finalizadas: Long = 0
)
