package com.example.myapplication.presentation.screens.patient

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
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
import com.example.myapplication.data.remote.NetworkProvider
import com.example.myapplication.data.repository.patient.PatientRepository
import com.example.myapplication.navigation.BottomNavigationBar
import com.example.myapplication.presentation.screens.admin.NotificationsScreen

@Composable
fun MedicalHistoryScreen(
    navController: NavHostController,
    name: String,
    authPreferences: AuthPreferences
) {
    val TAG = "MedicalHistoryScreen"
    Log.d(TAG, "Composing MedicalHistoryScreen")
    val rubikFontFamily = FontFamily(
        Font(R.font.rubik_regular, FontWeight.Normal),
        Font(R.font.rubik_medium, FontWeight.Medium),
        Font(R.font.rubik_bold, FontWeight.Bold)
    )
    val patientApi = NetworkProvider.patientApi
    val viewModel: MedicalHistoryViewModel = viewModel(
        factory = MedicalHistoryViewModelFactory(PatientRepository(authPreferences, patientApi))
    )
    val medicalRecordState by viewModel.medicalRecordState.collectAsState()
    var showSettingsPopup by remember { mutableStateOf(false) }
    var showNotificationPage by remember { mutableStateOf(false) }

    Scaffold(
        modifier = Modifier
            .background(Color.White)
            .fillMaxSize(),
        bottomBar = { BottomNavigationBar(navController, authPreferences) }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White)
                .padding(innerPadding)
                .padding(16.dp),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Top Section
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Medical History",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = rubikFontFamily,
                    color = Color(0xFF6B5FF8)
                )
                Row {
                }
            }

            // Medical Records
            when (val state = medicalRecordState) {
                is MedicalHistoryViewModel.MedicalRecordState.Loading -> {
                    CircularProgressIndicator(modifier = Modifier.padding(16.dp))
                }
                is MedicalHistoryViewModel.MedicalRecordState.Success -> {
                    if (state.records.isEmpty()) {
                        Text(
                            text = "No medical records found",
                            fontSize = 16.sp,
                            fontFamily = rubikFontFamily,
                            color = Color.Gray
                        )
                    } else {
                        state.records.forEach { record ->
                            Card(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 4.dp),
                                colors = CardDefaults.cardColors(containerColor = Color.White),
                                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                                shape = RoundedCornerShape(12.dp)
                            ) {
                                Column(
                                    modifier = Modifier.padding(16.dp),
                                    horizontalAlignment = Alignment.Start
                                ) {
                                    Text(
                                        text = "Doctor: ${record.doctorInfo?.name ?: "N/A"}",
                                        fontSize = 18.sp,
                                        fontWeight = FontWeight.Medium,
                                        fontFamily = rubikFontFamily,
                                        color = Color(0xFF6B5FF8)
                                    )
                                    Text(
                                        text = "Diagnosis: ${record.diagnosis ?: "N/A"}",
                                        fontSize = 16.sp,
                                        fontFamily = rubikFontFamily
                                    )
                                    Text(
                                        text = "Treatment: ${record.treatment ?: "N/A"}",
                                        fontSize = 14.sp,
                                        color = Color.Gray,
                                        fontFamily = rubikFontFamily
                                    )
                                    Text(
                                        text = "Last Updated: ${record.lastUpdated ?: "N/A"}",
                                        fontSize = 14.sp,
                                        color = Color.Gray,
                                        fontFamily = rubikFontFamily
                                    )
                                }
                            }
                            Spacer(modifier = Modifier.height(8.dp))
                        }
                    }
                }
                is MedicalHistoryViewModel.MedicalRecordState.Error -> {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = state.message,
                            fontSize = 16.sp,
                            fontFamily = rubikFontFamily,
                            color = Color.Red
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Button(
                            onClick = { viewModel.fetchMedicalRecords() },
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF6B5FF8))
                        ) {
                            Text(
                                text = "Retry",
                                color = Color.White,
                                fontFamily = rubikFontFamily
                            )
                        }
                    }
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
                                .clickable { navController.navigate("edit_profile") }
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
                                        popUpTo("medical_history") { inclusive = true }
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