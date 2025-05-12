package com.example.myapplication.presentation.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.myapplication.data.model.AuthPreferences
import com.example.myapplication.data.repository.AuthRepository

class LoginViewModelFactory(
    private val repository: AuthRepository,
    private val authPreferences: AuthPreferences
) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(LoginViewModel::class.java)) {
            return LoginViewModel(repository, authPreferences) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}