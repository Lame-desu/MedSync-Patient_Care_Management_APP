package com.example.myapplication.presentation.auth

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.myapplication.data.model.AuthPreferences
import com.example.myapplication.data.model.RegisterResponse
import com.example.myapplication.data.repository.AuthRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class RegisterViewModel(
    private val authRepository: AuthRepository,
    private val authPreferences: AuthPreferences
) : ViewModel() {
    var currentStep by mutableStateOf(1)
    var name by mutableStateOf("")
    var dateOfBirth by mutableStateOf("")
    var gender by mutableStateOf("")
    var age by mutableStateOf("")
    var bloodGroup by mutableStateOf("")
    var emergencyContactName by mutableStateOf("")
    var emergencyContactNumber by mutableStateOf("")
    var email by mutableStateOf("")
    var password by mutableStateOf("")
    var confirmPassword by mutableStateOf("")
    private val role = "patient"

    private val _registerState = MutableStateFlow<RegisterState>(RegisterState.Idle)
    val registerState: StateFlow<RegisterState> = _registerState

    fun nextStep() {
        if (validateStep(currentStep)) {
            if (currentStep < 3) {
                currentStep++
            }
        } else {
            _registerState.value = RegisterState.Error("Please fill all required fields")
        }
    }

    fun previousStep() {
        if (currentStep > 1) currentStep--
    }

    fun register() {
        if (!validateStep(3)) {
            _registerState.value = RegisterState.Error("Please fill all required fields")
            return
        }
        if (password != confirmPassword) {
            _registerState.value = RegisterState.Error("Passwords do not match")
            return
        }
        val ageInt = age.toIntOrNull()
        if (ageInt == null || ageInt <= 0) {
            _registerState.value = RegisterState.Error("Please enter a valid age")
            return
        }
        _registerState.value = RegisterState.Loading
        viewModelScope.launch {
            val result = authRepository.register(
                name = name,
                dateOfBirth = dateOfBirth,
                gender = gender.lowercase(), // Transform to lowercase
                age = ageInt,
                bloodGroup = bloodGroup,
                emergencyContactName = emergencyContactName,
                emergencyContactNumber = emergencyContactNumber,
                email = email,
                password = password,
                role = role
            )
            _registerState.value = when {
                result.isSuccess -> {
                    val response = result.getOrNull()!!
                    authPreferences.saveAuthData(response.token, response.userId, response.role, name)
                    RegisterState.Success(response)
                }
                else -> {
                    val errorMessage = result.exceptionOrNull()?.message ?: "Registration failed"
                    RegisterState.Error(errorMessage)
                }
            }
        }
    }

    fun resetState() {
        _registerState.value = RegisterState.Idle
    }

    private fun validateStep(step: Int): Boolean {
        return when (step) {
            1 -> name.isNotBlank() && dateOfBirth.isNotBlank() && gender.isNotBlank() && age.isNotBlank()
            2 -> bloodGroup.isNotBlank() && emergencyContactName.isNotBlank() && emergencyContactNumber.isNotBlank()
            3 -> email.isNotBlank() && password.isNotBlank() && confirmPassword.isNotBlank() && isValidEmail(email)
            else -> false
        }
    }

    private fun isValidEmail(email: String): Boolean {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }
}

sealed class RegisterState {
    object Idle : RegisterState()
    object Loading : RegisterState()
    data class Success(val response: RegisterResponse) : RegisterState()
    data class Error(val message: String) : RegisterState()
}

class RegisterViewModelFactory(
    private val repository: AuthRepository,
    private val authPreferences: AuthPreferences
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(RegisterViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return RegisterViewModel(repository, authPreferences) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}