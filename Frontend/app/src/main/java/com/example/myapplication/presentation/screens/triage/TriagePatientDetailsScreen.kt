package com.example.myapplication.presentation.screens.triage

import android.app.DatePickerDialog
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
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
import com.example.myapplication.data.model.triage.TriageData
import com.example.myapplication.data.remote.NetworkProvider
import com.example.myapplication.data.repository.triage.TriageRepository
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun TriagePatientDetailsScreen(
    navController: NavHostController,
    authPreferences: AuthPreferences,
    patientId: String
) {
    val rubikFontFamily = FontFamily(
        Font(R.font.rubik_regular, FontWeight.Normal),
        Font(R.font.rubik_medium, FontWeight.Medium),
        Font(R.font.rubik_bold, FontWeight.Bold)
    )
    val context = LocalContext.current
    var appointmentDate by remember { mutableStateOf("") }
    var triageNotes by remember { mutableStateOf("") }
    var selectedDoctorId by remember { mutableStateOf<String?>(null) }
    var doctorQuery by remember { mutableStateOf("") }
    var showDoctorDropdown by remember { mutableStateOf(false) }
    var hasNavigated by remember { mutableStateOf(false) }

    val viewModel: TriagePatientDetailsViewModel = viewModel {
        TriagePatientDetailsViewModel(TriageRepository(authPreferences, NetworkProvider.triageApi))
    }
    val detailsState by viewModel.detailsState.collectAsState()
    val submitState by viewModel.submitState.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.fetchPatientDetails(patientId)
    }

    LaunchedEffect(submitState) {
        when (val state = submitState) {
            is TriagePatientDetailsViewModel.SubmitState.Success -> {
                if (hasNavigated) return@LaunchedEffect
                hasNavigated = true
                Toast.makeText(context, "Triage data submitted successfully", Toast.LENGTH_SHORT).show()
                navController.navigate("triage_home") {
                    popUpTo("triage_patient_details/$patientId") { inclusive = true }
                    launchSingleTop = true
                }
            }
            is TriagePatientDetailsViewModel.SubmitState.Error -> {
                Toast.makeText(context, state.message, Toast.LENGTH_SHORT).show()
            }
            else -> Unit
        }
    }

    Scaffold(
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
            when (val state = detailsState) {
                is TriagePatientDetailsViewModel.DetailsState.Loading -> {
                    CircularProgressIndicator()
                }
                is TriagePatientDetailsViewModel.DetailsState.Success -> {
                    val booking = state.booking
                    Image(
                        painter = painterResource(id = R.drawable.ic_patient),
                        contentDescription = "Patient Photo",
                        modifier = Modifier
                            .size(120.dp)
                            .clip(CircleShape)
                            .border(2.dp, Color(0xFF6B5FF8), CircleShape)
                    )
                    Spacer(modifier = Modifier.height(16.dp))

                    Text(
                        text = "Patient Name: ${booking?.patientName ?: "Unknown"}",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        fontFamily = rubikFontFamily,
                        color = Color.Black
                    )
                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = "Preferred Time: ${booking?.preferredTime ?: "Not set"}",
                        fontSize = 16.sp,
                        fontFamily = rubikFontFamily,
                        color = Color.Gray
                    )
                    Spacer(modifier = Modifier.height(16.dp))

                    OutlinedTextField(
                        value = appointmentDate,
                        onValueChange = {},
                        label = { Text("Appointment Date", fontFamily = rubikFontFamily) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                val calendar = Calendar.getInstance()
                                DatePickerDialog(
                                    context,
                                    { _, year, month, dayOfMonth ->
                                        val selected = Calendar.getInstance()
                                        selected.set(year, month, dayOfMonth)
                                        val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                                        appointmentDate = dateFormat.format(selected.time)
                                    },
                                    calendar.get(Calendar.YEAR),
                                    calendar.get(Calendar.MONTH),
                                    calendar.get(Calendar.DAY_OF_MONTH)
                                ).show()
                            },
                        enabled = false
                    )
                    Spacer(modifier = Modifier.height(16.dp))

                    // Autocomplete Doctor Dropdown
                    Box {
                        OutlinedTextField(
                            value = doctorQuery,
                            onValueChange = {
                                doctorQuery = it
                                showDoctorDropdown = it.isNotBlank()
                            },
                            label = { Text("Assign Doctor", fontFamily = rubikFontFamily) },
                            modifier = Modifier.fillMaxWidth(),
                            singleLine = true
                        )
                        if (showDoctorDropdown) {
                            DropdownMenu(
                                expanded = showDoctorDropdown,
                                onDismissRequest = { showDoctorDropdown = false },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .background(Color.White)
                            ) {
                                state.doctors
                                    .filter { it.name.contains(doctorQuery, ignoreCase = true) }
                                    .forEach { doctor ->
                                        DropdownMenuItem(
                                            text = {
                                                Text(
                                                    "${doctor.name} (${doctor.specialty})",
                                                    fontFamily = rubikFontFamily
                                                )
                                            },
                                            onClick = {
                                                doctorQuery = doctor.name
                                                selectedDoctorId = doctor.id
                                                showDoctorDropdown = false
                                            }
                                        )
                                    }
                            }
                        }
                    }
                    Spacer(modifier = Modifier.height(16.dp))

                    OutlinedTextField(
                        value = triageNotes,
                        onValueChange = { triageNotes = it },
                        label = { Text("Triage Notes", fontFamily = rubikFontFamily) },
                        modifier = Modifier.fillMaxWidth(),
                        maxLines = 4
                    )
                    Spacer(modifier = Modifier.height(16.dp))

                    Button(
                        onClick = {
                            if (submitState is TriagePatientDetailsViewModel.SubmitState.Loading) return@Button
                            if (appointmentDate.isBlank() || selectedDoctorId == null || triageNotes.isBlank()) {
                                Toast.makeText(context, "Please fill all fields", Toast.LENGTH_SHORT).show()
                                return@Button
                            }
                            if (booking == null) {
                                Toast.makeText(context, "No booking found for patient", Toast.LENGTH_SHORT).show()
                                return@Button
                            }
                            val defaultDate = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())
                            val triageData = TriageData(
                                bookingId = booking.id,
                                patientId = booking.patientId.id,
                                doctorId = selectedDoctorId!!,
                                date = booking.preferredDate ?: defaultDate,
                                time = booking.preferredTime ?: "09:00",
                                status = "scheduled",
                                reason = booking.symptoms,
                                priority = booking.priority,
                                triageNotes = triageNotes
                            )
                            viewModel.submitTriageData(booking.id, triageData)
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF6B5FF8)),
                        enabled = submitState !is TriagePatientDetailsViewModel.SubmitState.Loading
                    ) {
                        if (submitState is TriagePatientDetailsViewModel.SubmitState.Loading) {
                            CircularProgressIndicator(
                                color = Color.White,
                                modifier = Modifier.size(24.dp)
                            )
                        } else {
                            Text(
                                text = "Send",
                                color = Color.White,
                                fontFamily = rubikFontFamily,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
                is TriagePatientDetailsViewModel.DetailsState.Error -> {
                    Toast.makeText(context, state.message, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}