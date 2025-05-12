package com.example.myapplication.presentation.screens.patient

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.myapplication.data.model.patient.DashboardResponse
import com.example.myapplication.data.repository.patient.AppointmentRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class AppointmentViewModel(
    private val repository: AppointmentRepository
) : ViewModel() {

    sealed class DashboardState {
        object Loading : DashboardState()
        data class Success(val dashboardData: DashboardResponse) : DashboardState()
        data class Error(val message: String) : DashboardState()
    }

    private val _dashboardState = MutableStateFlow<DashboardState>(DashboardState.Loading)
    val dashboardState: StateFlow<DashboardState> = _dashboardState.asStateFlow()

    init {
        fetchDashboardData()
    }

    fun fetchDashboardData() {
        viewModelScope.launch {
            try {
                _dashboardState.value = DashboardState.Loading
                repository.getDashboardData().collect { response ->
                    Log.d("AppointmentViewModel", "Fetched dashboard data: $response")
                    _dashboardState.value = DashboardState.Success(response)
                }
            } catch (e: Exception) {
                Log.e("AppointmentViewModel", "Error fetching dashboard: ${e.message}", e)
                _dashboardState.value = DashboardState.Error(
                    e.message ?: "Failed to load dashboard data"
                )
            }
        }
    }

    fun cancelBooking(bookingId: String) {
        viewModelScope.launch {
            try {
                repository.cancelBooking(bookingId).collect { response ->
                    Log.d("AppointmentViewModel", "Cancel booking response: $response")
                    if (response.success) {
                        fetchDashboardData() // Refresh data after cancellation
                    } else {
                        _dashboardState.value = DashboardState.Error(
                            response.message ?: "Failed to cancel booking"
                        )
                    }
                }
            } catch (e: Exception) {
                Log.e("AppointmentViewModel", "Error cancelling booking: ${e.message}", e)
                _dashboardState.value = DashboardState.Error(
                    e.message ?: "Failed to cancel booking"
                )
            }
        }
    }
}

