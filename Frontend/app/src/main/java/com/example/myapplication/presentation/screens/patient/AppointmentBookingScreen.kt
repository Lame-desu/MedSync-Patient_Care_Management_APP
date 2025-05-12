package com.example.myapplication.presentation.screens.patient

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
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
import com.example.myapplication.data.remote.NetworkProvider
import com.example.myapplication.data.repository.triage.BookingRepository
import com.example.myapplication.navigation.BottomNavigationBar
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun AppointmentBookingScreen(navController: NavHostController, authPreferences: AuthPreferences) {
    val rubikFontFamily = FontFamily(
        Font(R.font.rubik_regular, FontWeight.Normal),
        Font(R.font.rubik_medium, FontWeight.Medium),
        Font(R.font.rubik_bold, FontWeight.Bold)
    )
    val context = LocalContext.current
    var selectedDate by remember { mutableStateOf("") }
    var selectedTime by remember { mutableStateOf("") }
    var selectedSpecialty by remember { mutableStateOf<String?>(null) }
    var hasNavigated by remember { mutableStateOf(false) }

    val viewModel: AppointmentBookingViewModel = viewModel {
        AppointmentBookingViewModel(BookingRepository(authPreferences, NetworkProvider.bookingApi))
    }
    val bookingState by viewModel.bookingState.collectAsState()

    LaunchedEffect(bookingState) {
        when (val state = bookingState) {
            is AppointmentBookingViewModel.BookingState.Success -> {
                if (hasNavigated) return@LaunchedEffect
                hasNavigated = true
                Toast.makeText(context, "Booking created successfully", Toast.LENGTH_SHORT).show()
                val name = authPreferences.getName() ?: "Guest"
                val encodedName = try {
                    java.net.URLEncoder.encode(name, "UTF-8").takeIf { it.isNotBlank() } ?: "Guest"
                } catch (e: Exception) {
                    Log.e("AppointmentBookingScreen", "Encoding failed: ${e.message}")
                    "Guest"
                }
                Log.d("AppointmentBookingScreen", "Encoded Name: $encodedName")

                try {
                    navController.navigate("patient_dashboard/$encodedName") {
                        popUpTo("appointment_booking") { inclusive = true }
                        launchSingleTop = true
                    }
                } catch (e: IllegalStateException) {
                    Log.e("AppointmentBookingScreen", "Navigation failed: ${e.message}")
                    Toast.makeText(context, "Navigation error: Try again", Toast.LENGTH_SHORT).show()
                    hasNavigated = false
                }
            }
            is AppointmentBookingViewModel.BookingState.Error -> {
                Toast.makeText(context, state.message, Toast.LENGTH_SHORT).show()
            }
            else -> Unit
        }
    }

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
                text = "Book\nAppointments",
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                fontFamily = rubikFontFamily,
                color = Color(0xFF6B5FF8),
                maxLines = 2,
                modifier = Modifier.align(Alignment.Start)
            )
            Spacer(modifier = Modifier.height(16.dp))

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "What are you looking for?",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium,
                    fontFamily = rubikFontFamily,
                    color = Color.Black
                )
                Text(
                    text = "See All",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium,
                    fontFamily = rubikFontFamily,
                    color = Color(0xFF6B5FF8),
                    modifier = Modifier
                        .clickable {
                            Toast.makeText(context, "See all clicked", Toast.LENGTH_SHORT).show()
                        }
                        .padding(8.dp)
                )
            }

            val specialties = listOf(
                Specialty("Pathologist", Color(0xFFBC439A), R.drawable.ic_medical2),
                Specialty("Ophthalmologist", Color(0xFFAFCB81), R.drawable.ic_medical2),
                Specialty("Dermatologist", Color(0xFFB385FF), R.drawable.ic_medical2),
                Specialty("Pediatrician", Color(0xFF3586A9), R.drawable.ic_medical2)
            )
            Column {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    specialties.take(2).forEach { specialty ->
                        SpecialtyCard(
                            specialty = specialty,
                            isSelected = selectedSpecialty == specialty.name,
                            onClick = { selectedSpecialty = if (selectedSpecialty == specialty.name) null else specialty.name },
                            modifier = Modifier
                                .weight(1f)
                                .padding(end = 8.dp)
                        )
                    }
                }
                Spacer(modifier = Modifier.height(8.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    specialties.drop(2).forEach { specialty ->
                        SpecialtyCard(
                            specialty = specialty,
                            isSelected = selectedSpecialty == specialty.name,
                            onClick = { selectedSpecialty = if (selectedSpecialty == specialty.name) null else specialty.name },
                            modifier = Modifier
                                .weight(1f)
                                .padding(end = 8.dp)
                        )
                    }
                }
            }
            Spacer(modifier = Modifier.height(24.dp))

            OutlinedTextField(
                value = selectedDate,
                onValueChange = {},
                label = { Text("Select Date", fontFamily = rubikFontFamily) },
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
                                selectedDate = dateFormat.format(selected.time)
                            },
                            calendar.get(Calendar.YEAR),
                            calendar.get(Calendar.MONTH),
                            calendar.get(Calendar.DAY_OF_MONTH)
                        ).show()
                    },
                enabled = false
            )
            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = selectedTime,
                onValueChange = {},
                label = { Text("Select Time", fontFamily = rubikFontFamily) },
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable {
                        val calendar = Calendar.getInstance()
                        TimePickerDialog(
                            context,
                            { _, hourOfDay, minute ->
                                val selected = Calendar.getInstance()
                                selected.set(Calendar.HOUR_OF_DAY, hourOfDay)
                                selected.set(Calendar.MINUTE, minute)
                                val timeFormat = SimpleDateFormat("HH:mm", Locale.getDefault())
                                selectedTime = timeFormat.format(selected.time)
                            },
                            calendar.get(Calendar.HOUR_OF_DAY),
                            calendar.get(Calendar.MINUTE),
                            true
                        ).show()
                    },
                enabled = false
            )
            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = {
                    if (bookingState is AppointmentBookingViewModel.BookingState.Loading) return@Button
                    if (selectedSpecialty == null || selectedDate.isBlank() || selectedTime.isBlank()) {
                        Toast.makeText(context, "Please fill all fields", Toast.LENGTH_SHORT).show()
                        return@Button
                    }
                    val patientId = authPreferences.getUserId() ?: run {
                        Toast.makeText(context, "User not logged in", Toast.LENGTH_SHORT).show()
                        return@Button
                    }
                    val patientName = authPreferences.getName() ?: run {
                        Toast.makeText(context, "User name not found", Toast.LENGTH_SHORT).show()
                        return@Button
                    }
                    viewModel.createBooking(
                        patientId = patientId,
                        patientName = patientName,
                        lookingFor = selectedSpecialty!!,
                        preferredDate = selectedDate,
                        preferredTime = selectedTime,
                        priority = "medium"
                    )
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF6B5FF8)),
                enabled = bookingState !is AppointmentBookingViewModel.BookingState.Loading
            ) {
                if (bookingState is AppointmentBookingViewModel.BookingState.Loading) {
                    CircularProgressIndicator(
                        color = Color.White,
                        modifier = Modifier.size(24.dp)
                    )
                } else {
                    Text(
                        text = "Book Appointment",
                        color = Color.White,
                        fontFamily = rubikFontFamily,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}

data class Specialty(val name: String, val color: Color, val icon: Int)

@Composable
fun SpecialtyCard(
    specialty: Specialty,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val rubikFontFamily = FontFamily(
        Font(R.font.rubik_regular, FontWeight.Normal),
        Font(R.font.rubik_medium, FontWeight.Medium),
        Font(R.font.rubik_bold, FontWeight.Bold)
    )
    Card(
        shape = RoundedCornerShape(16.dp),
        modifier = modifier
            .height(120.dp)
            .clickable { onClick() }
            .border(
                width = if (isSelected) 2.dp else 0.dp,
                color = if (isSelected) Color(0xFF6B5FF8) else Color.Transparent,
                shape = RoundedCornerShape(16.dp)
            ),
        colors = CardDefaults.cardColors(containerColor = specialty.color)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Image(
                painter = painterResource(id = specialty.icon),
                contentDescription = specialty.name,
                modifier = Modifier.size(40.dp)
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = specialty.name,
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium,
                fontFamily = rubikFontFamily,
                color = Color.White,
                maxLines = 2,
                textAlign = androidx.compose.ui.text.style.TextAlign.Center
            )
        }
    }
}