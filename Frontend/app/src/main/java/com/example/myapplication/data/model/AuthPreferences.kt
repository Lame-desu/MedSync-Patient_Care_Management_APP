package com.example.myapplication.data.model

import android.content.Context
import android.content.SharedPreferences

class AuthPreferences(context: Context) {
    private val sharedPref: SharedPreferences =
        context.getSharedPreferences("auth_prefs", Context.MODE_PRIVATE)

    fun saveAuthData(token: String, userId: String, role: String, name: String) {
        with(sharedPref.edit()) {
            putString("token", token)
            putString("userId", userId)
            putString("role", role)
            putString("name", name)
            putBoolean("isLoggedIn", true)
            apply()
        }
    }

    fun getToken(): String? = sharedPref.getString("token", null)
    fun getUserId(): String? = sharedPref.getString("userId", null)
    fun getRole(): String? = sharedPref.getString("role", null)
    fun getName(): String? = sharedPref.getString("name", null)
    fun isLoggedIn(): Boolean = sharedPref.getBoolean("isLoggedIn", false)
    fun clearAuthData() = sharedPref.edit().clear().apply()
}

