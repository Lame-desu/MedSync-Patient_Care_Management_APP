package com.example.myapplication.presentation.screens.patient

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.myapplication.data.model.patient.MedicalRecord
import com.example.myapplication.data.repository.patient.PatientRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class MedicalHistoryViewModel(
    private val repository: PatientRepository
) : ViewModel() {

    sealed class MedicalRecordState {
        object Loading : MedicalRecordState()
        data class Success(val records: List<MedicalRecord>) : MedicalRecordState()
        data class Error(val message: String) : MedicalRecordState()
    }

    private val _medicalRecordState = MutableStateFlow<MedicalRecordState>(MedicalRecordState.Loading)
    val medicalRecordState: StateFlow<MedicalRecordState> = _medicalRecordState.asStateFlow()

    init {
        fetchMedicalRecords()
    }

    fun fetchMedicalRecords() {
        viewModelScope.launch {
            try {
                _medicalRecordState.value = MedicalRecordState.Loading
                val response = repository.getPatientMedicalRecords().first()
                if (response.success && response.data != null) {
                    _medicalRecordState.value = MedicalRecordState.Success(response.data)
                } else {
                    throw Exception(response.message ?: "Failed to fetch medical records")
                }
            } catch (e: Exception) {
                Log.e("MedicalHistoryViewModel", "Error: ${e.message}", e)
                _medicalRecordState.value = MedicalRecordState.Error(e.message ?: "Failed to load medical records")
            }
        }
    }
}

class MedicalHistoryViewModelFactory(
    private val repository: PatientRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MedicalHistoryViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return MedicalHistoryViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}