package com.example.myapplication.data.model


data class LoginRequest(
    val email: String,
    val password: String
)

data class LoginResponse(
    val token: String,
    val userId: String,
    val role: String,
    val name: String,
    val message: String
)