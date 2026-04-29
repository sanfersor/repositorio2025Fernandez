package com.example.mecanicsync.login.data.network

import com.google.gson.annotations.SerializedName

data class LoginResponse(
    @SerializedName("success")
    val success: Boolean,

    @SerializedName("role")
    val role: String? = null,

    @SerializedName("usuario")
    val usuario: String? = null,

    @SerializedName("message")
    val message: String? = null
)