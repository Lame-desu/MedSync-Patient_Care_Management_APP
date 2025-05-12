package com.example.myapplication.presentation.screens.admin

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.myapplication.R
import com.example.myapplication.data.model.AuthPreferences
import com.example.myapplication.data.model.doctor.Doctor
import com.example.myapplication.data.remote.NetworkProvider
import com.example.myapplication.data.repository.doctor.DoctorRepository

@Composable
fun AdminUsersScreen(
    navController: NavHostController,
    name: String,
    authPreferences: AuthPreferences
) {
    val rubikFontFamily = FontFamily(
        Font(R.font.rubik_regular, FontWeight.Normal),
        Font(R.font.rubik_medium, FontWeight.Medium),
        Font(R.font.rubik_bold, FontWeight.Bold)
    )
    val context = LocalContext.current
    val viewModel: AdminUserViewModel = viewModel {
        AdminUserViewModel(DoctorRepository(authPreferences, NetworkProvider.doctorApi))
    }
    var selectedTab by remember { mutableStateOf("Doctors") }
    var showSettingsPopup by remember { mutableStateOf(false) }
    var showNotificationPage by remember { mutableStateOf(false) }
    val tabs = listOf("Doctors", "Triages", "Admins", "Patients")
    val doctorState by viewModel.doctorState.collectAsState()

    Scaffold(
        modifier = Modifier
            .background(Color.White)
            .fillMaxSize(),
        bottomBar = { AdminBottomBar(navController, name) }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White)
                .padding(innerPadding)
                .padding(16.dp)
        ) {
            // Top Section
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Users",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = rubikFontFamily,
                    color = Color(0xFF6B5FF8)
                )
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

            // Search Bar
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp)
                    .padding(bottom = 16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight()
                        .background(Color(0xFFF1F1F9), RoundedCornerShape(25.dp))
                        .padding(horizontal = 16.dp, vertical = 8.dp),
                    contentAlignment = Alignment.CenterStart
                ) {
                    Text(
                        text = "Search For Patients, Doctors, Or Reports...",
                        color = Color.Gray,
                        fontFamily = rubikFontFamily,
                        fontSize = 12.sp
                    )
                }
                IconButton(onClick = { Toast.makeText(context, "Search not implemented", Toast.LENGTH_SHORT).show() }) {
                    Icon(Icons.Default.Search, contentDescription = "Search", tint = Color(0xFF6B5FF8))
                }
            }

            // Tabs
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                tabs.forEach { tab ->
                    val isSelected = selectedTab == tab
                    FilterChip(
                        selected = isSelected,
                        onClick = { selectedTab = tab },
                        label = {
                            Text(
                                text = tab,
                                color = if (isSelected) Color.White else Color(0xFF6B5FF8),
                                fontWeight = FontWeight.SemiBold,
                                fontFamily = rubikFontFamily,
                                fontSize = 13.sp
                            )
                        },
                        modifier = Modifier.padding(horizontal = 3.dp),
                        shape = RoundedCornerShape(16.dp),
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = Color(0xFF6B5FF8),
                            containerColor = Color(0xFFE8EAFE)
                        )
                    )
                }
            }

            // Content based on selected tab
            when (selectedTab) {
                "Doctors" -> {
                    when (val state = doctorState) {
                        is AdminUserViewModel.DoctorState.Loading -> {
                            CircularProgressIndicator(
                                modifier = Modifier
                                    .size(48.dp)
                                    .align(Alignment.CenterHorizontally)
                                    .padding(16.dp)
                            )
                        }
                        is AdminUserViewModel.DoctorState.Success -> {
                            LazyColumn(
                                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                                modifier = Modifier.weight(1f)
                            ) {
                                items(state.doctors.size) { index ->
                                    DoctorCard(state.doctors[index], rubikFontFamily)
                                    Spacer(modifier = Modifier.height(12.dp))
                                }
                            }
                        }
                        is AdminUserViewModel.DoctorState.Error -> {
                            Text(
                                text = state.message,
                                color = Color.Red,
                                modifier = Modifier
                                    .padding(16.dp)
                                    .align(Alignment.CenterHorizontally),
                                fontFamily = rubikFontFamily
                            )
                        }
                        else -> {}
                    }
                }
                else -> {
                    Text(
                        text = "$selectedTab data not implemented",
                        modifier = Modifier
                            .padding(16.dp)
                            .align(Alignment.CenterHorizontally),
                        fontFamily = rubikFontFamily
                    )
                }
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
fun DoctorCard(doctor: Doctor, rubikFontFamily: FontFamily) {
    Card(
        shape = RoundedCornerShape(16.dp),
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            doctor.imageRes?.let { res ->
                Image(
                    painter = painterResource(
                        id = when (res) {
                            "ic_doctor" -> R.drawable.doctor1
                            else -> R.drawable.doctor1
                        }
                    ),
                    contentDescription = "Doctor Image",
                    modifier = Modifier
                        .size(70.dp)
                        .clip(RoundedCornerShape(12.dp)),
                    contentScale = ContentScale.Crop
                )
            } ?: Image(
                painter = painterResource(id = R.drawable.doctor1),
                contentDescription = "Doctor Image",
                modifier = Modifier
                    .size(70.dp)
                    .clip(RoundedCornerShape(12.dp)),
                contentScale = ContentScale.Crop
            )
            Spacer(modifier = Modifier.width(12.dp))

            Column {
                Text(
                    text = doctor.name,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF6B5FF8),
                    fontFamily = rubikFontFamily,
                    fontSize = 16.sp
                )
                Text(
                    text = doctor.specialty ?: "No Specialization",
                    color = Color(0xFF6B5FF8),
                    fontFamily = rubikFontFamily,
                    fontSize = 14.sp
                )
                doctor.ageInYears?.let { age ->
                    Text(
                        text = "$age Years Old",
                        color = Color.Black,
                        fontFamily = rubikFontFamily,
                        fontSize = 14.sp
                    )
                }
                doctor.experienceYears?.let { years ->
                    Text(
                        text = "$years Years Experience",
                        color = Color.Black,
                        fontFamily = rubikFontFamily,
                        fontSize = 14.sp
                    )
                }
                doctor.phone?.let { phone ->
                    Text(
                        text = phone,
                        color = Color.Black,
                        fontFamily = rubikFontFamily,
                        fontSize = 14.sp
                    )
                }
                doctor.rating?.let { rating ->
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            Icons.Default.Star,
                            contentDescription = "Star",
                            tint = Color(0xFFFFC107)
                        )
                        Text(
                            text = rating.toString(),
                            color = Color.Black,
                            modifier = Modifier.padding(start = 4.dp),
                            fontFamily = rubikFontFamily,
                            fontSize = 14.sp
                        )
                    }
                }
                doctor.hospital?.let { hospital ->
                    Text(
                        text = hospital,
                        color = Color.Black,
                        fontFamily = rubikFontFamily,
                        fontSize = 14.sp
                    )
                } ?: Text(
                    text = "No Hospital",
                    color = Color.Gray,
                    fontFamily = rubikFontFamily,
                    fontSize = 14.sp
                )
            }
        }
    }
}