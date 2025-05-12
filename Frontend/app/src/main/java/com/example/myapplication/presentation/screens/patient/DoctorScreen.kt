package com.example.myapplication.presentation.screens.patient

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
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
fun DoctorsScreen(
    navController: NavHostController,
    authPreferences: AuthPreferences
) {
    val TAG = "DoctorsScreen"
    Log.d(TAG, "Composing DoctorsScreen")
    val rubikFontFamily = FontFamily(
        Font(R.font.rubik_regular, FontWeight.Normal),
        Font(R.font.rubik_medium, FontWeight.Medium),
        Font(R.font.rubik_bold, FontWeight.Bold)
    )
    val patientApi = NetworkProvider.patientApi
    val viewModel: DoctorsViewModel = viewModel(
        factory = DoctorsViewModelFactory(PatientRepository(authPreferences, patientApi))
    )
    val doctorState by viewModel.doctorState.collectAsState()

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
                text = "Your Doctors",
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                fontFamily = rubikFontFamily,
                color = Color(0xFF6B5FF8)
            )
            Spacer(modifier = Modifier.height(16.dp))

            when (val state = doctorState) {
                is DoctorsViewModel.DoctorState.Loading -> {
                    CircularProgressIndicator(modifier = Modifier.padding(16.dp))
                }
                is DoctorsViewModel.DoctorState.Success -> {
                    if (state.doctors.isEmpty()) {
                        Text(
                            text = "No doctors found",
                            fontSize = 16.sp,
                            fontFamily = rubikFontFamily,
                            color = Color.Gray
                        )
                    } else {
                        // Current Doctor (first doctor in the list)
                        Text(
                            text = "Current Doctor",
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Medium,
                            fontFamily = rubikFontFamily
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        val currentDoctor = state.doctors.first()
                        DoctorCard(doctor = currentDoctor) {
                            currentDoctor.id?.let { id ->
                                navController.navigate("doctor_detail/$id")
                            }
                        }
                        Spacer(modifier = Modifier.height(16.dp))

                        // Recent Doctors (remaining doctors)
                        if (state.doctors.size > 1) {
                            Text(
                                text = "Recent Doctors",
                                fontSize = 20.sp,
                                fontWeight = FontWeight.Medium,
                                fontFamily = rubikFontFamily
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            state.doctors.drop(1).forEach { doctor ->
                                DoctorCard(doctor = doctor) {
                                    doctor.id?.let { id ->
                                        navController.navigate("doctor_detail/$id")
                                    }
                                }
                                Spacer(modifier = Modifier.height(8.dp))
                            }
                        }
                    }
                }
                is DoctorsViewModel.DoctorState.Error -> {
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

@Composable
fun DoctorCard(doctor: com.example.myapplication.data.model.patient.Doctor, onClick: () -> Unit) {
    val rubikFontFamily = FontFamily(
        Font(R.font.rubik_regular, FontWeight.Normal),
        Font(R.font.rubik_medium, FontWeight.Medium),
        Font(R.font.rubik_bold, FontWeight.Bold)
    )
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        colors = CardDefaults.cardColors(containerColor = Color(0xFFF5F5F5))
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = painterResource(id = R.drawable.doctor1),
                contentDescription = doctor.name,
                modifier = Modifier
                    .size(50.dp)
                    .clip(CircleShape)
            )
            Spacer(modifier = Modifier.width(16.dp))
            Column {
                Text(
                    text = doctor.name ?: "N/A",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Medium,
                    fontFamily = rubikFontFamily
                )
                Text(
                    text = doctor.specialization ?: "N/A",
                    fontSize = 14.sp,
                    color = Color.Gray,
                    fontFamily = rubikFontFamily
                )
                Text(
                    text = "Rating: ${doctor.rating?.toString() ?: "N/A"}",
                    fontSize = 14.sp,
                    color = Color.Gray,
                    fontFamily = rubikFontFamily
                )
                Text(
                    text = "Experience: ${doctor.experienceYears?.toString() ?: "N/A"} years",
                    fontSize = 14.sp,
                    color = Color.Gray,
                    fontFamily = rubikFontFamily
                )
            }
        }
    }
}