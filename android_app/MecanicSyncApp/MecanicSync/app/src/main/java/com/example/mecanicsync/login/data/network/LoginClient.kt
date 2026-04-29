package com.example.mecanicsync.login.data.network

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface LoginClient {
    @POST("api/login")
    suspend fun login(
        @Body request: LoginRequest): Response<LoginResponse>
}