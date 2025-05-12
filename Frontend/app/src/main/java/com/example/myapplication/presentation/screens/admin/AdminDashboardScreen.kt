package com.example.myapplication.presentation.screens.admin

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.navigation.NavHostController
import com.example.myapplication.R
import com.example.myapplication.data.model.AuthPreferences

@Composable
fun AdminDashboardScreen(
    navController: NavHostController,
    name: String,
    authPreferences: AuthPreferences
) {
    val rubikFontFamily = FontFamily(
        Font(R.font.rubik_regular, FontWeight.Normal),
        Font(R.font.rubik_medium, FontWeight.Medium),
        Font(R.font.rubik_bold, FontWeight.Bold)
    )
    var showSettingsPopup by remember { mutableStateOf(false) }
    var showNotificationPage by remember { mutableStateOf(false) }

    Scaffold(
        modifier = Modifier
            .background(Color.White)
            .fillMaxSize(),
        bottomBar = { AdminBottomBar(navController, name) }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        colors = listOf(Color(0xFFF1F1F9), Color.White)
                    )
                )
                .padding(innerPadding)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Top Section
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Image(
                        painter = painterResource(id = R.drawable.doctor1),
                        contentDescription = "Profile",
                        modifier = Modifier
                            .size(40.dp)
                            .clip(CircleShape)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Column {
                        Text(
                            text = "Hi, Welcome Back",
                            fontSize = 14.sp,
                            color = Color.Gray,
                            fontFamily = rubikFontFamily
                        )
                        Text(
                            text = name,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            fontFamily = rubikFontFamily
                        )
                    }
                }
                Row {
                    Icon(
                        painter = painterResource(id = R.drawable.bell),
                        contentDescription = "Notifications",
                        modifier = Modifier
                            .size(24.dp)
                            .clickable { showNotificationPage = true },
                        tint = Color(0xFF6B5FF8)
                    )
                    Spacer(modifier = Modifier.width(16.dp))
                    Icon(
                        painter = painterResource(id = R.drawable.cog_outline),
                        contentDescription = "Settings",
                        modifier = Modifier
                            .size(24.dp)
                            .clickable { showSettingsPopup = true },
                        tint = Color(0xFF6B5FF8)
                    )
                }
            }

            // Welcome Card
            Card(
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "Welcome, $name!",
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        fontFamily = rubikFontFamily,
                        color = Color(0xFF6B5FF8)
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "As an admin, you can manage users, oversee triages, and generate reports to keep the system running smoothly.",
                        fontSize = 16.sp,
                        fontFamily = rubikFontFamily,
                        color = Color.Gray,
                        textAlign = androidx.compose.ui.text.style.TextAlign.Center
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Quick Actions
            Text(
                text = "Quick Actions",
                fontSize = 20.sp,
                fontWeight = FontWeight.Medium,
                fontFamily = rubikFontFamily,
                color = Color.Black
            )
            Spacer(modifier = Modifier.height(8.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                ActionCard(
                    title = "Manage Users",
                    icon = R.drawable.ic_users_unselected,
                    onClick = { navController.navigate("admin_users/$name") },
                    rubikFontFamily = rubikFontFamily
                )
                ActionCard(
                    title = "View Reports",
                    icon = R.drawable.ic_report,
                    onClick = { /* Navigate to reports screen */ },
                    rubikFontFamily = rubikFontFamily
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                ActionCard(
                    title = "Manage Triages",
                    icon = R.drawable.ic_triage,
                    onClick = { /* Navigate to triages screen */ },
                    rubikFontFamily = rubikFontFamily
                )
                ActionCard(
                    title = "System Settings",
                    icon = R.drawable.cog_outline,
                    onClick = { showSettingsPopup = true },
                    rubikFontFamily = rubikFontFamily
                )
            }
        }

        // Settings Popup
        if (showSettingsPopup) {
            Dialog(onDismissRequest = { showSettingsPopup = false }) {
                Surface(
                    shape = RoundedCornerShape(16.dp),
                    color = Color.White,
                    modifier = Modifier
                        .fillMaxWidth(0.9f)
                        .height(300.dp)
                        .wrapContentHeight(align = Alignment.CenterVertically)
                ) {
                    Column(
                        modifier = Modifier
                            .padding(24.dp)
                            .fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "Settings",
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Bold,
                            fontFamily = rubikFontFamily
                        )
                        Spacer(modifier = Modifier.height(24.dp))
                        Text(
                            text = "Edit Profile",
                            fontSize = 18.sp,
                            fontFamily = rubikFontFamily,
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { navController.navigate("admin_profile") }
                                .padding(vertical = 8.dp)
                        )
                        Text(
                            text = "Logout",
                            fontSize = 18.sp,
                            fontFamily = rubikFontFamily,
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    authPreferences.clearAuthData()
                                    navController.navigate("login") {
                                        popUpTo("admin_dashboard") { inclusive = true }
                                    }
                                }
                                .padding(vertical = 8.dp)
                        )
                        Spacer(modifier = Modifier.height(24.dp))
                        Button(
                            onClick = { showSettingsPopup = false },
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF6B5FF8)),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(
                                text = "Close",
                                color = Color.White,
                                fontFamily = rubikFontFamily,
                                fontSize = 16.sp
                            )
                        }
                    }
                }
            }
        }

        // Notification Page
        if (showNotificationPage) {
            NotificationsScreen(navController) { showNotificationPage = false }
        }
    }
}

@Composable
fun ActionCard(
    title: String,
    icon: Int,
    onClick: () -> Unit,
    rubikFontFamily: FontFamily
) {
    Card(
        shape = RoundedCornerShape(12.dp),
        modifier = Modifier
            .width(160.dp)
            .height(120.dp)
            .clickable { onClick() },
        colors = CardDefaults.cardColors(containerColor = Color(0xFFE8EAFE)),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Icon(
                painter = painterResource(id = icon),
                contentDescription = title,
                modifier = Modifier.size(32.dp),
                tint = Color(0xFF6B5FF8)
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = title,
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium,
                fontFamily = rubikFontFamily,
                color = Color(0xFF6B5FF8),
                textAlign = androidx.compose.ui.text.style.TextAlign.Center
            )
        }
    }
}