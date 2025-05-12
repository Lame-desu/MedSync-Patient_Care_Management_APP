package com.example.myapplication.presentation.screens.doctor

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.myapplication.R
import com.example.myapplication.data.model.AuthPreferences
import com.example.myapplication.data.remote.NetworkProvider
import com.example.myapplication.data.repository.doctor.DoctorRepository
import com.example.myapplication.navigation.DoctorBottomNavBar
import java.text.SimpleDateFormat
import java.util.Locale

@Composable
fun DoctorDashboardScreen(
    navController: NavHostController,
    authPreferences: AuthPreferences
) {
    val TAG = "DoctorDashboardScreen"
    Log.d(TAG, "Composing DoctorDashboardScreen")
    val rubikFontFamily = FontFamily(
        Font(R.font.rubik_regular, FontWeight.Normal),
        Font(R.font.rubik_medium, FontWeight.Medium),
        Font(R.font.rubik_bold, FontWeight.Bold)
    )
    val doctorApi = NetworkProvider.doctorApi
    val viewModel: DoctorDashboardViewModel = viewModel(
        factory = DoctorDashboardViewModelFactory(DoctorRepository(authPreferences, doctorApi))
    )
    val patientViewModel: PatientListViewModel = viewModel(
        factory = PatientListViewModelFactory(DoctorRepository(authPreferences, doctorApi))
    )
    Log.d(TAG, "ViewModel initialized: $viewModel, $patientViewModel")
    val dashboardState by viewModel.dashboardState.collectAsState()
    val patientState by patientViewModel.patientState.collectAsState()
    var showSettingsPopup by remember { mutableStateOf(false) }
    val doctorName by remember { mutableStateOf(authPreferences.getName() ?: "Doctor") }
    var searchQuery by remember { mutableStateOf(TextFieldValue("")) }

    LaunchedEffect(Unit) {
        Log.d(TAG, "Fetching appointments and patients on launch")
        viewModel.fetchAppointments()
        patientViewModel.fetchPatients()
    }

    Scaffold(
        topBar = {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color(0xFF6B5FF8))
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
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
                            fontSize = 18.sp,
                            color = Color.White,
                            fontFamily = rubikFontFamily
                        )
                        Text(
                            text = doctorName,
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Bold,
                            fontFamily = rubikFontFamily,
                            color = Color.White
                        )
                    }
                }
                Row {
                    Icon(
                        painter = painterResource(id = R.drawable.bell),
                        contentDescription = "Notifications",
                        modifier = Modifier
                            .size(24.dp)
                            .clickable { navController.navigate("notifications") },
                        tint = Color.White
                    )
                    Spacer(modifier = Modifier.width(16.dp))
                    Icon(
                        painter = painterResource(id = R.drawable.cog_outline),
                        contentDescription = "Settings",
                        modifier = Modifier
                            .size(24.dp)
                            .clickable { showSettingsPopup = true },
                        tint = Color.White
                    )
                }
            }
        },
        bottomBar = {
            DoctorBottomNavBar(
                navController = navController,
                authPreferences = authPreferences,
                currentRoute = "doctor_dashboard"
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White)
                .padding(innerPadding)
                .padding(16.dp)
        ) {
            // Search Field
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                label = { Text("Search Patients", fontFamily = rubikFontFamily) },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp),
                leadingIcon = {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_search),
                        contentDescription = "Search",
                        tint = Color(0xFF6B5FF8)
                    )
                },
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Color(0xFF6B5FF8),
                    unfocusedBorderColor = Color.Gray
                )
            )

            // Appointments Card
            Card(
                shape = RoundedCornerShape(20.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFFF5F5F5))
            ) {
                Column(
                    modifier = Modifier
                        .background(
                            brush = androidx.compose.ui.graphics.Brush.linearGradient(
                                colors = listOf(Color(0xFFD8C4E7), Color.White)
                            )
                        )
                        .padding(16.dp)
                ) {
                    Text(
                        text = "Upcoming Appointments",
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Bold,
                        fontFamily = rubikFontFamily,
                        color = Color(0xFF6B5FF8),
                        modifier = Modifier.padding(bottom = 12.dp)
                    )

                    when (val state = dashboardState) {
                        is DoctorDashboardViewModel.DashboardState.Loading -> {
                            CircularProgressIndicator(modifier = Modifier.align(Alignment.CenterHorizontally))
                        }
                        is DoctorDashboardViewModel.DashboardState.Success -> {
                            val appointments = state.appointments.data
                            if (appointments.isEmpty()) {
                                Text(
                                    text = "No appointments scheduled",
                                    fontFamily = rubikFontFamily,
                                    fontSize = 16.sp,
                                    color = Color.Gray,
                                    modifier = Modifier
                                        .align(Alignment.CenterHorizontally)
                                        .padding(16.dp)
                                )
                            } else {
                                LazyColumn {
                                    items(appointments) { appointment ->
                                        Card(
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .padding(vertical = 8.dp),
                                            colors = CardDefaults.cardColors(containerColor = Color.White),
                                            elevation = CardDefaults.cardElevation(4.dp),
                                            shape = RoundedCornerShape(12.dp)
                                        ) {
                                            Row(
                                                modifier = Modifier
                                                    .padding(12.dp)
                                                    .fillMaxWidth(),
                                                verticalAlignment = Alignment.CenterVertically,
                                                horizontalArrangement = Arrangement.SpaceBetween
                                            ) {
                                                Column {
                                                    Text(
                                                        text = appointment.patientId.name,
                                                        fontSize = 18.sp,
                                                        fontWeight = FontWeight.Medium,
                                                        fontFamily = rubikFontFamily,
                                                        color = Color(0xFF4A3FB7)
                                                    )
                                                    Spacer(modifier = Modifier.height(4.dp))
                                                    Text(
                                                        text = formatDisplayDate(appointment.date),
                                                        fontSize = 14.sp,
                                                        fontFamily = rubikFontFamily,
                                                        color = Color.Gray
                                                    )
                                                    Text(
                                                        text = "Time: ${appointment.time}",
                                                        fontSize = 14.sp,
                                                        fontFamily = rubikFontFamily,
                                                        color = Color.Gray
                                                    )
                                                }
                                                Box(
                                                    modifier = Modifier
                                                        .clip(RoundedCornerShape(8.dp))
                                                        .background(
                                                            when (appointment.status) {
                                                                "scheduled" -> Color(0xFF4CAF50)
                                                                "pending" -> Color(0xFFFFC107)
                                                                else -> Color.Gray
                                                            }
                                                        )
                                                        .padding(horizontal = 8.dp, vertical = 4.dp)
                                                ) {
                                                    Text(
                                                        text = appointment.status.capitalize(),
                                                        fontSize = 12.sp,
                                                        color = Color.White,
                                                        fontFamily = rubikFontFamily
                                                    )
                                                }
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
                                    .align(Alignment.CenterHorizontally)
                                    .padding(16.dp)
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Patients Cards
            Text(
                text = "Patients",
                fontSize = 18.sp,
                fontWeight = FontWeight.Medium,
                fontFamily = rubikFontFamily,
                color = Color(0xFF6B5FF8),
                modifier = Modifier.padding(bottom = 8.dp)
            )
            Card(
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = Color.White)
            ) {
                when (val state = patientState) {
                    is PatientListViewModel.PatientState.Loading -> {
                        CircularProgressIndicator(modifier = Modifier.padding(16.dp))
                    }
                    is PatientListViewModel.PatientState.Success -> {
                        val patients = state.patients.data
                        if (patients.isEmpty()) {
                            Text(
                                text = "No patients found",
                                fontFamily = rubikFontFamily,
                                fontSize = 16.sp,
                                color = Color.Gray,
                                modifier = Modifier.padding(16.dp)
                            )
                        } else {
                            LazyColumn {
                                items(patients) { patient ->
                                    Card(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(8.dp)
                                            .clickable {
                                                navController.navigate("patient_details/${patient.id}")
                                            },
                                        colors = CardDefaults.cardColors(containerColor = Color(0xFFF5F5F5))
                                    ) {
                                        Row(
                                            modifier = Modifier.padding(16.dp),
                                            verticalAlignment = Alignment.CenterVertically
                                        ) {
                                            Image(
                                                painter = painterResource(id = R.drawable.ic_patient),
                                                contentDescription = "Patient",
                                                modifier = Modifier
                                                    .size(40.dp)
                                                    .clip(CircleShape)
                                            )
                                            Spacer(modifier = Modifier.width(16.dp))
                                            Column {
                                                Text(
                                                    text = patient.name,
                                                    fontSize = 16.sp,
                                                    fontWeight = FontWeight.Medium,
                                                    fontFamily = rubikFontFamily,
                                                    color = Color.Black
                                                )
                                                Text(
                                                    text = patient.email,
                                                    fontSize = 12.sp,
                                                    fontFamily = rubikFontFamily,
                                                    color = Color.Gray
                                                )
                                            }
                                        }
                                    }
                                    Divider()
                                }
                            }
                        }
                    }
                    is PatientListViewModel.PatientState.Error -> {
                        Text(
                            text = state.message,
                            fontFamily = rubikFontFamily,
                            fontSize = 16.sp,
                            color = Color.Red,
                            modifier = Modifier.padding(16.dp)
                        )
                    }
                }
            }
        }
    }

    // Settings Popup
    if (showSettingsPopup) {
        Dialog(onDismissRequest ={ showSettingsPopup = false }) {
            Surface(
                shape = RoundedCornerShape(16.dp),
                color = Color.White
            ) {
                Column(
                    modifier = Modifier.padding(16.dp)
                ) {
                    Text(
                        text = "Settings",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        fontFamily = rubikFontFamily
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = "Logout",
                        fontSize = 16.sp,
                        modifier = Modifier.clickable {
                            authPreferences.clearAuthData()
                            navController.navigate("login") {
                                popUpTo("doctor_dashboard") { inclusive = true }
                            }
                        }
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Button(
                        onClick = { showSettingsPopup = false },
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF6B5FF8))
                    ) {
                        Text("Close", color = Color.White, fontFamily = rubikFontFamily)
                    }
                }
            }
        }
    }
}

fun formatDisplayDate(dateString: String): String {
    return try {
        val inputFormat = SimpleDateFormat("EEE MMM dd yyyy HH:mm:ss 'GMT'Z", Locale.getDefault())
        val outputFormat = SimpleDateFormat("MMM dd, yyyy", Locale.getDefault())
        val date = inputFormat.parse(dateString)
        date?.let { outputFormat.format(it) } ?: "N/A"
    } catch (e: Exception) {
        "N/A"
    }
}