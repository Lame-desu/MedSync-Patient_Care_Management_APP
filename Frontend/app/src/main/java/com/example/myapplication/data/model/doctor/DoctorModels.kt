package com.example.myapplication.data.model.doctor

import com.google.gson.annotations.SerializedName

//data class AppointmentResponse(
//    @SerializedName("success") val success: Boolean,
//    @SerializedName("count") val count: Int,
//    @SerializedName("data") val data: List<Appointment>,
//    @SerializedName("message") val message: String?
//)
//
//data class Appointment(
//    @SerializedName("_id") val id: String,
//    @SerializedName("doctorId") val doctorId: String,
//    @SerializedName("patientId") val patient: Patient,
//    @SerializedName("date") val date: String,
//    @SerializedName("time") val time: String,
//    @SerializedName("status") val status: String
//)
//
//data class Patient(
//    @SerializedName("_id") val id: String,
//    @SerializedName("name") val name: String,
//    @SerializedName("email") val email: String,
//    @SerializedName("dateOfBirth") val dateOfBirth: String,
//    @SerializedName("gender") val gender: String,
//    @SerializedName("bloodGroup") val bloodGroup: String
//)

data class PatientDetailsResponse(
    @SerializedName("success") val success: Boolean,
    @SerializedName("data") val data: PatientDetailsData?,
    @SerializedName("message") val message: String?
)

data class PatientDetailsData(
    @SerializedName("patient") val patient: Patient,
    @SerializedName("medicalHistory") val medicalHistory: MedicalHistory?,
    @SerializedName("prescriptions") val prescriptions: List<Prescription>
)

data class MedicalHistory(
    @SerializedName("allergies") val allergies: List<String>?,
    @SerializedName("conditions") val conditions: List<String>?,
    @SerializedName("pastSurgeries") val pastSurgeries: List<String>?
)

data class Prescription(
    @SerializedName("_id") val id: String,
    @SerializedName("medication") val medication: String?,
    @SerializedName("dosage") val dosage: String?,
    @SerializedName("date") val date: String?
)

data class UpdateStatusRequest(
    @SerializedName("status") val status: String
)

