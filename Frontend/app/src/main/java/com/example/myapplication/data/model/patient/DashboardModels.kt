package com.example.myapplication.data.model.patient


import com.google.gson.annotations.SerializedName

data class DashboardResponse(
    @SerializedName("success") val success: Boolean,
    @SerializedName("data") val data: DashboardData?,
    @SerializedName("message") val message: String?
)

data class DashboardData(
    @SerializedName("upcomingAppointments") val upcomingAppointments: List<Appointment>,
    @SerializedName("recentPrescriptions") val recentPrescriptions: List<Any>,
    @SerializedName("pendingBookings") val pendingBookings: List<PendingBooking>,
    @SerializedName("allergies") val allergies: List<Any>,
    @SerializedName("conditions") val conditions: List<Any>
)

data class Appointment(
    @SerializedName("_id") val id: String,
    @SerializedName("bookingId") val bookingId: String,
    @SerializedName("patientId") val patientId: String,
    @SerializedName("doctorId") val doctor: DoctorInfo,
    @SerializedName("date") val date: String,
    @SerializedName("time") val time: String,
    @SerializedName("status") val status: String,
    @SerializedName("createdAt") val createdAt: String,
    @SerializedName("updatedAt") val updatedAt: String
)

data class DoctorInfo(
    @SerializedName("_id") val id: String,
    @SerializedName("name") val name: String,
    @SerializedName("specialization") val specialization: String
)

data class PendingBooking(
    @SerializedName("_id") val id: String,
    @SerializedName("patientId") val patientId: String,
    @SerializedName("patientName") val patientName: String,
    @SerializedName("lookingFor") val lookingFor: String,
    @SerializedName("priority") val priority: String,
    @SerializedName("preferredDate") val preferredDate: String,
    @SerializedName("preferredTime") val preferredTime: String,
    @SerializedName("status") val status: String,
    @SerializedName("notes") val notes: String,
    @SerializedName("createdAt") val createdAt: String,
    @SerializedName("updatedAt") val updatedAt: String
)