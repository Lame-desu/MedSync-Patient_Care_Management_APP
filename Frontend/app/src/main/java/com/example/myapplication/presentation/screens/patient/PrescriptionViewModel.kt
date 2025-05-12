package com.example.myapplication.presentation.screens.patient

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.myapplication.data.model.patient.Prescription
import com.example.myapplication.data.repository.patient.PatientRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class PrescriptionViewModel(
    private val repository: PatientRepository
) : ViewModel() {

    sealed class PrescriptionState {
        object Loading : PrescriptionState()
        data class Success(val prescriptions: List<Prescription>) : PrescriptionState()
        data class Error(val message: String) : PrescriptionState()
    }

    private val _prescriptionState = MutableStateFlow<PrescriptionState>(PrescriptionState.Loading)
    val prescriptionState: StateFlow<PrescriptionState> = _prescriptionState.asStateFlow()

    init {
        fetchPrescriptions()
    }

    fun fetchPrescriptions() {
        viewModelScope.launch {
            try {
                _prescriptionState.value = PrescriptionState.Loading
                val response = repository.getPatientPrescriptions().first()
                if (response.success && response.data != null) {
                    _prescriptionState.value = PrescriptionState.Success(response.data)
                } else {
                    throw Exception(response.message ?: "Failed to fetch prescriptions")
                }
            } catch (e: Exception) {
                Log.e("PrescriptionViewModel", "Error: ${e.message}", e)
                _prescriptionState.value = PrescriptionState.Error(e.message ?: "Failed to load prescriptions")
            }
        }
    }
}

class PrescriptionViewModelFactory(
    private val repository: PatientRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(PrescriptionViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return PrescriptionViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}