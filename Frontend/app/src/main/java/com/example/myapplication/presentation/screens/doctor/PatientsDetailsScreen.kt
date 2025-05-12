package com.example.myapplication.presentation.screens.doctor

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.myapplication.R
import com.example.myapplication.data.model.AuthPreferences
import com.example.myapplication.data.model.doctor.*
import com.example.myapplication.data.remote.NetworkProvider
import com.example.myapplication.data.repository.doctor.DoctorRepository
import com.example.myapplication.navigation.DoctorBottomNavBar
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun PatientDetailsScreen(
    navController: NavHostController,
    patientId: String,
    authPreferences: AuthPreferences
) {
    val TAG = "PatientDetailsScreen"
    Log.d(TAG, "Composing PatientDetailsScreen for patientId: $patientId")
    val rubikFontFamily = FontFamily(
        Font(R.font.rubik_regular, FontWeight.Normal),
        Font(R.font.rubik_medium, FontWeight.Medium),
        Font(R.font.rubik_bold, FontWeight.Bold)
    )
    val doctorApi = NetworkProvider.doctorApi
    val viewModel: PatientDetailsViewModel = viewModel(
        factory = PatientDetailsViewModelFactory(DoctorRepository(authPreferences, doctorApi), patientId)
    )
    Log.d(TAG, "ViewModel initialized: $viewModel")

    val patientDetailsState by viewModel.patientDetailsState.collectAsState()
    var showCreateMedicalDialog by remember { mutableStateOf(false) }
    var showCreatePrescriptionDialog by remember { mutableStateOf(false) }
    val snackbarHostState = remember { SnackbarHostState() }
    var showSuccessMessage by remember { mutableStateOf(false) }

    LaunchedEffect(showSuccessMessage) {
        if (showSuccessMessage) {
            snackbarHostState.showSnackbar("Successful")
            navController.navigate("doctor_dashboard/{name}") {
                popUpTo(navController.graph.startDestinationId) { inclusive = true }
            }
            showSuccessMessage = false
        }
    }

    Scaffold(
        topBar = {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.White)
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.bell),
                    contentDescription = "Back",
                    modifier = Modifier
                        .size(24.dp)
                        .clickable { navController.navigateUp() },
                    tint = Color(0xFF6B5FF8)
                )
                Spacer(modifier = Modifier.width(16.dp))
                Text(
                    text = "Patient Details",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = rubikFontFamily,
                    color = Color(0xFF6B5FF8)
                )
            }
        },
        bottomBar = {
            DoctorBottomNavBar(
                navController = navController,
                authPreferences = authPreferences,
                currentRoute = "patient_details"
            )
        },
        snackbarHost = {
            SnackbarHost(
                hostState = snackbarHostState,
                modifier = Modifier.padding(16.dp)
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
            when (val state = patientDetailsState) {
                is PatientDetailsViewModel.PatientDetailsState.Loading -> {
                    CircularProgressIndicator(modifier = Modifier.padding(16.dp))
                }
                is PatientDetailsViewModel.PatientDetailsState.Success -> {
                    val patient = state.patientDetails.data?.patient
                    val medicalRecords = state.medicalRecords
                    val prescriptions = state.prescriptions

                    Card(
                        shape = RoundedCornerShape(16.dp),
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(containerColor = Color.White)
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text(
                                text = "Name: ${patient?.name ?: "N/A"}",
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold,
                                fontFamily = rubikFontFamily
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = "Email: ${patient?.email ?: "N/A"}",
                                fontSize = 16.sp,
                                fontFamily = rubikFontFamily
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = "Date of Birth: ${patient?.dateOfBirth ?: "N/A"}",
                                fontSize = 16.sp,
                                fontFamily = rubikFontFamily
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = "Gender: ${patient?.gender ?: "N/A"}",
                                fontSize = 16.sp,
                                fontFamily = rubikFontFamily
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = "Blood Group: ${patient?.bloodGroup ?: "N/A"}",
                                fontSize = 16.sp,
                                fontFamily = rubikFontFamily
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    Text(
                        text = "Medical Records",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        fontFamily = rubikFontFamily
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    LazyColumn {
                        items(medicalRecords) { record ->
                            Card(
                                shape = RoundedCornerShape(8.dp),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 4.dp)
                                    .clickable {
                                        record.recordId?.let { id ->
                                            navController.navigate("medical_record_details/$id")
                                        }
                                    },
                                colors = CardDefaults.cardColors(containerColor = Color.White)
                            ) {
                                Column(modifier = Modifier.padding(8.dp)) {
                                    Text(
                                        text = "Diagnosis: ${record.diagnosis ?: "N/A"}",
                                        fontSize = 16.sp,
                                        fontFamily = rubikFontFamily
                                    )
                                    Text(
                                        text = "Doctor: ${record.doctorInfo.name}",
                                        fontSize = 14.sp,
                                        color = Color.Gray,
                                        fontFamily = rubikFontFamily
                                    )
                                    Text(
                                        text = "Last Updated: ${record.lastUpdated ?: "N/A"}",
                                        fontSize = 12.sp,
                                        color = Color.Gray,
                                        fontFamily = rubikFontFamily
                                    )
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    Text(
                        text = "Prescriptions",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        fontFamily = rubikFontFamily
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    LazyColumn {
                        items(prescriptions) { prescription ->
                            Card(
                                shape = RoundedCornerShape(8.dp),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 4.dp)
                                    .clickable {
                                        prescription.id?.let { id ->
                                            navController.navigate("prescription_details/$id")
                                        }
                                    },
                                colors = CardDefaults.cardColors(containerColor = Color.White)
                            ) {
                                Column(modifier = Modifier.padding(8.dp)) {
                                    Text(
                                        text = "Medication: ${prescription.medications?.firstOrNull()?.name ?: "N/A"}",
                                        fontSize = 16.sp,
                                        fontFamily = rubikFontFamily
                                    )
                                    Text(
                                        text = "Doctor: ${prescription.doctorId ?: "N/A"}",
                                        fontSize = 14.sp,
                                        color = Color.Gray,
                                        fontFamily = rubikFontFamily
                                    )
                                    Text(
                                        text = "Created: ${formatDate(prescription.createdAt)}",
                                        fontSize = 12.sp,
                                        color = Color.Gray,
                                        fontFamily = rubikFontFamily
                                    )
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        Button(
                            onClick = { showCreateMedicalDialog = true },
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF6B5FF8)),
                            modifier = Modifier.weight(1f)
                        ) {
                            Text("Add Medical Record", color = Color.White, fontFamily = rubikFontFamily)
                        }
                        Spacer(modifier = Modifier.width(8.dp))
                        Button(
                            onClick = { showCreatePrescriptionDialog = true },
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF6B5FF8)),
                            modifier = Modifier.weight(1f)
                        ) {
                            Text("Add Prescription", color = Color.White, fontFamily = rubikFontFamily)
                        }
                    }
                }
                is PatientDetailsViewModel.PatientDetailsState.Error -> {
                    Text(
                        text = state.message,
                        fontFamily = rubikFontFamily,
                        fontSize = 16.sp,
                        color = Color.Red,
                        modifier = Modifier.padding(16.dp)
                    )
                }
            }

            if (showCreateMedicalDialog) {
                CreateMedicalRecordDialog(
                    onDismiss = { showCreateMedicalDialog = false },
                    onSubmit = { diagnosis, treatment, notes ->
                        viewModel.createMedicalRecord(diagnosis, treatment, notes)
                        showCreateMedicalDialog = false
                        showSuccessMessage = true
                    },
                    rubikFontFamily = rubikFontFamily
                )
            }

            if (showCreatePrescriptionDialog) {
                CreatePrescriptionDialog(
                    onDismiss = { showCreatePrescriptionDialog = false },
                    onSubmit = { appointmentId, medications ->
                        val request = CreatePrescriptionRequest(patientId, appointmentId, medications)
                        viewModel.createPrescription(request)
                        showCreatePrescriptionDialog = false
                        showSuccessMessage = true
                    },
                    rubikFontFamily = rubikFontFamily
                )
            }
        }
    }
}

@Composable
fun CreateMedicalRecordDialog(
    onDismiss: () -> Unit,
    onSubmit: (String, String, String) -> Unit,
    rubikFontFamily: FontFamily
) {
    var diagnosis by remember { mutableStateOf(TextFieldValue("")) }
    var treatment by remember { mutableStateOf(TextFieldValue("")) }
    var notes by remember { mutableStateOf(TextFieldValue("")) }

    Dialog(onDismissRequest = onDismiss) {
        Surface(
            shape = RoundedCornerShape(16.dp),
            color = Color.White,
            modifier = Modifier
                .padding(16.dp)
                .width(300.dp)
        ) {
            Column(
                modifier = Modifier
                    .padding(24.dp)
                    .fillMaxWidth()
            ) {
                Text(
                    text = "Add Medical Record",
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = rubikFontFamily,
                    modifier = Modifier.padding(bottom = 16.dp),
                    color = Color(0xFF6B5FF8)
                )

                OutlinedTextField(
                    value = diagnosis,
                    onValueChange = { diagnosis = it },
                    label = { Text("Diagnosis", fontFamily = rubikFontFamily) },
                    modifier = Modifier.fillMaxWidth(),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Color(0xFF6B5FF8),
                        unfocusedBorderColor = Color.Gray
                    )
                )

                Spacer(modifier = Modifier.height(12.dp))

                OutlinedTextField(
                    value = treatment,
                    onValueChange = { treatment = it },
                    label = { Text("Treatment", fontFamily = rubikFontFamily) },
                    modifier = Modifier.fillMaxWidth(),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Color(0xFF6B5FF8),
                        unfocusedBorderColor = Color.Gray
                    )
                )

                Spacer(modifier = Modifier.height(12.dp))

                OutlinedTextField(
                    value = notes,
                    onValueChange = { notes = it },
                    label = { Text("Notes", fontFamily = rubikFontFamily) },
                    modifier = Modifier.fillMaxWidth(),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Color(0xFF6B5FF8),
                        unfocusedBorderColor = Color.Gray
                    )
                )

                Spacer(modifier = Modifier.height(20.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Button(
                        onClick = onDismiss,
                        colors = ButtonDefaults.buttonColors(containerColor = Color.Gray),
                        modifier = Modifier.weight(1f)
                    ) {
                        Text("Cancel", color = Color.White, fontFamily = rubikFontFamily)
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Button(
                        onClick = {
                            onSubmit(diagnosis.text, treatment.text, notes.text)
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF6B5FF8)),
                        modifier = Modifier.weight(1f)
                    ) {
                        Text("Submit", color = Color.White, fontFamily = rubikFontFamily)
                    }
                }
            }
        }
    }
}

@Composable
fun CreatePrescriptionDialog(
    onDismiss: () -> Unit,
    onSubmit: (String?, List<MedicationEntry>) -> Unit,
    rubikFontFamily: FontFamily
) {
    var appointmentId by remember { mutableStateOf(TextFieldValue("")) }
    var medications by remember { mutableStateOf(listOf<MedicationEntry>()) }
    var medName by remember { mutableStateOf(TextFieldValue("")) }
    var medDosage by remember { mutableStateOf(TextFieldValue("")) }
    var medFrequency by remember { mutableStateOf(TextFieldValue("")) }
    var medDescription by remember { mutableStateOf(TextFieldValue("")) }
    var medPrice by remember { mutableStateOf(TextFieldValue("")) }
    var showAddMedication by remember { mutableStateOf(false) }

    Dialog(onDismissRequest = onDismiss) {
        Surface(
            shape = RoundedCornerShape(16.dp),
            color = Color.White,
            modifier = Modifier
                .padding(16.dp)
                .width(300.dp)
        ) {
            Column(
                modifier = Modifier
                    .padding(24.dp)
                    .fillMaxWidth()
            ) {
                Text(
                    text = "Add Prescription",
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = rubikFontFamily,
                    modifier = Modifier.padding(bottom = 16.dp),
                    color = Color(0xFF6B5FF8)
                )

                OutlinedTextField(
                    value = appointmentId,
                    onValueChange = { appointmentId = it },
                    label = { Text("Appointment ID (optional)", fontFamily = rubikFontFamily) },
                    modifier = Modifier.fillMaxWidth(),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Color(0xFF6B5FF8),
                        unfocusedBorderColor = Color.Gray
                    )
                )

                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    text = "Medications",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = rubikFontFamily
                )

                LazyColumn {
                    items(medications) { med ->
                        Text(
                            text = "${med.name} - ${med.dosage} (${med.frequency})",
                            fontFamily = rubikFontFamily,
                            modifier = Modifier.padding(vertical = 4.dp)
                        )
                    }
                }

                if (showAddMedication) {
                    OutlinedTextField(
                        value = medName,
                        onValueChange = { medName = it },
                        label = { Text("Name", fontFamily = rubikFontFamily) },
                        modifier = Modifier.fillMaxWidth(),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = Color(0xFF6B5FF8),
                            unfocusedBorderColor = Color.Gray
                        )
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    OutlinedTextField(
                        value = medDosage,
                        onValueChange = { medDosage = it },
                        label = { Text("Dosage", fontFamily = rubikFontFamily) },
                        modifier = Modifier.fillMaxWidth(),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = Color(0xFF6B5FF8),
                            unfocusedBorderColor = Color.Gray
                        )
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    OutlinedTextField(
                        value = medFrequency,
                        onValueChange = { medFrequency = it },
                        label = { Text("Frequency", fontFamily = rubikFontFamily) },
                        modifier = Modifier.fillMaxWidth(),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = Color(0xFF6B5FF8),
                            unfocusedBorderColor = Color.Gray
                        )
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    OutlinedTextField(
                        value = medDescription,
                        onValueChange = { medDescription = it },
                        label = { Text("Description", fontFamily = rubikFontFamily) },
                        modifier = Modifier.fillMaxWidth(),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = Color(0xFF6B5FF8),
                            unfocusedBorderColor = Color.Gray
                        )
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    OutlinedTextField(
                        value = medPrice,
                        onValueChange = { medPrice = it },
                        label = { Text("Price", fontFamily = rubikFontFamily) },
                        modifier = Modifier.fillMaxWidth(),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = Color(0xFF6B5FF8),
                            unfocusedBorderColor = Color.Gray
                        )
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Row {
                        Button(
                            onClick = {
                                val newMed = MedicationEntry(
                                    name = medName.text,
                                    dosage = medDosage.text,
                                    frequency = medFrequency.text,
                                    description = medDescription.text,
                                    price = medPrice.text.toDoubleOrNull()
                                )
                                medications = medications + newMed
                                showAddMedication = false
                                medName = TextFieldValue("")
                                medDosage = TextFieldValue("")
                                medFrequency = TextFieldValue("")
                                medDescription = TextFieldValue("")
                                medPrice = TextFieldValue("")
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF6B5FF8))
                        ) {
                            Text("Add Medication", color = Color.White, fontFamily = rubikFontFamily)
                        }
                        Spacer(modifier = Modifier.width(8.dp))
                        Button(
                            onClick = { showAddMedication = false },
                            colors = ButtonDefaults.buttonColors(containerColor = Color.Gray)
                        ) {
                            Text("Cancel", color = Color.White, fontFamily = rubikFontFamily)
                        }
                    }
                } else {
                    Button(
                        onClick = { showAddMedication = true },
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF6B5FF8))
                    ) {
                        Text("Add New Medication", color = Color.White, fontFamily = rubikFontFamily)
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Button(
                        onClick = onDismiss,
                        colors = ButtonDefaults.buttonColors(containerColor = Color.Gray),
                        modifier = Modifier.weight(1f)
                    ) {
                        Text("Cancel", color = Color.White, fontFamily = rubikFontFamily)
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Button(
                        onClick = {
                            onSubmit(
                                if (appointmentId.text.isEmpty()) null else appointmentId.text,
                                medications
                            )
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF6B5FF8)),
                        modifier = Modifier.weight(1f)
                    ) {
                        Text("Submit", color = Color.White, fontFamily = rubikFontFamily)
                    }
                }
            }
        }
    }
}

@Composable
private fun formatDate(dateStr: String?): String {
    return if (dateStr != null) {
        try {
            val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
            sdf.format(Date(dateStr.toLong()))
        } catch (e: Exception) {
            dateStr
        }
    } else "N/A"
}