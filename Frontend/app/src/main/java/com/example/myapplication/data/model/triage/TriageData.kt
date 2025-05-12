package com.example.myapplication.data.model.triage


import com.google.gson.annotations.SerializedName

data class TriageData(
//    @SerializedName("patientId") val patientId: String,
//    @SerializedName("temperature") val temperature: Float,
//    @SerializedName("heartRate") val heartRate: Int,
//    @SerializedName("bloodPressure") val bloodPressure: String,
//    @SerializedName("appointmentDate") val appointmentDate: String,
//    @SerializedName("assignedDoctorId") val assignedDoctorId: String
//
    @SerializedName("bookingId") val bookingId: String,
    @SerializedName("patientId") val patientId: String,
    @SerializedName("doctorId") val doctorId: String,
    @SerializedName("date") val date: String,
    @SerializedName("time") val time: String,
    @SerializedName("status") val status: String,
    @SerializedName("reason") val reason: String?,
    @SerializedName("priority") val priority: String,
    @SerializedName("triageNotes") val triageNotes: String
)