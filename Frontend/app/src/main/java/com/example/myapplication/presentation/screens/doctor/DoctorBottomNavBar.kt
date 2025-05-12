package com.example.myapplication.navigation

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.myapplication.R
import com.example.myapplication.data.model.AuthPreferences
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.draw.clip
import java.net.URLEncoder

data class DoctorBottomNavItem(val title: String, val route: String, val unselectedIcon: Int, val selectedIcon: Int)

@Composable
fun DoctorBottomNavBar(
    navController: NavController,
    authPreferences: AuthPreferences,
    currentRoute: String
) {
    val TAG = "DoctorBottomNavBar"
    val items = listOf(
        DoctorBottomNavItem("Dashboard", "doctor_dashboard", R.drawable.ic_home_unselected, R.drawable.ic_home_selected),
        DoctorBottomNavItem("Appointments", "appointments", R.drawable.ic_appointments_unselected, R.drawable.ic_appointments_selected),
        DoctorBottomNavItem("Chat", "chat", R.drawable.ic_chat_2, R.drawable.ic_chat_2),
        DoctorBottomNavItem("Profile", "profile", R.drawable.ic_users_unselected, R.drawable.ic_users_selected)
    )
    val context = LocalContext.current
    val doctorName = authPreferences.getName() ?: "Guest"
    val encodedName = URLEncoder.encode(doctorName, "UTF-8")

    NavigationBar(
        containerColor = Color(0xFF6B5FF8),
        modifier = Modifier
            .padding(horizontal = 16.dp, vertical = 16.dp)
            .fillMaxWidth()
            .height(50.dp)
            .clip(RoundedCornerShape(32.dp)),
        contentColor = Color.Transparent
    ) {
        val currentDestination = currentRoute.substringBefore("/{name}")

        Row(
            modifier = Modifier.fillMaxWidth()
        ) {
            items.forEach { item ->
                val isSelected = currentDestination == item.route
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .height(70.dp)
                        .clickable {
                            if (isSelected) return@clickable
                            val route = when (item.route) {
                                "doctor_dashboard" -> "doctor_dashboard/$encodedName"
                                else -> item.route
                            }
                            Log.d(TAG, "Navigating to $route")
                            try {
                                when (item.route) {
                                    "chat" -> Toast.makeText(context, "Chat not implemented yet", Toast.LENGTH_SHORT).show()
                                    else -> navController.navigate(route) {
                                        popUpTo(navController.graph.startDestinationId) { inclusive = false }
                                        launchSingleTop = true
                                    }
                                }
                            } catch (e: IllegalStateException) {
                                Log.e(TAG, "Navigation failed: ${e.message}")
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