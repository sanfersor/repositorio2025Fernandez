package com.example.mecanicsync.login.data

import com.example.mecanicsync.login.data.network.LoginRequest
import com.example.mecanicsync.login.data.network.LoginResponse
import com.example.mecanicsync.login.data.network.LoginClient

import javax.inject.Inject

class LoginRepository @Inject constructor(
    private val loginClient: LoginClient
) {
    suspend fun login(email: String, password: String): LoginResponse {
        val response = loginClient.login(LoginRequest(email, password))

        return if (response.isSuccessful) {
            response.body() ?: LoginResponse(false, message = "Respuesta vacía")
        } else {
            val errorBody = response.errorBody()?.string()
            LoginResponse(
                success = false,
                message = "Error de red: ${response.code()} - $errorBody"
            )
        }
    }
}
