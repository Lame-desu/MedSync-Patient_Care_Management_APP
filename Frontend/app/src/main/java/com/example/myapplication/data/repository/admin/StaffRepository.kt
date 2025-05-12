package com.example.myapplication.data.repository.admin

import com.example.myapplication.data.model.AuthPreferences
import com.example.myapplication.data.model.staff.StaffRequest
import com.example.myapplication.data.remote.StaffApi
import kotlinx.coroutines.flow.flow

class StaffRepository(
    private val authPreferences: AuthPreferences,
    private val staffApi: StaffApi
) {
    fun registerStaff(request: StaffRequest) = flow {
        val token = authPreferences.getToken() ?: throw IllegalStateException("No token found")
        val response = staffApi.registerStaff("Bearer $token", request)
        if (response.success) {
            emit(response.data ?: throw IllegalStateException("No staff data returned"))
        } else {
            throw IllegalStateException(response.message ?: "Registration failed")
        }
    }
}