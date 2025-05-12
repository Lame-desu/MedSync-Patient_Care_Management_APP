package com.example.myapplication.presentation.screens.patient

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
fun DoctorDetailScreen(
    navController: NavHostController,
    doctorId: String,
    authPreferences: AuthPreferences
) {
    val rubikFontFamily = FontFamily(
        Font(R.font.rubik_regular, FontWeight.Normal),
        Font(R.font.rubik_medium, FontWeight.Medium),
        Font(R.font.rubik_bold, FontWeight.Bold)
    )
    val patientApi = NetworkProvider.patientApi
    val viewModel: DoctorsViewModel = viewModel(
        factory = DoctorsViewModelFactory(PatientRepository(authPreferences, patientApi))
    )
    val selectedDoctor by viewModel.selectedDoctor.collectAsState()

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
                text = "Doctor Details",
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                fontFamily = rubikFontFamily,
                color = Color(0xFF6B5FF8)
            )
            Spacer(modifier = Modifier.height(16.dp))

            if (selectedDoctor != null && selectedDoctor?.id == doctorId) {
                val doctor = selectedDoctor!!
                Image(
                    painter = painterResource(id = R.drawable.doctor1),
                    contentDescription = doctor.name,
                    modifier = Modifier
                        .size(100.dp)
                        .clip(CircleShape)
                )
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = doctor.name ?: "N/A",
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Medium,
                    fontFamily = rubikFontFamily
                )
                Text(
                    text = doctor.specialization ?: "N/A",
                    fontSize = 18.sp,
                    color = Color.Gray,
                    fontFamily = rubikFontFamily
                )
                Text(
                    text = "Rating: ${doctor.rating?.toString() ?: "N/A"}",
                    fontSize = 16.sp,
                    color = Color.Gray,
                    fontFamily = rubikFontFamily
                )
                Text(
                    text = "Experience: ${doctor.experienceYears?.toString() ?: "N/A"} years",
                    fontSize = 16.sp,
                    color = Color.Gray,
                    fontFamily = rubikFontFamily
                )
            } else {
                Text(
                    text = "Doctor not found (ID: $doctorId)",
                    fontSize = 18.sp,
                    fontFamily = rubikFontFamily,
                    color = Color.Red
                )
            }
        }
    }
}