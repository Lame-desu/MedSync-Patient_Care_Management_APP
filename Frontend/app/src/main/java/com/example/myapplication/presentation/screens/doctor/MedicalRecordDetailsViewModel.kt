package com.example.myapplication.presentation.screens.doctor


import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.myapplication.data.model.doctor.MedicalRecord
import com.example.myapplication.data.model.doctor.UpdateMedicalRecordRequest
import com.example.myapplication.data.repository.doctor.DoctorRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class MedicalRecordDetailsViewModel(
    private val repository: DoctorRepository
) : ViewModel() {

    sealed class RecordState {
        object Loading : RecordState()
        data class Success(val record: MedicalRecord) : RecordState()
        data class Error(val message: String) : RecordState()
    }

    private val _recordState = MutableStateFlow<RecordState>(RecordState.Loading)
    val recordState: StateFlow<RecordState> = _recordState.asStateFlow()

    fun fetchMedicalRecordDetails(recordId: String) {
        viewModelScope.launch {
            try {
                _recordState.value = RecordState.Loading
                val recordResponse = repository.getMedicalRecordDetails(recordId).first()
                _recordState.value = RecordState.Success(recordResponse.data)
            } catch (e: Exception) {
                _recordState.value = RecordState.Error(
                    e.message ?: "Failed to load medical record details"
                )
            }
        }
    }

    fun updateMedicalRecord(recordId: String, diagnosis: String, treatment: String, notes: String) {
        viewModelScope.launch {
            try {
                val request = UpdateMedicalRecordRequest(
                    diagnosis = diagnosis,
                    treatment = treatment,
                    notes = notes
                )
                repository.updateMedicalRecord(recordId, request).first()
                fetchMedicalRecordDetails(recordId) // Refresh data
            } catch (e: Exception) {
                _recordState.value = RecordState.Error(
                    e.message ?: "Failed to update medical record"
                )
            }
        }
    }

    fun deleteMedicalRecord(recordId: String) {
        viewModelScope.launch {
            try {
                repository.deleteMedicalRecord(recordId).first()
                _recordState.value = RecordState.Success(
                    MedicalRecord(
                        recordId = null,
                        patientInfo = com.example.myapplication.data.model.doctor.Patient("", "","", "", null, ""),
                        doctorInfo = com.example.myapplication.data.model.doctor.Doctor("", "", specialty = null),
                        diagnosis = null,
                        treatment = null,
                        notes = null,
                        lastUpdated = null
                    )
                )
            } catch (e: Exception) {
                _recordState.value = RecordState.Error(
                    e.message ?: "Failed to delete medical record"
                )
            }
        }
    }
}

class MedicalRecordDetailsViewModelFactory(
    private val repository: DoctorRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MedicalRecordDetailsViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return MedicalRecordDetailsViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}