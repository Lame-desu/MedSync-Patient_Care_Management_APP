package com.example.myapplication.presentation.screens.doctor

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.Alignment
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.myapplication.R
import com.example.myapplication.data.model.AuthPreferences
import com.example.myapplication.data.remote.NetworkProvider
import com.example.myapplication.data.repository.doctor.DoctorRepository
import com.example.myapplication.navigation.DoctorBottomNavBar
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun AppointmentsScreen(
    navController: NavHostController,
    authPreferences: AuthPreferences
) {
    val rubikFontFamily = FontFamily(
        Font(R.font.rubik_regular, FontWeight.Normal),
        Font(R.font.rubik_medium, FontWeight.Medium),
        Font(R.font.rubik_bold, FontWeight.Bold)
    )
    val doctorApi = NetworkProvider.doctorApi
    val viewModel: DoctorDashboardViewModel = viewModel(
        factory = DoctorDashboardViewModelFactory(DoctorRepository(authPreferences, doctorApi))
    )
    val dashboardState by viewModel.dashboardState.collectAsState()
    var statusFilter by remember { mutableStateOf("all") }

    LaunchedEffect(Unit) {
        viewModel.fetchAppointments()
    }

    Scaffold(
        topBar = {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color(0xFFD8C4E7))
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Appointments",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = rubikFontFamily,
                    color = Color.Black
                )
            }
        },
        bottomBar = {
            DoctorBottomNavBar(
                navController = navController,
                authPreferences = authPreferences,
                currentRoute = "appointments"
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White)
                .padding(innerPadding)
                .padding(horizontal = 16.dp)
        ) {
            TabRow(selectedTabIndex = when (statusFilter) {
                "all" -> 0
                "pending" -> 1
                "scheduled" -> 2
                else -> 0
            }) {
                Tab(
                    selected = statusFilter == "all",
                    onClick = { statusFilter = "all" },
                    text = { Text("All", fontFamily = rubikFontFamily) }
                )
                Tab(
                    selected = statusFilter == "pending",
                    onClick = { statusFilter = "pending" },
                    text = { Text("Pending", fontFamily = rubikFontFamily) }
                )
                Tab(
                    selected = statusFilter == "scheduled",
                    onClick = { statusFilter = "scheduled" },
                    text = { Text("Scheduled", fontFamily = rubikFontFamily) }
                )
            }

            when (val state = dashboardState) {
                is DoctorDashboardViewModel.DashboardState.Loading -> {
                    CircularProgressIndicator(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(16.dp)
                            .wrapContentSize(Alignment.Center)
                    )
                }
                is DoctorDashboardViewModel.DashboardState.Success -> {
                    val appointments = state.appointments.data.filter {
                        when (statusFilter) {
                            "pending" -> it.status == "pending"
                            "scheduled" -> it.status == "scheduled"
                            else -> true
                        }
                    }
                    if (appointments.isEmpty()) {
                        Text(
                            text = "No appointments found",
                            fontFamily = rubikFontFamily,
                            fontSize = 16.sp,
                            color = Color.Gray,
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(16.dp)
                                .wrapContentSize(Alignment.Center)
                        )
                    } else {
                        LazyColumn(
                            modifier = Modifier.fillMaxSize()
                        ) {
                            items(appointments) { appointment ->
                                Card(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(vertical = 8.dp),
                                    colors = CardDefaults.cardColors(containerColor = Color(0xFFF5F5F5))
                                ) {
                                    Column(modifier = Modifier.padding(16.dp)) {
                                        Text(
                                            text = "Patient: ${appointment.patientId.name}",
                                            fontSize = 16.sp,
                                            fontWeight = FontWeight.Bold,
                                            fontFamily = rubikFontFamily
                                        )
                                        Spacer(modifier = Modifier.height(4.dp))
                                        Text(
                                            text = "Date: ${formatDisplayDate(appointment.date)}",
                                            fontSize = 14.sp,
                                            fontFamily = rubikFontFamily
                                        )
                                        Spacer(modifier = Modifier.height(4.dp))
                                        Text(
                                            text = "Time: ${appointment.time}",
                                            fontSize = 14.sp,
                                            fontFamily = rubikFontFamily
                                        )
                                        Spacer(modifier = Modifier.height(4.dp))
                                        Text(
                                            text = "Status: ${appointment.status.capitalize()}",
                                            fontSize = 14.sp,
                                            fontFamily = rubikFontFamily,
                                            color = when (appointment.status) {
                                                "scheduled" -> Color.Green
                                                "pending" -> Color.Yellow
                                                else -> Color.Black
                                            }
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
                is DoctorDashboardViewModel.DashboardState.Error -> {
                    Text(
                        text = state.message,
                        fontFamily = rubikFontFamily,
                        fontSize = 16.sp,
                        color = Color.Red,
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(16.dp)
                            .wrapContentSize(Alignment.Center)
                    )
                }
            }
        }
    }
}
