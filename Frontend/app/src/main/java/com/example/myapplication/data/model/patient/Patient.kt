//package com.example.myapplication.data.model.patient
//
//import com.google.gson.annotations.SerializedName
//
//data class Patient(
//    @SerializedName("_id") val id: String,
//    @SerializedName("name") val name: String,
//    @SerializedName("email") val email: String
//)

package com.example.myapplication.data.model.patient

import com.google.gson.annotations.SerializedName



data class PatientResponse(
    val success: Boolean,
    val count: Int,
    val data: List<Patient>
)

data class Patient(
    @SerializedName("_id") val id: String,
    val name: String,
    val email: String,
    val dateOfBirth: String,
    val gender: String?,
    val bloodGroup: String
)

data class PatientDetailsResponse(
    val success: Boolean,
    val data: PatientDetailsData
)

data class PatientDetailsData(
    val patient: Patient,
    val medicalHistory: MedicalHistory?,
    val prescriptions: List<Prescription>
)

data class MedicalHistory(
    @SerializedName("_id") val id: String,
    val patientId: String,
    val allergies: List<String>?,
    val conditions: List<String>?,
    val pastSurgeries: List<String>?
)

//data class Prescription(
//    @SerializedName("_id") val id: String,
//    val patientId: String,
//    val medication: String?,
//    val dosage: String?,
//    val date: String?
//)
data class DoctorResponse(
    val success: Boolean,
    val count: Int?,
    val data: List<Doctor>?,
    val message: String? = null
)

data class Doctor(
    val id: String?,
    val name: String?,
    val specialization: String?,
    val rating: Double?,
    val experienceYears: Int?
)