package com.example.myapplication.data.repository.patient


import com.example.myapplication.data.model.AuthPreferences
import com.example.myapplication.data.model.patient.DoctorResponse
import com.example.myapplication.data.model.patient.MedicalRecordResponse
import com.example.myapplication.data.model.patient.PrescriptionResponse
import com.example.myapplication.data.remote.PatientApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class PatientRepository(
    private val authPreferences: AuthPreferences,
    private val patientApi: PatientApi
) {
    suspend fun getPatientPrescriptions(): Flow<PrescriptionResponse> = flow {
        val token = authPreferences.getToken() ?: throw Exception("No auth token")
        val response = patientApi.getPatientPrescriptions("Bearer $token")
        emit(response)
    }

    suspend fun getPatientMedicalRecords(): Flow<MedicalRecordResponse> = flow {
        val token = authPreferences.getToken() ?: throw Exception("No auth token")
        val response = patientApi.getPatientMedicalRecords("Bearer $token")
        emit(response)
    }
    suspend fun getPatientDoctors(): Flow<DoctorResponse> = flow {
        val token = authPreferences.getToken() ?: throw Exception("No auth token")
        val response = patientApi.getPatientDoctors("Bearer $token")
        emit(response)
    }
}