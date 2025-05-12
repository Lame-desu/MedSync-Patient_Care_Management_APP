package com.example.myapplication.presentation.screens.patient

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.myapplication.R
import com.example.myapplication.data.model.AuthPreferences
import com.example.myapplication.data.remote.NetworkProvider
import com.example.myapplication.data.repository.patient.PatientRepository
import com.example.myapplication.navigation.BottomNavigationBar

@Composable
fun PrescriptionScreen(
    navController: NavHostController,
    authPreferences: AuthPreferences
) {
    val TAG = "PrescriptionScreen"
    Log.d(TAG, "Composing PrescriptionScreen")
    val rubikFontFamily = FontFamily(
        Font(R.font.rubik_regular, FontWeight.Normal),
        Font(R.font.rubik_medium, FontWeight.Medium),
        Font(R.font.rubik_bold, FontWeight.Bold)
    )
    val patientApi = NetworkProvider.patientApi
    val viewModel: PrescriptionViewModel = viewModel(
        factory = PrescriptionViewModelFactory(PatientRepository(authPreferences, patientApi))
    )
    val prescriptionState by viewModel.prescriptionState.collectAsState()

    Scaffold(
        bottomBar = { BottomNavigationBar(navController, authPreferences) }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White)
                .padding(innerPadding)
                .padding(24.dp),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Prescriptions",
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                fontFamily = rubikFontFamily,
                color = Color(0xFF6B5FF8)
            )
            Spacer(modifier = Modifier.height(16.dp))

            when (val state = prescriptionState) {
                is PrescriptionViewModel.PrescriptionState.Loading -> {
                    CircularProgressIndicator(modifier = Modifier.padding(16.dp))
                }
                is PrescriptionViewModel.PrescriptionState.Success -> {
                    if (state.prescriptions.isEmpty()) {
                        Text(
                            text = "No prescriptions found",
                            fontSize = 16.sp,
                            fontFamily = rubikFontFamily,
                            color = Color.Gray
                        )
                    } else {
                        state.prescriptions.forEach { prescription ->
                            Card(
                                modifier = Modifier.fillMaxWidth(),
                                colors = CardDefaults.cardColors(containerColor = Color(0xFFF5F5F5))
                            ) {
                                Column(
                                    modifier = Modifier.padding(16.dp),
                                    horizontalAlignment = Alignment.Start
                                ) {
                                    Text(
                                        text = "Doctor ID: ${prescription.doctorId ?: "N/A"}",
                                        fontSize = 18.sp,
                                        fontWeight = FontWeight.Medium,
                                        fontFamily = rubikFontFamily
                                    )
                                    Text(
                                        text = "Medication: ${prescription.medicationEntries?.firstOrNull() ?: "N/A"}",
                                        fontSize = 16.sp,
                                        fontFamily = rubikFontFamily
                                    )
                                    Text(
                                        text = "Instructions: ${prescription.medicationEntries?.firstOrNull()?: "N/A"}",
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
                is PrescriptionViewModel.PrescriptionState.Error -> {
                    Text(
                        text = state.message,
                        fontSize = 16.sp,
                        fontFamily = rubikFontFamily,
                        color = Color.Red
                    )
                }
            }
        }
    }
}