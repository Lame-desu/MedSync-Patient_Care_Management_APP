package com.example.myapplication.data.repository.patient

import android.util.Log
import com.example.myapplication.data.model.AuthPreferences
import com.example.myapplication.data.model.patient.Appointment
import com.example.myapplication.data.model.patient.DashboardResponse
import com.example.myapplication.data.model.patient.PendingBooking
import com.example.myapplication.data.remote.NetworkProvider
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

class AppointmentRepository(
    private val authPreferences: AuthPreferences
) {
    fun getDashboardData(): Flow<DashboardResponse> = flow {
        val token = authPreferences.getToken() ?: throw Exception("No token")
        try {
            Log.d("AppointmentRepository", "Fetching dashboard with token: Bearer $token")
            val response = NetworkProvider.patientApi.getDashboard("Bearer $token")
            Log.d("AppointmentRepository", "Dashboard response: $response")
            if (response.success) {
                emit(response)
            } else {
                throw Exception(response.message ?: "Failed to fetch dashboard data")
            }
        } catch (e: Exception) {
            Log.e("AppointmentRepository", "Error fetching dashboard: ${e.message}", e)
            throw Exception("Failed to fetch dashboard: ${e.message}")
        }
    }.flowOn(Dispatchers.IO)

    fun cancelBooking(bookingId: String): Flow<DashboardResponse> = flow {
        val token = authPreferences.getToken() ?: throw Exception("No token")
        try {
            Log.d("AppointmentRepository", "Cancelling booking $bookingId with token: Bearer $token")
            val response = NetworkProvider.patientApi.cancelBooking("Bearer $token", bookingId)
            Log.d("AppointmentRepository", "Cancel booking response: $response")
            if (response.success) {
                emit(response)
            } else {
                throw Exception(response.message ?: "Failed to cancel booking")
            }
        } catch (e: Exception) {
            Log.e("AppointmentRepository", "Error cancelling booking: ${e.message}", e)
            throw Exception("Failed to cancel booking: ${e.message}")
        }
    }.flowOn(Dispatchers.IO)
}