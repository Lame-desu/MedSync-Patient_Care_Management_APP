//// In PrescriptionDetailsScreen.kt
//package com.example.myapplication.presentation.screens.doctor
//
//import android.util.Log
//import androidx.compose.foundation.background
//import androidx.compose.foundation.clickable
//import androidx.compose.foundation.layout.*
//import androidx.compose.foundation.lazy.LazyColumn
//import androidx.compose.foundation.lazy.items
//import androidx.compose.foundation.shape.RoundedCornerShape
//import androidx.compose.material3.*
//import androidx.compose.runtime.*
//import androidx.compose.ui.Alignment
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.graphics.Color
//import androidx.compose.ui.res.painterResource
//import androidx.compose.ui.text.font.Font
//import androidx.compose.ui.text.font.FontFamily
//import androidx.compose.ui.text.font.FontWeight
//import androidx.compose.ui.text.input.TextFieldValue
//import androidx.compose.ui.unit.dp
//import androidx.compose.ui.unit.sp
//import androidx.compose.ui.window.Dialog
//import androidx.lifecycle.viewmodel.compose.viewModel
//import androidx.navigation.NavHostController
//import com.example.myapplication.R
//import com.example.myapplication.data.model.AuthPreferences
//import com.example.myapplication.data.model.doctor.*
//import com.example.myapplication.data.remote.NetworkProvider
//import com.example.myapplication.data.repository.doctor.DoctorRepository
//import com.example.myapplication.navigation.DoctorBottomNavBar
//import com.example.myapplication.presentation.screens.patient.formatDate
//
//@Composable
//fun PrescriptionDetailsScreen(
//    navController: NavHostController,
//    prescriptionId: String,
//    authPreferences: AuthPreferences
//) {
//    val TAG = "PrescriptionDetailsScreen"
//    Log.d(TAG, "Composing PrescriptionDetailsScreen for prescriptionId: $prescriptionId")
//    val rubikFontFamily = FontFamily(
//        Font(R.font.rubik_regular, FontWeight.Normal),
//        Font(R.font.rubik_medium, FontWeight.Medium),
//        Font(R.font.rubik_bold, FontWeight.Bold)
//    )
//    val doctorApi = NetworkProvider.doctorApi
//    val viewModel: PrescriptionDetailsViewModel = viewModel(
//        factory = PrescriptionDetailsViewModelFactory(DoctorRepository(authPreferences, doctorApi), prescriptionId)
//    )
//    Log.d(TAG, "ViewModel initialized: $viewModel")
//
//    LaunchedEffect(prescriptionId) {
//        viewModel.fetchPrescriptionDetails()
//    }
//
//    val prescriptionState by viewModel.prescriptionState.collectAsState()
//    var showUpdateDialog by remember { mutableStateOf(false) }
//    var showDeleteDialog by remember { mutableStateOf(false) }
//    val snackbarHostState = remember { SnackbarHostState() }
//    var showSuccessMessage by remember { mutableStateOf(false) }
//
//    LaunchedEffect(showSuccessMessage) {
//        if (showSuccessMessage) {
//            snackbarHostState.showSnackbar("Successful")
//            navController.navigate("doctor_dashboard/{name}") {
//                popUpTo(navController.graph.startDestinationId) { inclusive = true }
//            }
//            showSuccessMessage = false
//        }
//    }
//
//    Scaffold(
//        topBar = {
//            Row(
//                modifier = Modifier
//                    .fillMaxWidth()
//                    .background(Color(0xFFD8C4E7))
//                    .padding(16.dp),
//                verticalAlignment = Alignment.CenterVertically
//            ) {
//                Icon(
//                    painter = painterResource(id = R.drawable.bell),
//                    contentDescription = "Back",
//                    modifier = Modifier
//                        .size(24.dp)
//                        .clickable { navController.navigateUp() },
//                    tint = Color.Black
//                )
//                Spacer(modifier = Modifier.width(16.dp))
//                Text(
//                    text = "Prescription Details",
//                    fontSize = 20.sp,
//                    fontWeight = FontWeight.Bold,
//                    fontFamily = rubikFontFamily,
//                    color = Color.Black
//                )
//            }
//        },
//        bottomBar = {
//            DoctorBottomNavBar(
//                navController = navController,
//                authPreferences = authPreferences,
//                currentRoute = "prescription_details"
//            )
//        },
//        snackbarHost = {
//            SnackbarHost(
//                hostState = snackbarHostState,
//                modifier = Modifier.padding(16.dp)
//            )
//        }
//    ) { innerPadding ->
//        Column(
//            modifier = Modifier
//                .fillMaxSize()
//                .background(Color(0xFFD8C4E7))
//                .padding(innerPadding)
//                .padding(16.dp)
//        ) {
//            when (val state = prescriptionState) {
//                is PrescriptionDetailsViewModel.PrescriptionState.Loading -> {
//                    CircularProgressIndicator(modifier = Modifier.padding(16.dp))
//                }
//                is PrescriptionDetailsViewModel.PrescriptionState.Success -> {
//                    val prescription = state.prescription
//                    Card(
//                        shape = RoundedCornerShape(16.dp),
//                        modifier = Modifier.fillMaxWidth(),
//                        colors = CardDefaults.cardColors(containerColor = Color.White)
//                    ) {
//                        Column(modifier = Modifier.padding(16.dp)) {
//                            Text(
//                                text = "Appointment ID: ${prescription.appointmentId}",
//                                fontSize = 18.sp,
//                                fontWeight = FontWeight.Bold,
//                                fontFamily = rubikFontFamily
//                            )
//                            Spacer(modifier = Modifier.height(8.dp))
//                            Text(
//                                text = "Patient: ${prescription.patientInfo.name}",
//                                fontSize = 16.sp,
//                                fontFamily = rubikFontFamily
//                            )
//                            Spacer(modifier = Modifier.height(8.dp))
//                            Text(
//                                text = "Doctor: ${prescription.doctorInfo.name}",
//                                fontSize = 16.sp,
//                                fontFamily = rubikFontFamily
//                            )
//                            Spacer(modifier = Modifier.height(8.dp))
//                            Text(
//                                text = "Medications:",
//                                fontSize = 16.sp,
//                                fontWeight = FontWeight.Bold,
//                                fontFamily = rubikFontFamily
//                            )
//                            LazyColumn {
//                                items(prescription.medications ?: emptyList()) { med ->
//                                    Text(
//                                        text = "${med.name} - ${med.dosage} (${med.frequency}), Desc: ${med.description}, Price: $${med.price ?: 0.0}",
//                                        fontSize = 14.sp,
//                                        fontFamily = rubikFontFamily,
//                                        modifier = Modifier.padding(vertical = 4.dp)
//                                    )
//                                }
//                            }
//                            Spacer(modifier = Modifier.height(8.dp))
//                            Text(
//                                text = "Created: ${formatDate(prescription.createdAt)}",
//                                fontSize = 14.sp,
//                                color = Color.Gray,
//                                fontFamily = rubikFontFamily
//                            )
//                        }
//                    }
//
//                    Spacer(modifier = Modifier.height(16.dp))
//
//                    Row(
//                        modifier = Modifier.fillMaxWidth(),
//                        horizontalArrangement = Arrangement.SpaceEvenly
//                    ) {
//                        Button(
//                            onClick = { showUpdateDialog = true },
//                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2196F3)),
//                            modifier = Modifier.weight(1f)
//                        ) {
//                            Text("Update", color = Color.White, fontFamily = rubikFontFamily)
//                        }
//                        Spacer(modifier = Modifier.width(8.dp))
//                        Button(
//                            onClick = { showDeleteDialog = true },
//                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFF44336)),
//                            modifier = Modifier.weight(1f)
//                        ) {
//                            Text("Delete", color = Color.White, fontFamily = rubikFontFamily)
//                        }
//                    }
//                }
//                is PrescriptionDetailsViewModel.PrescriptionState.Error -> {
//                    Text(
//                        text = state.message,
//                        fontFamily = rubikFontFamily,
//                        fontSize = 16.sp,
//                        color = Color.Red,
//                        modifier = Modifier.padding(16.dp)
//                    )
//                }
//            }
//
//            if (showUpdateDialog) {
//                UpdatePrescriptionDialog(
//                    onDismiss = { showUpdateDialog = false },
//                    onSubmit = { medications ->
//                        viewModel.updatePrescription(medications)
//                        showUpdateDialog = false
//                        showSuccessMessage = true
//                    },
//                    initialMedications = (prescriptionState as? PrescriptionDetailsViewModel.PrescriptionState.Success)?.prescription?.medications ?: emptyList(),
//                    rubikFontFamily = rubikFontFamily
//                )
//            }
//
//            if (showDeleteDialog) {
//                DeletePrescriptionDialog(
//                    onDismiss = { showDeleteDialog = false },
//                    onConfirm = {
//                        viewModel.deletePrescription()
//                        showDeleteDialog = false
//                        showSuccessMessage = true
//                    },
//                    rubikFontFamily = rubikFontFamily
//                )
//            }
//        }
//    }
//}
//
//@Composable
//fun UpdatePrescriptionDialog(
//    onDismiss: () -> Unit,
//    onSubmit: (List<MedicationEntry>) -> Unit,
//    initialMedications: List<MedicationEntry>,
//    rubikFontFamily: FontFamily
//) {
//    var medications by remember { mutableStateOf(initialMedications) }
//    var medName by remember { mutableStateOf(TextFieldValue("")) }
//    var medDosage by remember { mutableStateOf(TextFieldValue("")) }
//    var medFrequency by remember { mutableStateOf(TextFieldValue("")) }
//    var medDescription by remember { mutableStateOf(TextFieldValue("")) }
//    var medPrice by remember { mutableStateOf(TextFieldValue("")) }
//    var showAddMedication by remember { mutableStateOf(false) }
//
//    Dialog(onDismissRequest = onDismiss) {
//        Surface(
//            shape = RoundedCornerShape(16.dp),
//            color = Color.White,
//            modifier = Modifier
//                .padding(16.dp)
//                .width(300.dp)
//        ) {
//            Column(
//                modifier = Modifier
//                    .padding(24.dp)
//                    .fillMaxWidth()
//            ) {
//                Text(
//                    text = "Update Prescription",
//                    fontSize = 22.sp,
//                    fontWeight = FontWeight.Bold,
//                    fontFamily = rubikFontFamily,
//                    color = Color(0xFF2196F3),
//                    modifier = Modifier.padding(bottom = 16.dp)
//                )
//
//                LazyColumn {
//                    items(medications) { med ->
//                        Text(
//                            text = "${med.name} - ${med.dosage} (${med.frequency})",
//                            fontFamily = rubikFontFamily,
//                            modifier = Modifier.padding(vertical = 4.dp)
//                        )
//                    }
//                }
//
//                if (showAddMedication) {
//                    OutlinedTextField(
//                        value = medName,
//                        onValueChange = { medName = it },
//                        label = { Text("Name", fontFamily = rubikFontFamily) },
//                        modifier = Modifier.fillMaxWidth()
//                    )
//                    Spacer(modifier = Modifier.height(8.dp))
//                    OutlinedTextField(
//                        value = medDosage,
//                        onValueChange = { medDosage = it },
//                        label = { Text("Dosage", fontFamily = rubikFontFamily) },
//                        modifier = Modifier.fillMaxWidth()
//                    )
//                    Spacer(modifier = Modifier.height(8.dp))
//                    OutlinedTextField(
//                        value = medFrequency,
//                        onValueChange = { medFrequency = it },
//                        label = { Text("Frequency", fontFamily = rubikFontFamily) },
//                        modifier = Modifier.fillMaxWidth()
//                    )
//                    Spacer(modifier = Modifier.height(8.dp))
//                    OutlinedTextField(
//                        value = medDescription,
//                        onValueChange = { medDescription = it },
//                        label = { Text("Description", fontFamily = rubikFontFamily) },
//                        modifier = Modifier.fillMaxWidth()
//                    )
//                    Spacer(modifier = Modifier.height(8.dp))
//                    OutlinedTextField(
//                        value = medPrice,
//                        onValueChange = { medPrice = it },
//                        label = { Text("Price", fontFamily = rubikFontFamily) },
//                        modifier = Modifier.fillMaxWidth()
//                    )
//                    Spacer(modifier = Modifier.height(8.dp))
//                    Row {
//                        Button(
//                            onClick = {
//                                val newMed = MedicationEntry(
//                                    name = medName.text,
//                                    dosage = medDosage.text,
//                                    frequency = medFrequency.text,
//                                    description = medDescription.text,
//                                    price = medPrice.text.toDoubleOrNull()
//                                )
//                                medications = medications + newMed
//                                showAddMedication = false
//                                medName = TextFieldValue("")
//                                medDosage = TextFieldValue("")
//                                medFrequency = TextFieldValue("")
//                                medDescription = TextFieldValue("")
//                                medPrice = TextFieldValue("")
//                            },
//                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2196F3))
//                        ) {
//                            Text("Add Medication", color = Color.White, fontFamily = rubikFontFamily)
//                        }
//                        Spacer(modifier = Modifier.width(8.dp))
//                        Button(
//                            onClick = { showAddMedication = false },
//                            colors = ButtonDefaults.buttonColors(containerColor = Color.Gray)
//                        ) {
//                            Text("Cancel", color = Color.White, fontFamily = rubikFontFamily)
//                        }
//                    }
//                } else {
//                    Button(
//                        onClick = { showAddMedication = true },
//                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2196F3))
//                    ) {
//                        Text("Add New Medication", color = Color.White, fontFamily = rubikFontFamily)
//                    }
//                }
//
//                Spacer(modifier = Modifier.height(20.dp))
//
//                Row(
//                    modifier = Modifier.fillMaxWidth(),
//                    horizontalArrangement = Arrangement.SpaceBetween
//                ) {
//                    Button(
//                        onClick = onDismiss,
//                        colors = ButtonDefaults.buttonColors(containerColor = Color.Gray),
//                        modifier = Modifier.weight(1f)
//                    ) {
//                        Text("Cancel", color = Color.White, fontFamily = rubikFontFamily)
//                    }
//                    Spacer(modifier = Modifier.width(8.dp))
//                    Button(
//                        onClick = {
//                            onSubmit(medications)
//                        },
//                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2196F3)),
//                        modifier = Modifier.weight(1f)
//                    ) {
//                        Text("Update", color = Color.White, fontFamily = rubikFontFamily)
//                    }
//                }
//            }
//        }
//    }
//}
//
//@Composable
//fun DeletePrescriptionDialog(
//    onDismiss: () -> Unit,
//    onConfirm: () -> Unit,
//    rubikFontFamily: FontFamily
//) {
//    Dialog(onDismissRequest = onDismiss) {
//        Surface(
//            shape = RoundedCornerShape(16.dp),
//            color = Color.White,
//            modifier = Modifier
//                .padding(16.dp)
//                .width(300.dp)
//        ) {
//            Column(
//                modifier = Modifier
//                    .padding(24.dp)
//                    .fillMaxWidth()
//            ) {
//                Text(
//                    text = "Delete Prescription",
//                    fontSize = 22.sp,
//                    fontWeight = FontWeight.Bold,
//                    fontFamily = rubikFontFamily,
//                    color = Color(0xFFF44336),
//                    modifier = Modifier.padding(bottom = 16.dp)
//                )
//
//                Text(
//                    text = "Are you sure you want to delete this prescription? This action cannot be undone.",
//                    fontSize = 16.sp,
//                    fontFamily = rubikFontFamily,
//                    color = Color.Gray,
//                    modifier = Modifier.padding(bottom = 16.dp)
//                )
//
//                Row(
//                    modifier = Modifier.fillMaxWidth(),
//                    horizontalArrangement = Arrangement.SpaceBetween
//                ) {
//                    Button(
//                        onClick = onDismiss,
//                        colors = ButtonDefaults.buttonColors(containerColor = Color.Gray),
//                        modifier = Modifier.weight(1f)
//                    ) {
//                        Text("Cancel", color = Color.White, fontFamily = rubikFontFamily)
//                    }
//                    Spacer(modifier = Modifier.width(8.dp))
//                    Button(
//                        onClick = onConfirm,
//                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFF44336)),
//                        modifier = Modifier.weight(1f)
//                    ) {
//                        Text("Delete", color = Color.White, fontFamily = rubikFontFamily)
//                    }
//                }
//            }
//        }
//    }
//}


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
fun PrescriptionDetailsScreen(
    navController: NavHostController,
    prescriptionId: String,
    authPreferences: AuthPreferences
) {
    val TAG = "PrescriptionDetailsScreen"
    Log.d(TAG, "Composing PrescriptionDetailsScreen for prescriptionId: $prescriptionId")
    val rubikFontFamily = FontFamily(
        Font(R.font.rubik_regular, FontWeight.Normal),
        Font(R.font.rubik_medium, FontWeight.Medium),
        Font(R.font.rubik_bold, FontWeight.Bold)
    )
    val doctorApi = NetworkProvider.doctorApi
    val viewModel: PrescriptionDetailsViewModel = viewModel(
        factory = PrescriptionDetailsViewModelFactory(DoctorRepository(authPreferences, doctorApi), prescriptionId)
    )
    Log.d(TAG, "ViewModel initialized: $viewModel")

    LaunchedEffect(prescriptionId) {
        viewModel.fetchPrescriptionDetails()
    }

    val prescriptionState by viewModel.prescriptionState.collectAsState()
    var showUpdateDialog by remember { mutableStateOf(false) }
    var showDeleteDialog by remember { mutableStateOf(false) }
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
                    .background(Color(0xFFD8C4E7))
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.bell),
                    contentDescription = "Back",
                    modifier = Modifier
                        .size(24.dp)
                        .clickable { navController.navigateUp() },
                    tint = Color.Black
                )
                Spacer(modifier = Modifier.width(16.dp))
                Text(
                    text = "Prescription Details",
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
                currentRoute = "prescription_details"
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
                .background(Color(0xFFD8C4E7))
                .padding(innerPadding)
                .padding(16.dp)
        ) {
            when (val state = prescriptionState) {
                is PrescriptionDetailsViewModel.PrescriptionState.Loading -> {
                    CircularProgressIndicator(modifier = Modifier.padding(16.dp))
                }
                is PrescriptionDetailsViewModel.PrescriptionState.Success -> {
                    val prescription = state.prescription
                    Card(
                        shape = RoundedCornerShape(16.dp),
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(containerColor = Color.White)
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text(
                                text = "Appointment ID: ${prescription.appointmentId ?: "N/A"}",
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold,
                                fontFamily = rubikFontFamily
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = "Patient: ${prescription.patientInfo?.name ?: "N/A"}",
                                fontSize = 16.sp,
                                fontFamily = rubikFontFamily
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = "Doctor: ${prescription.doctorInfo?.name ?: "N/A"}",
                                fontSize = 16.sp,
                                fontFamily = rubikFontFamily
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = "Medications:",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                                fontFamily = rubikFontFamily
                            )
                            LazyColumn {
                                items(prescription.medications ?: emptyList()) { med ->
                                    Text(
                                        text = "${med.name ?: "N/A"} - ${med.dosage ?: "N/A"} (${med.frequency ?: "N/A"}), Desc: ${med.description ?: "N/A"}, Price: $${med.price ?: 0.0}",
                                        fontSize = 14.sp,
                                        fontFamily = rubikFontFamily,
                                        modifier = Modifier.padding(vertical = 4.dp)
                                    )
                                }
                            }
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = "Created: ${formatDate(prescription.createdAt)}",
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
                is PrescriptionDetailsViewModel.PrescriptionState.Error -> {
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
                UpdatePrescriptionDialog(
                    onDismiss = { showUpdateDialog = false },
                    onSubmit = { medications ->
                        viewModel.updatePrescription(medications)
                        showUpdateDialog = false
                        showSuccessMessage = true
                    },
                    initialMedications = (prescriptionState as? PrescriptionDetailsViewModel.PrescriptionState.Success)?.prescription?.medications ?: emptyList(),
                    rubikFontFamily = rubikFontFamily
                )
            }

            if (showDeleteDialog) {
                DeletePrescriptionDialog(
                    onDismiss = { showDeleteDialog = false },
                    onConfirm = {
                        viewModel.deletePrescription()
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
fun UpdatePrescriptionDialog(
    onDismiss: () -> Unit,
    onSubmit: (List<MedicationEntry>) -> Unit,
    initialMedications: List<MedicationEntry>,
    rubikFontFamily: FontFamily
) {
    var medications by remember { mutableStateOf(initialMedications) }
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
                    text = "Update Prescription",
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = rubikFontFamily,
                    color = Color(0xFF2196F3),
                    modifier = Modifier.padding(bottom = 16.dp)
                )

                LazyColumn {
                    items(medications) { med ->
                        Text(
                            text = "${med.name ?: "N/A"} - ${med.dosage ?: "N/A"} (${med.frequency ?: "N/A"})",
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
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    OutlinedTextField(
                        value = medDosage,
                        onValueChange = { medDosage = it },
                        label = { Text("Dosage", fontFamily = rubikFontFamily) },
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    OutlinedTextField(
                        value = medFrequency,
                        onValueChange = { medFrequency = it },
                        label = { Text("Frequency", fontFamily = rubikFontFamily) },
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    OutlinedTextField(
                        value = medDescription,
                        onValueChange = { medDescription = it },
                        label = { Text("Description", fontFamily = rubikFontFamily) },
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    OutlinedTextField(
                        value = medPrice,
                        onValueChange = { medPrice = it },
                        label = { Text("Price", fontFamily = rubikFontFamily) },
                        modifier = Modifier.fillMaxWidth()
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
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2196F3))
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
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2196F3))
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
                            onSubmit(medications)
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
fun DeletePrescriptionDialog(
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
                    text = "Delete Prescription",
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = rubikFontFamily,
                    color = Color(0xFFF44336),
                    modifier = Modifier.padding(bottom = 16.dp)
                )

                Text(
                    text = "Are you sure you want to delete this prescription? This action cannot be undone.",
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