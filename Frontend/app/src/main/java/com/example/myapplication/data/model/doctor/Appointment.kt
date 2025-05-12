package com.example.myapplication.data.model.doctor

import com.google.gson.annotations.SerializedName

data class AppointmentResponse(
    val success: Boolean,
    val count: Int,
    val data: List<Appointment>
)

data class Appointment(
    @SerializedName("_id") val id: String,
    val bookingId: String,
    val patientId: Patient,
    val doctorId: String,
    val date: String, // e.g., "Thu May 15 2025 03:00:00 GMT+0300 (East Africa Time)"
    val time: String, // e.g., "02:10"
    val status: String, // e.g., "scheduled", "pending"
    val createdAt: String,
    val updatedAt: String,
    @SerializedName("__v") val version: Int
)

data class Patient(
    @SerializedName("_id") val id: String,
    val name: String,
    val email: String,
    val dateOfBirth: String,
    val gender: String? = null,
    val bloodGroup: String
)