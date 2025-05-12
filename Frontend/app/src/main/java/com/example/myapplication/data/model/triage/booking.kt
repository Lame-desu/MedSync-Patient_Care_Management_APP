package com.example.myapplication.data.model.triage


import com.google.gson.annotations.SerializedName

//data class Booking(
//    @SerializedName("_id") val id: String,
//    val patientId: String,
//    val patientName: String,
//    val lookingFor: String,
//    val preferredDate: String,
//    val preferredTime: String,
//    val status: String,
//    val createdAt: String,
//    val updatedAt: String,
//    @SerializedName("__v") val version: Int
//)
data class Booking(
    @SerializedName("_id") val id: String,
    @SerializedName("patientId") val patientId: PatientId,
    @SerializedName("patientName") val patientName: String,
    @SerializedName("lookingFor") val lookingFor: String,
    @SerializedName("priority") val priority: String,
    @SerializedName("preferredDate") val preferredDate: String,
    @SerializedName("preferredTime") val preferredTime: String,
    @SerializedName("status") val status: String,
    @SerializedName("symptoms") val symptoms: String?,
    @SerializedName("notes") val notes: String,
    @SerializedName("createdAt") val createdAt: String,
    @SerializedName("updatedAt") val updatedAt: String,
    @SerializedName("__v") val version: Int
)

data class PatientId(
    @SerializedName("_id") val id: String,
    @SerializedName("name") val name: String,
    @SerializedName("email") val email: String
)

data class BookingRequest(
    @SerializedName("patientId") val patientId: String,
    @SerializedName("patientName") val patientName: String,
    @SerializedName("lookingFor") val lookingFor: String,
    @SerializedName("preferredDate") val preferredDate: String,
    @SerializedName("preferredTime") val preferredTime: String,
    @SerializedName("priority") val priority: String
)

data class BookingResponse(
    val success: Boolean,
    val data: Booking?,
    val message: String?
)

data class BookingListResponse(
    val success: Boolean,
    val data: List<Booking>,
    val message: String?
)

data class BookingDetailResponse(
    val success: Boolean,
    val data: Booking,
    val message: String?
)