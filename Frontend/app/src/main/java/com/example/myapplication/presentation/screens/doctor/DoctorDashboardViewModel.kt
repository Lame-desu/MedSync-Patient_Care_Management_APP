package com.example.myapplication.presentation.screens.doctor

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.myapplication.data.model.doctor.AppointmentResponse
import com.example.myapplication.data.repository.doctor.DoctorRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

class DoctorDashboardViewModel(
    private val repository: DoctorRepository
) : ViewModel() {

    sealed class DashboardState {
        object Loading : DashboardState()
        data class Success(val appointments: AppointmentResponse) : DashboardState()
        data class Error(val message: String) : DashboardState()
    }

    private val _dashboardState = MutableStateFlow<DashboardState>(DashboardState.Loading)
    val dashboardState: StateFlow<DashboardState> = _dashboardState.asStateFlow()

    init {
        fetchAppointments()
    }

    fun fetchAppointments() {
        viewModelScope.launch {
            try {
                _dashboardState.value = DashboardState.Loading
                repository.getDoctorAppointments(null, null).collect { response ->
                    Log.d("DoctorDashboardViewModel", "Fetched appointments: $response")
                    _dashboardState.value = DashboardState.Success(response)
                }
            } catch (e: Exception) {
                Log.e("DoctorDashboardViewModel", "Error fetching appointments: ${e.message ?: "Unknown error"}", e)
                _dashboardState.value = DashboardState.Error(
                    e.message ?: "Failed to load appointments"
                )
            }
        }
    }

    fun getCurrentDay(): String {
        val sdf = SimpleDateFormat("EEE", Locale.getDefault())
        return sdf.format(Date()).uppercase()
    }

    fun formatDate(dateString: String?): String {
        if (dateString == null) return "N/A"
        return try {
            val inputFormat = SimpleDateFormat("EEE MMM dd yyyy HH:mm:ss 'GMT'Z", Locale.getDefault())
            val outputFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
            val date = inputFormat.parse(dateString)
            date?.let { outputFormat.format(it) } ?: "N/A"
        } catch (e: Exception) {
            Log.e("DoctorDashboardViewModel", "Error formatting date: ${e.message}")
            "N/A"
        }
    }
}

class DoctorDashboardViewModelFactory(
    private val repository: DoctorRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(DoctorDashboardViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return DoctorDashboardViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}