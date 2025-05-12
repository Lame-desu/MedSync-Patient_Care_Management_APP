//package com.example.myapplication.data.model.doctor
//
//
//data class Doctor(
//    val id: String, // Unique identifier for the doctor
//    val name: String,
//    val specialty: String,
//    val imageRes: Int, // Resource ID for the doctor's image
//    val rating: Float?, // Rating for "Top Doctors" card (nullable for cases where rating isn't available)
//    val hospital: String? = null, // Optional: Hospital affiliation for DoctorDetailScreen
//    val experienceYears: Int? = null // Optional: Years of experience for DoctorDetailScreen
//)
package com.example.myapplication.data.model.doctor

import com.google.gson.annotations.SerializedName

data class Doctor(
    @SerializedName("_id")
    val id: String,
    val name: String,
    @SerializedName("specialization") // Changed to match database field
    val specialty: String?, // Kept nullable for robustness
    @SerializedName("ageInYears")
    val ageInYears: Int? = null,
    val experienceYears: Int? = null,
    val phone: String? = null,
    val rating: Float? = null,
    val imageRes: String? = null,
    val hospital: String? = null,
    val role: String? = null
)
data class DoctorResponse(
    val success: Boolean,
    val data: List<Doctor>,
    val message: String?
)