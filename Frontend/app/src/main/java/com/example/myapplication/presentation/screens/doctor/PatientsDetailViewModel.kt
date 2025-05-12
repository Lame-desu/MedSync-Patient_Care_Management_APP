package com.example.myapplication.presentation.screens.doctor

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

class PatientDetailsViewModel(
    private val repository: DoctorRepository,
    private val patientId: String
) : ViewModel() {

    sealed class PatientDetailsState {
        object Loading : PatientDetailsState()
        data class Success(
            val patientDetails: PatientDetailsResponse,
            val medicalRecords: List<MedicalRecord>,
            val prescriptions: List<PrescriptionSummary>
        ) : PatientDetailsState()
        data class Error(val message: String) : PatientDetailsState()
    }

    private val _patientDetailsState = MutableStateFlow<PatientDetailsState>(PatientDetailsState.Loading)
    val patientDetailsState: StateFlow<PatientDetailsState> = _patientDetailsState.asStateFlow()

    init {
        fetchPatientDetails()
    }

    fun fetchPatientDetails() {
        viewModelScope.launch {
            try {
                _patientDetailsState.value = PatientDetailsState.Loading
                val patientResponse = repository.getPatientDetails(patientId).first()
                if (!patientResponse.success || patientResponse.data == null) {
                    throw Exception(patientResponse.message ?: "Failed to fetch patient details")
                }
                val medicalRecordsResponse = repository.getPatientMedicalRecords(patientId).first()
                val medicalRecords = if (medicalRecordsResponse.success) medicalRecordsResponse.data else emptyList()
                val prescriptionsResponse = repository.getPatientPrescriptions(patientId).first()
                val prescriptions = if (prescriptionsResponse.success) prescriptionsResponse.prescriptions ?: emptyList() else emptyList()
                _patientDetailsState.value = PatientDetailsState.Success(patientResponse, medicalRecords, prescriptions)
            } catch (e: Exception) {
                Log.e("PatientDetailsViewModel", "Error: ${e.message}", e)
                _patientDetailsState.value = PatientDetailsState.Error(e.message ?: "Failed to load patient details")
            }
        }
    }

    fun createMedicalRecord(diagnosis: String, treatment: String, notes: String) {
        viewModelScope.launch {
            try {
                val request = CreateMedicalRecordRequest(patientId, diagnosis, treatment, notes)
                val response = repository.createMedicalRecord(request).first()
                if (!response.success) {
                    throw Exception(response.message ?: "Failed to create medical record")
                }
                fetchPatientDetails()
            } catch (e: Exception) {
                Log.e("PatientDetailsViewModel", "Error creating medical record: ${e.message}", e)
                _patientDetailsState.value = PatientDetailsState.Error(e.message ?: "Failed to create medical record")
            }
        }
    }

    fun createPrescription(request: CreatePrescriptionRequest) {
        viewModelScope.launch {
            try {
                val response = repository.createPrescription(request).first()
                if (!response.success) {
                    throw Exception(response.message ?: "Failed to create prescription")
                }
                fetchPatientDetails()
            } catch (e: Exception) {
                Log.e("PatientDetailsViewModel", "Error creating prescription: ${e.message}", e)
                _patientDetailsState.value = PatientDetailsState.Error(e.message ?: "Failed to create prescription")
            }
        }
    }
}

class PatientDetailsViewModelFactory(
    private val repository: DoctorRepository,
    private val patientId: String
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(PatientDetailsViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return PatientDetailsViewModel(repository, patientId) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}