package com.example.myapplication.presentation.screens.doctor

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.myapplication.data.model.patient.PatientResponse
import com.example.myapplication.data.repository.doctor.DoctorRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class PatientListViewModel(
    private val repository: DoctorRepository
) : ViewModel() {

    sealed class PatientState {
        object Loading : PatientState()
        data class Success(val patients: PatientResponse) : PatientState()
        data class Error(val message: String) : PatientState()
    }

    private val _patientState = MutableStateFlow<PatientState>(PatientState.Loading)
    val patientState: StateFlow<PatientState> = _patientState.asStateFlow()

    init {
        fetchPatients()
    }

    fun fetchPatients() {
        viewModelScope.launch {
            try {
                _patientState.value = PatientState.Loading
                repository.getDoctorPatients().collect { response ->
                    Log.d("PatientListViewModel", "Fetched patients: $response")
                    _patientState.value = PatientState.Success(response)
                }
            } catch (e: Exception) {
                Log.e("PatientListViewModel", "Error fetching patients: ${e.message ?: "Unknown error"}", e)
                _patientState.value = PatientState.Error(
                    e.message ?: "Failed to load patients"
                )
            }
        }
    }
}

class PatientListViewModelFactory(
    private val repository: DoctorRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(PatientListViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return PatientListViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}