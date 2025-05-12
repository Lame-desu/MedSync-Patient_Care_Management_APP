package com.example.myapplication.presentation.screens.admin

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.data.model.staff.Staff
import com.example.myapplication.data.model.staff.StaffRequest
import com.example.myapplication.data.repository.admin.StaffRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.util.regex.Pattern

class AdminAddViewModel(
    private val repository: StaffRepository
) : ViewModel() {

    private val _registrationState = MutableStateFlow<RegistrationState>(RegistrationState.Idle)
    val registrationState: StateFlow<RegistrationState> = _registrationState

    sealed class RegistrationState {
        object Idle : RegistrationState()
        object Loading : RegistrationState()
        data class Success(val staff: Staff) : RegistrationState()
        data class Error(val message: String) : RegistrationState()
    }

    fun registerStaff(
        fullName: String,
        email: String,
        password: String,
        confirmPassword: String,
        role: String,
        specialization: String,
        dateOfBirth: String,
        experienceYears: Int? = null,
        phone: String? = null,
        rating: Float? = null,
        hospital: String? = null
    ) {
        viewModelScope.launch {
            _registrationState.value = RegistrationState.Loading

            // Validation (only for required fields)
            if (fullName.isBlank()) {
                _registrationState.value = RegistrationState.Error("Full name is required")
                return@launch
            }
            if (!isValidEmail(email)) {
                _registrationState.value = RegistrationState.Error("Invalid email format")
                return@launch
            }
            if (password.length < 6) {
                _registrationState.value = RegistrationState.Error("Password must be at least 6 characters")
                return@launch
            }
            if (password != confirmPassword) {
                _registrationState.value = RegistrationState.Error("Passwords do not match")
                return@launch
            }
            if (role == "Select Role") {
                _registrationState.value = RegistrationState.Error("Please select a role")
                return@launch
            }
            if (specialization == "Select Specialization") {
                _registrationState.value = RegistrationState.Error("Please select a specialization")
                return@launch
            }
            if (dateOfBirth.isBlank()) {
                _registrationState.value = RegistrationState.Error("Date of birth is required")
                return@launch
            }

            try {
                repository.registerStaff(
                    StaffRequest(
                        name = fullName,
                        email = email,
                        password = password,
                        role = role.lowercase(), // Backend expects lowercase
                        specialization = specialization,
                        dateOfBirth = dateOfBirth,
                        experienceYears = experienceYears,
                        phone = phone,
                        rating = rating,
                        hospital = hospital
                    )
                ).collect { staff ->
                    _registrationState.value = RegistrationState.Success(staff)
                }
            } catch (e: Exception) {
                _registrationState.value = RegistrationState.Error(e.message ?: "Registration failed")
            }
        }
    }

    private fun isValidEmail(email: String): Boolean {
        val emailPattern = Pattern.compile(
            "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$"
        )
        return emailPattern.matcher(email).matches()
    }
}