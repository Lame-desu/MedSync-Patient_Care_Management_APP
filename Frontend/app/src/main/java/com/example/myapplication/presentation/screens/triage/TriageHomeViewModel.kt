package com.example.myapplication.presentation.screens.triage

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.data.repository.triage.TriagePatient
import com.example.myapplication.data.repository.triage.TriageRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class TriageHomeViewModel(
    private val triageRepository: TriageRepository
) : ViewModel() {

    sealed class TriageState {
        object Loading : TriageState()
        data class Success(val patients: List<TriagePatient>) : TriageState()
        data class Error(val message: String) : TriageState()
    }

    private val _triageState = MutableStateFlow<TriageState>(TriageState.Loading)
    val triageState: StateFlow<TriageState> = _triageState.asStateFlow()

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    init {
        fetchPatients()
    }

    fun fetchPatients() {
        viewModelScope.launch {
            try {
                _triageState.value = TriageState.Loading
                triageRepository.getTriagePatients().collect { patients ->
                    patients.forEach { patient ->
                        Log.d("TriageHomeViewModel", "Patient: id=${patient.id}, name=${patient.name}")
                    }
                    Log.d("TriageHomeViewModel", "Fetched ${patients.size} patients")
                    _triageState.value = TriageState.Success(
                        patients.filter {
                            it.name.contains(_searchQuery.value, ignoreCase = true)
                        }
                    )
                }
            } catch (e: Exception) {
                Log.e("TriageHomeViewModel", "Error fetching patients: ${e.message}", e)
                _triageState.value = TriageState.Error(
                    e.message ?: "Failed to load patients. Please try again."
                )
            }
        }
    }

    fun updateSearchQuery(query: String) {
        _searchQuery.value = query
        fetchPatients() // Re-filter patients based on new query
    }
}