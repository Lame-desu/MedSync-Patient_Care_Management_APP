//package com.example.myapplication.data.repository
//
//import android.util.Log
//import com.example.myapplication.data.model.LoginRequest
//import com.example.myapplication.data.model.LoginResponse
//import com.example.myapplication.data.model.RegisterRequest
//import com.example.myapplication.data.model.RegisterResponse
//import com.example.myapplication.data.remote.NetworkProvider
//import retrofit2.HttpException
//import java.io.IOException
//
//class AuthRepository {
//    private val authApi = NetworkProvider.authApi
//
//    suspend fun login(email: String, password: String): Result<LoginResponse> {
//        return try {
//            val response = authApi.login(LoginRequest(email, password))
//            Result.success(response)
//        } catch (e: HttpException) {
//            val errorMessage = when (e.code()) {
//                401 -> "Wrong credentials"
//                400 -> "Invalid input"
//                500 -> "Server error"
//                else -> e.message() ?: "Unknown error"
//            }
//            Result.failure(Exception(errorMessage))
//        } catch (e: IOException) {
//            Result.failure(Exception("Network error: Check your connection"))
//        } catch (e: Exception) {
//            Result.failure(Exception(e.message ?: "Login failed"))
//        }
//    }
//
//    suspend fun register(
//        name: String,
//        dateOfBirth: String?,
//        gender: String?,
//        age: Int?,
//        bloodGroup: String?,
//        emergencyContactName: String?,
//        emergencyContactNumber: String?,
//        email: String,
//        password: String,
//        role: String
//    ): Result<RegisterResponse> {
//        return try {
//            val response = authApi.register(
//                RegisterRequest(
//                    name = name,
//                    dateOfBirth = dateOfBirth,
//                    gender = gender,
//                    age = age,
//                    bloodGroup = bloodGroup,
//                    emergencyContactName = emergencyContactName,
//                    emergencyContactNumber = emergencyContactNumber,
//                    email = email,
//                    password = password,
//                    role = role
//                )
//            )
//            Result.success(response)
//        } catch (e: HttpException) {
//            val errorMessage = when (e.code()) {
//                400 -> "Invalid input"
//                409 -> "Email already exists"
//                500 -> "Server error"
//                else -> e.message() ?: "Unknown error"
//            }
//            Result.failure(Exception(errorMessage))
//        } catch (e: IOException) {
//            Log.e("LoginError", "Network error: ${e.message}", e)
//            Result.failure(Exception("Network error: Check your connection"))
//        } catch (e: Exception) {
//            Result.failure(Exception(e.message ?: "Registration failed"))
//        }
//    }
//}



















//package com.example.myapplication.data.repository
//
//import android.util.Log
//import com.example.myapplication.data.model.LoginRequest
//import com.example.myapplication.data.model.LoginResponse
//import com.example.myapplication.data.model.RegisterRequest
//import com.example.myapplication.data.model.RegisterResponse
//import com.example.myapplication.data.remote.NetworkProvider
//import retrofit2.HttpException
//import java.io.IOException
//
//class AuthRepository {
//    private val authApi = NetworkProvider.authApi
//
//    // AuthRepository.kt
//    suspend fun login(email: String, password: String): Result<LoginResponse> {
//        return try {
//            val response = authApi.login(LoginRequest(email, password))
//            Result.success(response)
//        } catch (e: HttpException) {
//            Log.e("API_ERROR", "HTTP ${e.code()}: ${e.response()?.errorBody()?.string()}")
//            Result.failure(Exception("Server error: ${e.code()}"))
//        } catch (e: IOException) {
//            Log.e("API_ERROR", "Network error: ${e.message}")
//            Result.failure(Exception("Check your internet connection"))
//        } catch (e: Exception) {
//            Log.e("API_ERROR", "Unexpected error: ${e.stackTraceToString()}")
//            Result.failure(Exception("Login failed unexpectedly"))
//        }
//    }
//
//    suspend fun register(
//        name: String,
//        dateOfBirth: String?,
//        gender: String?,
//        age: Int?,
//        bloodGroup: String?,
//        emergencyContactName: String?,
//        emergencyContactNumber: String?,
//        email: String,
//        password: String,
//        role: String
//    ): Result<RegisterResponse> {
//        return try {
//            val response = authApi.register(
//                RegisterRequest(
//                    name = name,
//                    dateOfBirth = dateOfBirth,
//                    gender = gender,
//                    age = age,
//                    bloodGroup = bloodGroup,
//                    emergencyContactName = emergencyContactName,
//                    emergencyContactNumber = emergencyContactNumber,
//                    email = email,
//                    password = password,
//                    role = role
//                )
//            )
//            Result.success(response)
//        } catch (e: HttpException) {
//            val errorMessage = when (e.code()) {
//                400 -> "Invalid input"
//                409 -> "Email already exists"
//                500 -> "Server error"
//                else -> e.message() ?: "Unknown error"
//            }
//            Result.failure(Exception(errorMessage))
//        } catch (e: IOException) {
//            Log.e("LoginError", "Network error: ${e.message}", e)
//            Result.failure(Exception("Network error: Check your connection"))
//        } catch (e: Exception) {
//            Result.failure(Exception(e.message ?: "Registration failed"))
//        }
//    }
//}




















package com.example.myapplication.data.repository

import android.util.Log
import com.example.myapplication.data.model.LoginRequest
import com.example.myapplication.data.model.LoginResponse
import com.example.myapplication.data.model.RegisterRequest
import com.example.myapplication.data.model.RegisterResponse
import com.example.myapplication.data.remote.NetworkProvider
import retrofit2.HttpException
import java.io.IOException

class AuthRepository {
    private val authApi = NetworkProvider.authApi

    suspend fun login(email: String, password: String): Result<LoginResponse> {
        return try {
            val response = authApi.login(LoginRequest(email, password))
            Result.success(response)
        } catch (e: HttpException) {
            val errorBody = e.response()?.errorBody()?.string()
            Log.e("AuthRepository", "Login HTTP ${e.code()}: $errorBody")
            val errorMessage = when (e.code()) {
                401 -> "Invalid email or password"
                400 -> "Invalid input"
                500 -> "Server error"
                else -> errorBody ?: "Unknown error"
            }
            Result.failure(Exception(errorMessage))
        } catch (e: IOException) {
            Log.e("AuthRepository", "Login Network error: ${e.message}", e)
            Result.failure(Exception("Network error: Check your connection"))
        } catch (e: Exception) {
            Log.e("AuthRepository", "Login Unexpected error: ${e.message}", e)
            Result.failure(Exception("Login failed: ${e.message ?: "Unknown error"}"))
        }
    }

    suspend fun register(
        name: String,
        dateOfBirth: String?,
        gender: String?,
        age: Int?,
        bloodGroup: String?,
        emergencyContactName: String?,
        emergencyContactNumber: String?,
        email: String,
        password: String,
        role: String
    ): Result<RegisterResponse> {
        return try {
            val response = authApi.register(
                RegisterRequest(
                    name = name,
                    dateOfBirth = dateOfBirth,
                    gender = gender,
                    age = age,
                    bloodGroup = bloodGroup,
                    emergencyContactName = emergencyContactName,
                    emergencyContactNumber = emergencyContactNumber,
                    email = email,
                    password = password,
                    role = role
                )
            )
            Result.success(response)
        } catch (e: HttpException) {
            val errorBody = e.response()?.errorBody()?.string()
            Log.e("AuthRepository", "Register HTTP ${e.code()}: $errorBody")
            val errorMessage = when (e.code()) {
                400 -> "Invalid input: $errorBody"
                409 -> "Email already exists"
                500 -> "Server error"
                else -> errorBody ?: "Unknown error"
            }
            Result.failure(Exception(errorMessage))
        } catch (e: IOException) {
            Log.e("AuthRepository", "Register Network error: ${e.message}", e)
            Result.failure(Exception("Network error: Check your connection"))
        } catch (e: Exception) {
            Log.e("AuthRepository", "Register Unexpected error: ${e.message}", e)
            Result.failure(Exception("Registration failed: ${e.message ?: "Unknown error"}"))
        }
    }
}