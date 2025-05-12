package com.example.myapplication.data.model.doctor

import com.google.gson.annotations.SerializedName

data class MedicalRecordsResponse(
    val success: Boolean,
    val data: List<MedicalRecord>,
    val message: String? = null
)

data class MedicalRecordResponse(
    val success: Boolean,
    val data: MedicalRecord,
    val message: String? = null
)

data class MedicalRecord(
    @SerializedName("_id") val recordId: String? = null,
    @SerializedName("patientId") val patientInfo: Patient,
    @SerializedName("doctorId") val doctorInfo: Doctor,
    @SerializedName("diagnosis") val diagnosis: String? = null,
    @SerializedName("treatment") val treatment: String? = null,
    @SerializedName("notes") val notes: String? = null,
    @SerializedName("lastUpdated") val lastUpdated: String? = null
)

data class CreateMedicalRecordRequest(
    val patientId: String,
    val diagnosis: String?,
    val treatment: String?,
    val notes: String?
)

data class UpdateMedicalRecordRequest(
    val diagnosis: String?,
    val treatment: String?,
    val notes: String?
)

data class DeleteMedicalRecordResponse(
    val success: Boolean,
    val message: String,
    val data: DeleteMedicalRecordData
)

data class DeleteMedicalRecordData(
    val patientId: String
)