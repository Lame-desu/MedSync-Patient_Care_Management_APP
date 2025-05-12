package com.example.myapplication.data.remote

import com.example.myapplication.data.model.staff.StaffRequest
import com.example.myapplication.data.model.staff.StaffResponse


import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST

interface StaffApi {
    @POST("api/admin/staff")
    suspend fun registerStaff(
        @Header("Authorization") authHeader: String,
        @Body request: StaffRequest
    ): StaffResponse
}
