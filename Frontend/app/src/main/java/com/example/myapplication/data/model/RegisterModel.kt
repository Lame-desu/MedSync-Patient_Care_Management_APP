package com.example.myapplication.data.model

data class RegisterRequest(
    val name: String,
    val dateOfBirth: String?,
    val gender: String?,
    val age: Int?,
    val bloodGroup: String?,
    val emergencyContactName: String?,
    val emergencyContactNumber: String?,
    val email: String,
    val password: String,
    val role: String
)

data class RegisterResponse(
    val token: String,
    val userId: String,
    val role: String,
    val message: String?
)