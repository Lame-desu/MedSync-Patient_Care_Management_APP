package com.example.myapplication.data.repository.triage

import android.util.Log
import com.example.myapplication.data.model.AuthPreferences
import com.example.myapplication.data.model.triage.Booking
import com.example.myapplication.data.model.doctor.Doctor
import com.example.myapplication.data.model.triage.TriageData
import com.example.myapplication.data.remote.TriageApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

data class TriagePatient(
    val id: String,
    val name: String
)

class TriageRepository(
    private val authPreferences: AuthPreferences,
    private val triageApi: TriageApi
) {
    fun getTriagePatients(): Flow<List<TriagePatient>> = flow {
        val token = authPreferences.getToken() ?: throw Exception("No token")
        try {
            Log.d("TriageRepository", "Fetching all bookings with token: Bearer $token")
            val response = triageApi.getAllBookings("Bearer $token")
            Log.d("TriageRepository", "Get bookings response: $response")
            if (response.success) {
                val bookings = response.data ?: emptyList()
                val patients = bookings
                    .groupBy { it.patientId.id }
                    .map { (id, bookings) ->
                        TriagePatient(
                            id = id,
                            name = bookings.first().patientName
                        )
                    }
                emit(patients)
            } else {
                throw Exception(response.message ?: "Failed to fetch bookings")
            }
        } catch (e: Exception) {
            Log.e("TriageRepository", "Error fetching bookings: ${e.message}", e)
            throw Exception("Failed to fetch patients: ${e.message}")
        }
    }.flowOn(Dispatchers.IO)

    fun getDoctors(): Flow<List<Doctor>> = flow {
        val token = authPreferences.getToken() ?: throw Exception("No token")
        try {
            Log.d("TriageRepository", "Fetching doctors with token: Bearer $token")
            val response = triageApi.getDoctors("Bearer $token")
            Log.d("TriageRepository", "Get doctors response: $response")
            if (response.success) {
                emit(response.data ?: emptyList())
            } else {
                throw Exception(response.message ?: "Failed to fetch doctors")
            }
        } catch (e: Exception) {
            Log.e("TriageRepository", "Error fetching doctors: ${e.message}", e)
            throw Exception("Failed to fetch doctors: ${e.message}")
        }
    }.flowOn(Dispatchers.IO)

    fun getBookingByPatientId(patientId: String): Flow<Booking?> = flow {
        val token = authPreferences.getToken() ?: throw Exception("No token")
        try {
            Log.d("TriageRepository", "Fetching booking for patientId: $patientId with token: Bearer $token")
            val response = triageApi.getBookingByPatientId("Bearer $token", patientId)
            Log.d("TriageRepository", "Get booking response: $response")
            if (response.success) {
                emit(response.data?.firstOrNull())
            } else {
                throw Exception(response.message ?: "Failed to fetch booking")
            }
        } catch (e: Exception) {
            Log.e("TriageRepository", "Error fetching booking: ${e.message}", e)
            throw Exception("Failed to fetch booking: ${e.message}")
        }
    }.flowOn(Dispatchers.IO)

    fun submitTriageData(bookingId: String, triageData: TriageData): Flow<TriageData> = flow {
        val token = authPreferences.getToken() ?: throw Exception("No token")
        try {
            Log.d("TriageRepository", "Submitting triage data for bookingId: $bookingId with token: Bearer $token")
            val response = triageApi.submitTriageData("Bearer $token", bookingId, triageData)
            Log.d("TriageRepository", "Submit triage response: $response")
            if (response.success && response.data != null) {
                emit(response.data)
            } else {
                throw Exception(response.message ?: "Failed to submit triage data")
            }
        } catch (e: Exception) {
            Log.e("TriageRepository", "Error submitting triage: ${e.message}", e)
            throw Exception("Failed to submit triage data: ${e.message}")
        }
    }.flowOn(Dispatchers.IO)
}