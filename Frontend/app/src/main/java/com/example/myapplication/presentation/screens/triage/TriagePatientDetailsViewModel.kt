package com.example.myapplication.presentation.screens.triage

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.data.model.triage.Booking
import com.example.myapplication.data.model.doctor.Doctor
import com.example.myapplication.data.model.triage.TriageData
import com.example.myapplication.data.repository.triage.TriageRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class TriagePatientDetailsViewModel(
    private val triageRepository: TriageRepository
) : ViewModel() {

    sealed class DetailsState {
        object Loading : DetailsState()
        data class Success(
            val booking: Booking?,
            val doctors: List<Doctor>
        ) : DetailsState()
        data class Error(val message: String) : DetailsState()
    }

    sealed class SubmitState {
        object Idle : SubmitState()
        object Loading : SubmitState()
        object Success : SubmitState()
        data class Error(val message: String) : SubmitState()
    }

    private val _detailsState = MutableStateFlow<DetailsState>(DetailsState.Loading)
    val detailsState: StateFlow<DetailsState> = _detailsState.asStateFlow()

    private val _submitState = MutableStateFlow<SubmitState>(SubmitState.Idle)
    val submitState: StateFlow<SubmitState> = _submitState.asStateFlow()

    fun fetchPatientDetails(patientId: String) {
        viewModelScope.launch {
            try {
                _detailsState.value = DetailsState.Loading
                val bookingFlow = triageRepository.getBookingByPatientId(patientId)
                val doctorsFlow = triageRepository.getDoctors()
                kotlinx.coroutines.flow.combine(bookingFlow, doctorsFlow) { booking, doctors ->
                    DetailsState.Success(booking, doctors)
                }.collect { state ->
                    _detailsState.value = state
                }
            } catch (e: Exception) {
                Log.e("TriagePatientDetailsViewModel", "Error fetching details: ${e.message}", e)
                _detailsState.value = DetailsState.Error(e.message ?: "Failed to fetch details")
            }
        }
    }

    fun submitTriageData(bookingId: String, triageData: TriageData) {
        viewModelScope.launch {
            if (_submitState.value is SubmitState.Loading) return@launch
            _submitState.value = SubmitState.Loading
            try {
                triageRepository.submitTriageData(bookingId, triageData).collect {
                    _submitState.value = SubmitState.Success
                }
            } catch (e: Exception) {
                Log.e("TriagePatientDetailsViewModel", "Error submitting triage: ${e.message}", e)
                _submitState.value = SubmitState.Error(e.message ?: "Failed to submit triage data")
            }
        }
    }
}