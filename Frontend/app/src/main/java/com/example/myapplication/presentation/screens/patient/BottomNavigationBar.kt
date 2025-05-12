package com.example.myapplication.navigation

import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.myapplication.R
import com.example.myapplication.data.model.AuthPreferences
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.draw.clip
import java.net.URLEncoder

@Composable
fun BottomNavigationBar(navController: NavController, authPreferences: AuthPreferences) {
    val items = listOf(
        BottomNavItem("Dashboard", "patient_dashboard", R.drawable.ic_home_unselected, R.drawable.ic_home_selected),
        BottomNavItem("Appointments", "appointment_booking", R.drawable.ic_appointments_unselected, R.drawable.ic_appointments_selected),
        BottomNavItem("Doctors", "doctors", R.drawable.ic_doctors_unselected, R.drawable.ic_doctors_selected),
        BottomNavItem("History", "medical_history", R.drawable.ic_medical_history_unselected, R.drawable.ic_medical_history_selected),
        BottomNavItem("Prescriptions", "prescriptions", R.drawable.ic_perscriptions_unselected, R.drawable.ic_perscriptions_selected),
    )

    NavigationBar(
        containerColor = Color(0xFF6B5FF8),
        modifier = Modifier
            .padding(horizontal = 16.dp, vertical = 16.dp)
            .fillMaxWidth()
            .height(50.dp)
            .clip(RoundedCornerShape(32.dp)),
        contentColor = Color.Transparent
    ) {
        val currentRoute by navController.currentBackStackEntryAsState()
        val currentDestination = currentRoute?.destination?.route?.substringBefore("/{name}")

        Row(
            modifier = Modifier.fillMaxWidth()
        ) {
            items.forEach { item ->
                val isSelected = if (item.route == "patient_dashboard" || item.route == "medical_history") {
                    currentDestination?.startsWith(item.route) == true
                } else {
                    currentDestination == item.route
                }
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .height(70.dp)
                        .clickable {
                            if (isSelected) return@clickable
                            val route = if (item.route == "patient_dashboard" || item.route == "medical_history") {
                                val name = authPreferences.getName() ?: "Guest"
                                val encodedName = try {
                                    URLEncoder.encode(name, "UTF-8")
                                } catch (e: Exception) {
                                    Log.e("BottomNavigationBar", "Encoding failed: ${e.message}")
                                    "Guest"
                                }
                                "${item.route}/$encodedName"
                            } else {
                                item.route
                            }
                            Log.d("BottomNavigationBar", "Navigating to $route")
                            try {
                                navController.navigate(route) {
                                    popUpTo(navController.graph.id) { inclusive = false }
                                    launchSingleTop = true
                                }
                            } catch (e: IllegalStateException) {
                                Log.e("BottomNavigationBar", "Navigation failed: ${e.message}")
                            }
                        }
                ) {
                    Icon(
                        painter = painterResource(id = if (isSelected) item.selectedIcon else item.unselectedIcon),
                        contentDescription = null,
                        modifier = Modifier
                            .size(30.dp)
                            .align(Alignment.Center),
                        tint = Color.Unspecified
                    )
                }
            }
        }
    }
}

data class BottomNavItem(val title: String, val route: String, val unselectedIcon: Int, val selectedIcon: Int)