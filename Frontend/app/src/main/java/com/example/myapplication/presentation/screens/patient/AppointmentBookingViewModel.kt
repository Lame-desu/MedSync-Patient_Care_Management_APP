package com.example.myapplication.presentation.screens.patient

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.data.model.triage.BookingRequest
import com.example.myapplication.data.repository.triage.BookingRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class AppointmentBookingViewModel(
    private val bookingRepository: BookingRepository
) : ViewModel() {

    sealed class BookingState {
        object Idle : BookingState()
        object Loading : BookingState()
        object Success : BookingState()
        data class Error(val message: String) : BookingState()
    }

    private val _bookingState = MutableStateFlow<BookingState>(BookingState.Idle)
    val bookingState: StateFlow<BookingState> = _bookingState.asStateFlow()

    fun createBooking(patientId: String, patientName: String, lookingFor: String, preferredDate: String, preferredTime: String, priority: String) {
        viewModelScope.launch {
            if (_bookingState.value is BookingState.Loading) return@launch
            _bookingState.value = BookingState.Loading
            try {
                val request = BookingRequest(
                    patientId = patientId,
                    patientName = patientName,
                    lookingFor = lookingFor.lowercase(),
                    preferredDate = preferredDate,
                    preferredTime = preferredTime,
                    priority = priority
                )
                bookingRepository.createBooking(request).collect { booking ->
                    _bookingState.value = BookingState.Success
                }
            } catch (e: Exception) {
                Log.e("AppointmentBookingViewModel", "Booking error: ${e.message}", e)
                _bookingState.value = BookingState.Error(e.message ?: "Failed to create booking")
            }
        }
    }
}