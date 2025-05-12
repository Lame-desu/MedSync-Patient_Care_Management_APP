package com.example.myapplication.data.model.staff

import com.google.gson.annotations.SerializedName


//data class StaffRequest(
//    val name: String,
//    val email: String,
//    val password: String,
//    val role: String,
//    val specialization: String,
//    val dateOfBirth: String
//)
data class StaffRequest(
    val name: String,
    val email: String,
    val password: String,
    val role: String,
    @SerializedName("specialization")
    val specialization: String,
    val dateOfBirth: String,
    val experienceYears: Int? = null,
    val phone: String? = null,
    val rating: Float? = null,
    val hospital: String? = null)

data class StaffResponse(
    val success: Boolean,
    val data: Staff?,
    val message: String?
)

data class Staff(
    val id: String,
    val name: String,
    val email: String,
    val role: String,
    val specialization: String,
    val dateOfBirth: String
)