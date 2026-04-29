package com.example.mecanicsync.login.ui.model

data class Login(
    val email: String = "",
    val password: String = "",
    val isLoading: Boolean = false,
    val role: String? = null,
    val errorMessage: String? = null
)