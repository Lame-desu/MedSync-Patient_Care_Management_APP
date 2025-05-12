package com.example.myapplication.presentation.screens.triage

import android.util.Log
import android.widget.Toast
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
import androidx.compose.ui.platform.LocalContext
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
import com.example.myapplication.data.repository.triage.TriagePatient
import com.example.myapplication.data.repository.triage.TriageRepository
import kotlinx.coroutines.launch
import java.net.URLEncoder

@Composable
fun TriageHomeScreen(
    navController: NavHostController,
    authPreferences: AuthPreferences
) {
    val rubikFontFamily = FontFamily(
        Font(R.font.rubik_regular, FontWeight.Normal),
        Font(R.font.rubik_medium, FontWeight.Medium),
        Font(R.font.rubik_bold, FontWeight.Bold)
    )
    val context = LocalContext.current
    val viewModel: TriageHomeViewModel = viewModel {
        TriageHomeViewModel(TriageRepository(authPreferences, NetworkProvider.triageApi))
    }
    val triageState by viewModel.triageState.collectAsState()
    val searchQuery by viewModel.searchQuery.collectAsState()
    val coroutineScope = rememberCoroutineScope()
    var showSettingsPopup by remember { mutableStateOf(false) }

    Scaffold(
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White)
                .padding(innerPadding)
                .padding(16.dp)
        ) {
            // Top Section
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "Hi, Triage Staff",
                        fontSize = 14.sp,
                        color = Color.Gray,
                        fontFamily = rubikFontFamily
                    )
                    Text(
                        text = "Triage Dashboard",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        fontFamily = rubikFontFamily,
                        color = Color(0xFF6B5FF8)
                    )
                }
                Icon(
                    painter = painterResource(id = R.drawable.cog_outline),
                    contentDescription = "Settings",
                    modifier = Modifier
                        .size(24.dp)
                        .clickable { showSettingsPopup = true },
                    tint = Color(0xFF6B5FF8)
                )
            }

            // Search Bar
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { viewModel.updateSearchQuery(it) },
                label = { Text("Search Patients", fontFamily = rubikFontFamily) },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp),
                leadingIcon = {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_medical2),
                        contentDescription = "Search Icon",
                        tint = Color(0xFF6B5FF8)
                    )
                },
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = Color.White,
                    unfocusedContainerColor = Color.White,
                    focusedIndicatorColor = Color(0xFF6B5FF8),
                    unfocusedIndicatorColor = Color.Gray
                )
            )

            // Patient List
            when (val state = triageState) {
                is TriageHomeViewModel.TriageState.Loading -> {
                    CircularProgressIndicator(modifier = Modifier.padding(16.dp))
                }
                is TriageHomeViewModel.TriageState.Success -> {
                    if (state.patients.isEmpty()) {
                        Text(
                            text = "No patients found",
                            fontFamily = rubikFontFamily,
                            fontSize = 16.sp,
                            color = Color.Gray,
                            modifier = Modifier.padding(16.dp)
                        )
                    } else {
                        LazyColumn {
                            items(state.patients) { patient ->
                                PatientCard(
                                    patient = patient,
                                    rubikFontFamily = rubikFontFamily,
                                    onClick = {
                                        val encodedPatientId = URLEncoder.encode(patient.id, "UTF-8")
                                        Log.d("TriageHomeScreen", "Navigating to triage_patient_details/$encodedPatientId")
                                        navController.navigate("triage_patient_details/$encodedPatientId")
                                    }
                                )
                                Spacer(modifier = Modifier.height(8.dp))
                            }
                        }
                    }
                }
                is TriageHomeViewModel.TriageState.Error -> {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = state.message,
                            fontFamily = rubikFontFamily,
                            fontSize = 16.sp,
                            color = Color.Red,
                            modifier = Modifier.padding(bottom = 16.dp)
                        )
                        Button(
                            onClick = { viewModel.fetchPatients() },
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF6B5FF8)),
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Text(
                                text = "Retry",
                                color = Color.White,
                                fontFamily = rubikFontFamily,
                                fontSize = 16.sp
                            )
                        }
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
                        fontFamily = rubikFontFamily,
                        modifier = Modifier.clickable {
                            coroutineScope.launch {
                                authPreferences.clearAuthData()
                                Toast.makeText(context, "Logged out", Toast.LENGTH_SHORT).show()
                                navController.navigate("login") {
                                    popUpTo("triage_home") { inclusive = true }
                                    launchSingleTop = true
                                }
                                showSettingsPopup = false
                            }
                        }
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Button(
                        onClick = { showSettingsPopup = false },
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF6B5FF8)),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text(
                            text = "Close",
                            color = Color.White,
                            fontFamily = rubikFontFamily
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun PatientCard(
    patient: TriagePatient,
    rubikFontFamily: FontFamily,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .background(Color(0xFF6B5FF8), RoundedCornerShape(16.dp)),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF6B5FF8)),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = painterResource(id = R.drawable.ic_patient),
                contentDescription = "Patient Image",
                modifier = Modifier
                    .size(48.dp)
                    .clip(CircleShape)
                    .background(Color.White)
            )
            Spacer(modifier = Modifier.width(16.dp))
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = patient.name,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = rubikFontFamily,
                    color = Color.White
                )
            }
            Button(
                onClick = onClick,
                colors = ButtonDefaults.buttonColors(containerColor = Color.White),
                shape = RoundedCornerShape(8.dp)
            ) {
                Text(
                    text = "View",
                    color = Color(0xFF6B5FF8),
                    fontFamily = rubikFontFamily,
                    fontWeight = FontWeight.Medium
                )
            }
        }
    }
}