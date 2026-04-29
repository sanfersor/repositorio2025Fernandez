package com.example.mecanicsync.login.domain

import com.example.mecanicsync.login.data.LoginRepository
import com.example.mecanicsync.login.data.network.LoginResponse
import javax.inject.Inject


class LoginUseCase @Inject constructor(
    private val repository: LoginRepository
) {
    suspend operator fun invoke(email: String, password: String): LoginResponse {
        return repository.login(email, password)
    }
}