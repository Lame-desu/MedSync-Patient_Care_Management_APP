package com.example.myapplication.data.model.patient

import com.example.myapplication.data.model.doctor.DoctorInfo
import com.example.myapplication.data.model.doctor.MedicationEntry
import com.google.gson.annotations.SerializedName
import com.example.myapplication.data.model.doctor.PatientInfo as DoctorPatientInfo

data class PrescriptionResponse(
    val success: Boolean,
    val data: List<Prescription>?,
    val message: String? = null
)

data class MedicalRecordResponse(
    val success: Boolean,
    val data: List<MedicalRecord>?,
    val message: String? = null
)

data class MedicalRecord(
    val id: String?,
    val patientId: PatientInfo?,
    val doctorId: String?,
    val diagnosis: String?,
    val treatment: String?,
    val notes: String?,
    val lastUpdated: String?,
    val doctorInfo: DoctorInfo?,
    val patientInfo: DoctorPatientInfo?
)

data class Prescription(
    val id: String?,
    val patientId: String?,
    val doctorId: String?,
    val medicationEntries: List<MedicationEntry>?,
    val createdAt: String?
)

data class PatientInfo(
    @SerializedName("_id") val id: String,
    val name: String,
    val dateOfBirth: String,
    val gender: String?
)