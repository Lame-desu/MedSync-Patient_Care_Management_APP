package com.example.myapplication.presentation.screens.doctor

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
import java.util.*

@Composable
fun MedicalRecordDetailsScreen(
    navController: NavHostController,
    recordId: String,
    authPreferences: AuthPreferences
) {
    val TAG = "MedicalRecordDetailsScreen"
    Log.d(TAG, "Composing MedicalRecordDetailsScreen for recordId: $recordId")
    val rubikFontFamily = FontFamily(
        Font(R.font.rubik_regular, FontWeight.Normal),
        Font(R.font.rubik_medium, FontWeight.Medium),
        Font(R.font.rubik_bold, FontWeight.Bold)
    )
    val doctorApi = NetworkProvider.doctorApi
    val viewModel: MedicalRecordDetailsViewModel = viewModel(
        factory = MedicalRecordDetailsViewModelFactory(DoctorRepository(authPreferences, doctorApi))
    )
    Log.d(TAG, "ViewModel initialized: $viewModel")

    LaunchedEffect(recordId) {
        viewModel.fetchMedicalRecordDetails(recordId)
    }

    val recordState by viewModel.recordState.collectAsState()
    var showUpdateDialog by remember { mutableStateOf(false) }
    var showDeleteDialog by remember { mutableStateOf(false) }
    val snackbarHostState = remember { SnackbarHostState() }
    var showSuccessMessage by remember { mutableStateOf(false) }

    // Show snackbar and navigate when showSuccessMessage is true
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
                    text = "Medical Record Details",
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
                currentRoute = "medical_record_details"
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
            when (val state = recordState) {
                is MedicalRecordDetailsViewModel.RecordState.Loading -> {
                    CircularProgressIndicator(modifier = Modifier.padding(16.dp))
                }
                is MedicalRecordDetailsViewModel.RecordState.Success -> {
                    val record = state.record
                    Card(
                        shape = RoundedCornerShape(16.dp),
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(containerColor = Color.White)
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text(
                                text = "Diagnosis: ${record.diagnosis ?: "N/A"}",
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold,
                                fontFamily = rubikFontFamily
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = "Treatment: ${record.treatment ?: "N/A"}",
                                fontSize = 16.sp,
                                fontFamily = rubikFontFamily
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = "Notes: ${record.notes ?: "N/A"}",
                                fontSize = 16.sp,
                                fontFamily = rubikFontFamily
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = "Last Updated: ${formatDate(record.lastUpdated)}",
                                fontSize = 14.sp,
                                color = Color.Gray,
                                fontFamily = rubikFontFamily
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = "Doctor: ${record.doctorInfo.name} (${record.doctorInfo.specialty ?: "N/A"})",
                                fontSize = 14.sp,
                                color = Color.Gray,
                                fontFamily = rubikFontFamily
                            )
                            Text(
                                text = "Patient: ${record.patientInfo.name}",
                                fontSize = 14.sp,
                                color = Color.Gray,
                                fontFamily = rubikFontFamily
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        Button(
                            onClick = { showUpdateDialog = true },
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2196F3)),
                            modifier = Modifier.weight(1f)
                        ) {
                            Text("Update", color = Color.White, fontFamily = rubikFontFamily)
                        }
                        Spacer(modifier = Modifier.width(8.dp))
                        Button(
                            onClick = { showDeleteDialog = true },
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFF44336)),
                            modifier = Modifier.weight(1f)
                        ) {
                            Text("Delete", color = Color.White, fontFamily = rubikFontFamily)
                        }
                    }
                }
                is MedicalRecordDetailsViewModel.RecordState.Error -> {
                    Text(
                        text = state.message,
                        fontFamily = rubikFontFamily,
                        fontSize = 16.sp,
                        color = Color.Red,
                        modifier = Modifier.padding(16.dp)
                    )
                }
            }

            if (showUpdateDialog) {
                UpdateMedicalRecordDialog(
                    onDismiss = { showUpdateDialog = false },
                    onSubmit = { diagnosis, treatment, notes ->
                        viewModel.updateMedicalRecord(recordId, diagnosis, treatment, notes)
                        showUpdateDialog = false
                        showSuccessMessage = true
                    },
                    initialDiagnosis = (recordState as? MedicalRecordDetailsViewModel.RecordState.Success)?.record?.diagnosis ?: "",
                    initialTreatment = (recordState as? MedicalRecordDetailsViewModel.RecordState.Success)?.record?.treatment ?: "",
                    initialNotes = (recordState as? MedicalRecordDetailsViewModel.RecordState.Success)?.record?.notes ?: "",
                    rubikFontFamily = rubikFontFamily
                )
            }

            if (showDeleteDialog) {
                DeleteMedicalRecordDialog(
                    onDismiss = { showDeleteDialog = false },
                    onConfirm = {
                        viewModel.deleteMedicalRecord(recordId)
                        showDeleteDialog = false
                        showSuccessMessage = true
                    },
                    rubikFontFamily = rubikFontFamily
                )
            }
        }
    }
}

@Composable
fun UpdateMedicalRecordDialog(
    onDismiss: () -> Unit,
    onSubmit: (String, String, String) -> Unit,
    initialDiagnosis: String,
    initialTreatment: String,
    initialNotes: String,
    rubikFontFamily: FontFamily
) {
    var diagnosis by remember { mutableStateOf(TextFieldValue(initialDiagnosis)) }
    var treatment by remember { mutableStateOf(TextFieldValue(initialTreatment)) }
    var notes by remember { mutableStateOf(TextFieldValue(initialNotes)) }

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
                    text = "Update Medical Record",
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = rubikFontFamily,
                    color = Color(0xFF2196F3),
                    modifier = Modifier.padding(bottom = 16.dp)
                )

                OutlinedTextField(
                    value = diagnosis,
                    onValueChange = { diagnosis = it },
                    label = { Text("Diagnosis", fontFamily = rubikFontFamily) },
                    modifier = Modifier.fillMaxWidth(),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Color(0xFF2196F3),
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
                        focusedBorderColor = Color(0xFF2196F3),
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
                        focusedBorderColor = Color(0xFF2196F3),
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
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2196F3)),
                        modifier = Modifier.weight(1f)
                    ) {
                        Text("Update", color = Color.White, fontFamily = rubikFontFamily)
                    }
                }
            }
        }
    }
}

@Composable
fun DeleteMedicalRecordDialog(
    onDismiss: () -> Unit,
    onConfirm: () -> Unit,
    rubikFontFamily: FontFamily
) {
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
                    text = "Delete Medical Record",
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = rubikFontFamily,
                    color = Color(0xFFF44336),
                    modifier = Modifier.padding(bottom = 16.dp)
                )

                Text(
                    text = "Are you sure you want to delete this medical record? This action cannot be undone.",
                    fontSize = 16.sp,
                    fontFamily = rubikFontFamily,
                    color = Color.Gray,
                    modifier = Modifier.padding(bottom = 16.dp)
                )

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
                        onClick = onConfirm,
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFF44336)),
                        modifier = Modifier.weight(1f)
                    ) {
                        Text("Delete", color = Color.White, fontFamily = rubikFontFamily)
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