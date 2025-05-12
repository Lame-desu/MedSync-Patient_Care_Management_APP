package com.example.myapplication.data.remote

import com.example.myapplication.data.model.triage.BookingDetailResponse
import com.example.myapplication.data.model.triage.BookingListResponse
import com.example.myapplication.data.model.triage.BookingRequest
import com.example.myapplication.data.model.triage.BookingResponse
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Path

interface BookingApi {
    @GET("api/patient/bookings")
    suspend fun getBookings(@Header("Authorization") authHeader: String): BookingListResponse

    @GET("api/patient/bookings/{id}")
    suspend fun getBookingById(
        @Header("Authorization") authHeader: String,
        @Path("id") id: String
    ): BookingDetailResponse

    @POST("api/patient/bookings")
    suspend fun createBooking(
        @Header("Authorization") authHeader: String,
        @Body request: BookingRequest
    ): BookingResponse
}