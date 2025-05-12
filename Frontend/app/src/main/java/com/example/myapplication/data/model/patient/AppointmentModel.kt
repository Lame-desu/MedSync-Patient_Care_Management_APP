package com.example.myapplication.data.model.patient


data class AppointmentModel(
    val id: String,
    val date: String,
    val time: String,
    val doctor: Doctor
)
//
//data class Doctor(
//    val name: String,
//    val specialty: String,
//    val imageRes: Int
//)