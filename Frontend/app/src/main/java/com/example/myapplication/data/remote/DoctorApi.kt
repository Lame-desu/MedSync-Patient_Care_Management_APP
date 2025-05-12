package com.example.myapplication.data.remote

import com.example.myapplication.data.model.doctor.*
import com.example.myapplication.data.model.patient.PatientResponse
import retrofit2.http.*

interface DoctorApi {
    @GET("api/admin/staff/doctor")
    suspend fun getDoctors(@Header("Authorization") authHeader: String): DoctorResponse

    @GET("/api/doctor/appointments")
    suspend fun getDoctorAppointments(
        @Header("Authorization") token: String,
        @Query("date") date: String? = null,
        @Query("status") status: String? = null
    ): AppointmentResponse

    @GET("/api/doctor/patients")
    suspend fun getDoctorPatients(
        @Header("Authorization") token: String
    ): PatientResponse

    @GET("/api/doctor/patients/{id}")
    suspend fun getPatientDetails(
        @Header("Authorization") token: String,
        @Path("id") patientId: String
    ): PatientDetailsResponse

    @GET("/api/doctor/patients/{patientId}/medical-records")
    suspend fun getPatientMedicalRecords(
        @Header("Authorization") token: String,
        @Path("patientId") patientId: String
    ): MedicalRecordsResponse

    @GET("/api/doctor/medical-records/{recordId}")
    suspend fun getMedicalRecordDetails(
        @Header("Authorization") token: String,
        @Path("recordId") recordId: String
    ): MedicalRecordResponse

    @POST("/api/doctor/medical-records")
    suspend fun createMedicalRecord(
        @Header("Authorization") token: String,
        @Body request: CreateMedicalRecordRequest
    ): MedicalRecordResponse

    @PUT("/api/doctor/medical-records/{recordId}")
    suspend fun updateMedicalRecord(
        @Header("Authorization") token: String,
        @Path("recordId") recordId: String,
        @Body request: UpdateMedicalRecordRequest
    ): MedicalRecordResponse

    @DELETE("/api/doctor/medical-records/{recordId}")
    suspend fun deleteMedicalRecord(
        @Header("Authorization") token: String,
        @Path("recordId") recordId: String
    ): DeleteMedicalRecordResponse
    // In DoctorApi.kt
    @GET("/api/doctor/patients/{patientId}/prescriptions")
    suspend fun getPatientPrescriptions(
        @Header("Authorization") token: String,
        @Path("patientId") patientId: String
    ): PrescriptionListResponse

    @GET("/api/doctor/prescriptions/{prescriptionId}")
    suspend fun getPrescriptionDetails(
        @Header("Authorization") token: String,
        @Path("prescriptionId") prescriptionId: String
    ): PrescriptionDetailResponse

    @POST("/api/doctor/prescriptions")
    suspend fun createPrescription(
        @Header("Authorization") token: String,
        @Body request: CreatePrescriptionRequest
    ): PrescriptionDetailResponse

    @PUT("/api/doctor/prescriptions/{prescriptionId}")
    suspend fun updatePrescription(
        @Header("Authorization") token: String,
        @Path("prescriptionId") prescriptionId: String,
        @Body request: UpdatePrescriptionRequest
    ): PrescriptionDetailResponse

    @DELETE("/api/doctor/prescriptions/{prescriptionId}")
    suspend fun deletePrescription(
        @Header("Authorization") token: String,
        @Path("prescriptionId") prescriptionId: String
    ): DeletePrescriptionResponse
}
