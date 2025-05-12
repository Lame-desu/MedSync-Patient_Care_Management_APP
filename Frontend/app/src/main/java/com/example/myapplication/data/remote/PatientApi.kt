package com.example.myapplication.data.remote

import com.example.myapplication.data.model.patient.DashboardResponse
import com.example.myapplication.data.model.patient.DoctorResponse
import com.example.myapplication.data.model.patient.MedicalRecordResponse
import com.example.myapplication.data.model.patient.PrescriptionResponse
import retrofit2.http.*

interface PatientApi {
    @GET("/api/patient/dashboard")
    suspend fun getDashboard(@Header("Authorization") token: String): DashboardResponse

    @PUT("/api/patient/bookings/{id}/cancel")
    suspend fun cancelBooking(
        @Header("Authorization") token: String,
        @Path("id") bookingId: String
    ): DashboardResponse
    @GET("api/patient/prescriptions")
    suspend fun getPatientPrescriptions(
        @Header("Authorization") authToken: String
    ): PrescriptionResponse

    @GET("api/patient/medical-records")
    suspend fun getPatientMedicalRecords(
        @Header("Authorization") authToken: String
    ): MedicalRecordResponse



        @GET("api/patient/doctors")
        suspend fun getPatientDoctors(
            @Header("Authorization") authToken: String
        ): DoctorResponse
    }
