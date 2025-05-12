package com.example.myapplication.presentation.screens.patient

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.myapplication.data.model.patient.Doctor
import com.example.myapplication.data.repository.patient.PatientRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class DoctorsViewModel(
    private val repository: PatientRepository
) : ViewModel() {

    sealed class DoctorState {
        object Loading : DoctorState()
        data class Success(val doctors: List<Doctor>) : DoctorState()
        data class Error(val message: String) : DoctorState()
    }

    private val _doctorState = MutableStateFlow<DoctorState>(DoctorState.Loading)
    val doctorState: StateFlow<DoctorState> = _doctorState.asStateFlow()

    private val _selectedDoctor = MutableStateFlow<Doctor?>(null)
    val selectedDoctor: StateFlow<Doctor?> = _selectedDoctor.asStateFlow()

    init {
        fetchDoctors()
    }

    fun fetchDoctors() {
        viewModelScope.launch {
            try {
                _doctorState.value = DoctorState.Loading
                val response = repository.getPatientDoctors().first()
                if (response.success && response.data != null) {
                    _doctorState.value = DoctorState.Success(response.data)
                } else {
                    throw Exception(response.message ?: "Failed to fetch doctors")
                }
            } catch (e: Exception) {
                Log.e("DoctorsViewModel", "Error: ${e.message}", e)
                _doctorState.value = DoctorState.Error(e.message ?: "Failed to load doctors")
            }
        }
    }

    fun selectDoctor(doctor: Doctor?) {
        _selectedDoctor.value = doctor
    }
}

class DoctorsViewModelFactory(
    private val repository: PatientRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(DoctorsViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return DoctorsViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}