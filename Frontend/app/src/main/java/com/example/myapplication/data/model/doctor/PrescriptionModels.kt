// PrescriptionModels.kt
package com.example.myapplication.data.model.doctor

import com.google.gson.annotations.SerializedName

data class PrescriptionListResponse(
    @SerializedName("success") val success: Boolean,
    @SerializedName("data") val prescriptions: List<PrescriptionSummary>?,
    @SerializedName("message") val message: String?
)

data class PrescriptionDetailResponse(
    @SerializedName("success") val success: Boolean,
    @SerializedName("data") val prescription: PrescriptionDetail?,
    @SerializedName("message") val message: String?
)

data class DeletePrescriptionResponse(
    @SerializedName("success") val success: Boolean,
    @SerializedName("message") val message: String?,
    @SerializedName("data") val data: Map<String, String>?
)

data class PrescriptionSummary(
    @SerializedName("_id") val id: String?,
    @SerializedName("appointmentId") val appointmentId: String?,
    @SerializedName("patientId") val patientId: String?,
    @SerializedName("doctorId") val doctorId: String?,
    @SerializedName("medications") val medications: List<MedicationEntry>?,
    @SerializedName("createdAt") val createdAt: String?
)

data class PrescriptionDetail(
    @SerializedName("_id") val id: String?,
    @SerializedName("appointmentId") val appointmentId: String?,
    @SerializedName("patientId") val patientId: String?,
    @SerializedName("doctorId") val doctorId: String?,
    @SerializedName("medications") val medications: List<MedicationEntry>?,
    @SerializedName("createdAt") val createdAt: String?,
    @SerializedName("doctorInfo") val doctorInfo: DoctorInfo,
    @SerializedName("patientInfo") val patientInfo: PatientInfo,
    @SerializedName("appointmentInfo") val appointmentInfo: AppointmentInfo
)

data class MedicationEntry(
    @SerializedName("name") val name: String?,
    @SerializedName("dosage") val dosage: String?,
    @SerializedName("frequency") val frequency: String?,
    @SerializedName("description") val description: String?,
    @SerializedName("price") val price: Double?
)

data class DoctorInfo(
    @SerializedName("name") val name: String,
    @SerializedName("specialization") val specialization: String?
)

data class PatientInfo(
    @SerializedName("name") val name: String,
    @SerializedName("dateOfBirth") val dateOfBirth: String?,
    @SerializedName("gender") val gender: String?
)

data class AppointmentInfo(
    @SerializedName("date") val date: String?,
    @SerializedName("status") val status: String?
)

data class CreatePrescriptionRequest(
    @SerializedName("patientId") val patientId: String,
    @SerializedName("appointmentId") val appointmentId: String?,
    @SerializedName("medications") val medications: List<MedicationEntry>
)

data class UpdatePrescriptionRequest(
    @SerializedName("medications") val medications: List<MedicationEntry>
)