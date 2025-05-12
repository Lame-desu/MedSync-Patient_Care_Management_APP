package com.example.myapplication.presentation.auth

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.data.model.AuthPreferences
import com.example.myapplication.data.model.LoginResponse
import com.example.myapplication.data.repository.AuthRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class LoginViewModel(
    private val authRepository: AuthRepository,
    private val authPreferences: AuthPreferences
) : ViewModel() {
    var email by mutableStateOf("")
    var password by mutableStateOf("")
    var userToken by mutableStateOf("")
    var userName by mutableStateOf("")
    var userRole by mutableStateOf("")
    var userId by mutableStateOf("")

    private val _loginState = MutableStateFlow<LoginState>(LoginState.Idle)
    val loginState: StateFlow<LoginState> = _loginState

    fun login() {
        _loginState.value = LoginState.Loading
        viewModelScope.launch {
            val result = authRepository.login(email, password)
            _loginState.value = when {
                result.isSuccess -> {
                    val response = result.getOrNull()!!
                    userToken = response.token
                    userName = response.name
                    userRole = response.role
                    userId = response.userId
                    LoginState.Success(response)
                }
                else -> LoginState.Error(result.exceptionOrNull()?.message ?: "Login failed")
            }
        }
    }

    fun checkLoggedInState(): Boolean {
        val isLoggedIn = authPreferences.isLoggedIn()
        if (isLoggedIn) {
            userToken = authPreferences.getToken() ?: ""
            userName = authPreferences.getName() ?: ""
            userRole = authPreferences.getRole() ?: ""
            userId = authPreferences.getUserId() ?: ""
        }
        return isLoggedIn
    }


    fun resetState() {
        _loginState.value = LoginState.Idle
    }
}

sealed class LoginState {
    object Idle : LoginState()
    object Loading : LoginState()
    data class Success(val response: LoginResponse) : LoginState()
    data class Error(val message: String) : LoginState()
}