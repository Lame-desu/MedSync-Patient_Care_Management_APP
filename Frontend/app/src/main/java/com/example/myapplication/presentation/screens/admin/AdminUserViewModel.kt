//package com.example.myapplication.presentation.screens.admin
//
//
//import androidx.lifecycle.ViewModel
//import androidx.lifecycle.viewModelScope
//import com.example.myapplication.data.model.doctor.Doctor
//import com.example.myapplication.data.repository.doctor.DoctorRepository
//import kotlinx.coroutines.flow.MutableStateFlow
//import kotlinx.coroutines.flow.StateFlow
//import kotlinx.coroutines.flow.asStateFlow
//import kotlinx.coroutines.launch
//
//class AdminViewModel(private val repository: DoctorRepository) : ViewModel() {
//
//    sealed class DoctorState {
//        object Idle : DoctorState()
//        object Loading : DoctorState()
//        data class Success(val doctors: List<Doctor>) : DoctorState()
//        data class Error(val message: String) : DoctorState()
//    }
//
//    private val _doctorState = MutableStateFlow<DoctorState>(DoctorState.Idle)
//    val doctorState: StateFlow<DoctorState> = _doctorState.asStateFlow()
//
//    init {
//        fetchDoctors()
//    }
//
//    private fun fetchDoctors() {
//        viewModelScope.launch {
//            _doctorState.value = DoctorState.Loading
//            try {
//                val doctors = repository.getDoctors()
//                _doctorState.value = DoctorState.Success(doctors)
//            } catch (e: Exception) {
//                _doctorState.value = DoctorState.Error(e.message ?: "Failed to fetch doctors")
//            }
//        }
//    }
//}

package com.example.myapplication.presentation.screens.admin

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.data.model.doctor.Doctor
import com.example.myapplication.data.repository.doctor.DoctorRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class AdminUserViewModel(private val repository: DoctorRepository) : ViewModel() {

    sealed class DoctorState {
        object Idle : DoctorState()
        object Loading : DoctorState()
        data class Success(val doctors: List<Doctor>) : DoctorState()
        data class Error(val message: String) : DoctorState()
    }

    private val _doctorState = MutableStateFlow<DoctorState>(DoctorState.Idle)
    val doctorState: StateFlow<DoctorState> = _doctorState.asStateFlow()

    init {
        fetchDoctors()
    }

    private fun fetchDoctors() {
        viewModelScope.launch {
            _doctorState.value = DoctorState.Loading
            try {
                repository.getDoctors().collect { doctors ->
                    _doctorState.value = DoctorState.Success(doctors)
                }
            } catch (e: Exception) {
                _doctorState.value = DoctorState.Error(e.message ?: "Failed to fetch doctors")
            }
        }
    }
}