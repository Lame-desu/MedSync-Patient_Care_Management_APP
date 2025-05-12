package com.example.myapplication.data.remote

import com.example.myapplication.data.model.triage.Booking
import com.example.myapplication.data.model.doctor.Doctor
import com.example.myapplication.data.model.patient.Patient
import com.example.myapplication.data.model.triage.TriageData
import retrofit2.http.*

interface TriageApi {
    @GET("/api/triage/bookings")
    suspend fun getAllBookings(@Header("Authorization") token: String): ResponseWrapper<List<Booking>>

    @GET("/api/triage/doctors")
    suspend fun getDoctors(@Header("Authorization") token: String): ResponseWrapper<List<Doctor>>

    @GET("/api/triage/bookings")
    suspend fun getBookingByPatientId(
        @Header("Authorization") token: String,
        @Query("patientId") patientId: String
    ): ResponseWrapper<List<Booking>>

    @POST("/api/triage/process/{bookingId}")
    suspend fun submitTriageData(
        @Header("Authorization") token: String,
        @Path("bookingId") bookingId: String,
        @Body triageData: TriageData
    ): ResponseWrapper<TriageData>
}

data class ResponseWrapper<T>(
    val success: Boolean,
    val data: T?,
    val message: String?
)