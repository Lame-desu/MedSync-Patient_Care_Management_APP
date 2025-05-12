package com.example.myapplication.data.repository.triage


import com.example.myapplication.data.model.AuthPreferences
import com.example.myapplication.data.model.triage.Booking
import com.example.myapplication.data.model.triage.BookingRequest
import com.example.myapplication.data.remote.BookingApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

class BookingRepository(
    private val authPreferences: AuthPreferences,
    private val bookingApi: BookingApi
) {
    fun getBookings(): Flow<List<Booking>> = flow {
        val token = authPreferences.getToken() ?: throw Exception("No token")
        val response = bookingApi.getBookings("Bearer $token")
        if (response.success) {
            emit(response.data)
        } else {
            throw Exception(response.message ?: "Failed to fetch bookings")
        }
    }.flowOn(Dispatchers.IO)

    fun getBookingById(id: String): Flow<Booking> = flow {
        val token = authPreferences.getToken() ?: throw Exception("No token")
        val response = bookingApi.getBookingById("Bearer $token", id)
        if (response.success) {
            emit(response.data)
        } else {
            throw Exception(response.message ?: "Failed to fetch booking")
        }
    }.flowOn(Dispatchers.IO)

    fun createBooking(request: BookingRequest): Flow<Booking> = flow {
        val token = authPreferences.getToken() ?: throw Exception("No token")
        val response = bookingApi.createBooking("Bearer $token", request)
        if (response.success && response.data != null) {
            emit(response.data)
        } else {
            throw Exception(response.message ?: "Failed to create booking")
        }
    }.flowOn(Dispatchers.IO)
}