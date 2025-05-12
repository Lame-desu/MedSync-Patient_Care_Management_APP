package com.example.myapplication.presentation.screens.doctor

// In PrescriptionDetailsViewModel.kt

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.myapplication.data.model.doctor.*
import com.example.myapplication.data.repository.doctor.DoctorRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class PrescriptionDetailsViewModel(
    private val repository: DoctorRepository,
    private val prescriptionId: String
) : ViewModel() {

    sealed class PrescriptionState {
        object Loading : PrescriptionState()
        data class Success(val prescription: PrescriptionDetail) : PrescriptionState()
        data class Error(val message: String) : PrescriptionState()
    }

    private val _prescriptionState = MutableStateFlow<PrescriptionState>(PrescriptionState.Loading)
    val prescriptionState: StateFlow<PrescriptionState> = _prescriptionState.asStateFlow()

    init {
        fetchPrescriptionDetails()
    }

    fun fetchPrescriptionDetails() {
        viewModelScope.launch {
            try {
                _prescriptionState.value = PrescriptionState.Loading
                val response = repository.getPrescriptionDetails(prescriptionId).first()
                if (!response.success || response.prescription == null) {
                    throw Exception(response.message ?: "Failed to fetch prescription details")
                }
                _prescriptionState.value = PrescriptionState.Success(response.prescription)
            } catch (e: Exception) {
                Log.e("PrescriptionDetailsViewModel", "Error: ${e.message}", e)
                _prescriptionState.value = PrescriptionState.Error(e.message ?: "Failed to load prescription details")
            }
        }
    }

    fun updatePrescription(medications: List<MedicationEntry>) {
        viewModelScope.launch {
            try {
                val request = UpdatePrescriptionRequest(medications)
                val response = repository.updatePrescription(prescriptionId, request).first()
                if (!response.success) {
                    throw Exception(response.message ?: "Failed to update prescription")
                }
                fetchPrescriptionDetails()
            } catch (e: Exception) {
                Log.e("PrescriptionDetailsViewModel", "Error updating prescription: ${e.message}", e)
                _prescriptionState.value = PrescriptionState.Error(e.message ?: "Failed to update prescription")
            }
        }
    }

    fun deletePrescription() {
        viewModelScope.launch {
            try {
                val response = repository.deletePrescription(prescriptionId).first()
                if (!response.success) {
                    throw Exception(response.message ?: "Failed to delete prescription")
                }
            } catch (e: Exception) {
                Log.e("PrescriptionDetailsViewModel", "Error deleting prescription: ${e.message}", e)
                _prescriptionState.value = PrescriptionState.Error(e.message ?: "Failed to delete prescription")
            }
        }
    }
}

class PrescriptionDetailsViewModelFactory(
    private val repository: DoctorRepository,
    private val prescriptionId: String
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(PrescriptionDetailsViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return PrescriptionDetailsViewModel(repository, prescriptionId) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}