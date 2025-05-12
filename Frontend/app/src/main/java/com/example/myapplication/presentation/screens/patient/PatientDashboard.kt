package com.example.myapplication.presentation.screens.patient

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
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
import com.example.myapplication.data.model.doctor.Doctor
import com.example.myapplication.data.model.patient.Appointment
import com.example.myapplication.data.model.patient.PendingBooking
import com.example.myapplication.data.repository.patient.AppointmentRepository
import com.example.myapplication.navigation.BottomNavigationBar
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun PatientDashboardScreen(
    navController: NavHostController,
    userName: String,
    authPreferences: AuthPreferences
) {
    val rubikFontFamily = FontFamily(
        Font(R.font.rubik_regular, FontWeight.Normal),
        Font(R.font.rubik_medium, FontWeight.Medium),
        Font(R.font.rubik_bold, FontWeight.Bold)
    )
    val viewModel: AppointmentViewModel = viewModel(factory = AppointmentViewModelFactory(AppointmentRepository(authPreferences)))
    var showSettingsPopup by remember { mutableStateOf(false) }
    var showNotificationPage by remember { mutableStateOf(false) }

    Scaffold(
        bottomBar = { BottomNavigationBar(navController, authPreferences) }
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
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Image(
                        painter = painterResource(id = R.drawable.doctor),
                        contentDescription = "Profile",
                        modifier = Modifier
                            .size(40.dp)
                            .clip(CircleShape)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Column {
                        Text(
                            text = "Hi, Welcome Back",
                            fontSize = 14.sp,
                            color = Color.Gray,
                            fontFamily = rubikFontFamily
                        )
                        Text(
                            text = userName,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            fontFamily = rubikFontFamily
                        )
                    }
                }
                Row {
                    Icon(
                        painter = painterResource(id = R.drawable.bell),
                        contentDescription = "Notifications",
                        modifier = Modifier
                            .size(24.dp)
                            .clickable { showNotificationPage = true },
                        tint = Color(0xFF6B5FF8)
                    )
                    Spacer(modifier = Modifier.width(16.dp))
                    Icon(
                        painter = painterResource(id = R.drawable.cog_outline),
                        contentDescription = "Settings",
                        modifier = Modifier
                            .size(24.dp)
                            .clickable { showSettingsPopup = true },
                        tint = Color(0xFF6B5FF8)
                    )
                }
            }

            // Doctor Consult Card
            DoctorConsultCard(rubikFontFamily)

            // Top Doctors Section
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Top Doctors",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Medium,
                    fontFamily = rubikFontFamily
                )
                Text(
                    text = "See all",
                    fontSize = 14.sp,
                    color = Color(0xFF6B5FF8),
                    modifier = Modifier.clickable { navController.navigate("doctors") }
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(rememberScrollState()),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                listOf(
                    Doctor(
                        id = "doc2",
                        name = "Dr. Biruk A",
                        specialty = "Cardiologist",
                        imageRes = "R.drawable.doctor",
                        rating = 4.7f,
                        phone = "123",
                        hospital = null,
                        experienceYears = null
                    ),
                    Doctor(
                        id = "doc3",
                        name = "Dr. Firaol",
                        specialty = "Psychologist",
                        imageRes = "R.drawable.doctor",
                        rating = 4.9f,
                        phone = "123",
                        hospital = null,
                        experienceYears = null
                    ),
                    Doctor(
                        id = "doc4",
                        name = "Dr. Abdulkerim",
                        specialty = "Orthopedist",
                        imageRes = "R.drawable.doctor",
                        rating = 4.8f,
                        phone = "123",
                        hospital = null,
                        experienceYears = null
                    )
                ).forEach { doctor ->
                    TopDoctorCard(doctor = doctor, rubikFontFamily)
                }
            }

            // Dashboard Card
            val dashboardState by viewModel.dashboardState.collectAsState()
            when (val state = dashboardState) {
                is AppointmentViewModel.DashboardState.Loading -> {
                    CircularProgressIndicator(modifier = Modifier.padding(16.dp))
                }
                is AppointmentViewModel.DashboardState.Success -> {
                    val appointments = state.dashboardData.data?.upcomingAppointments ?: emptyList()
                    val pendingBookings = state.dashboardData.data?.pendingBookings ?: emptyList()
                    if (appointments.isNotEmpty()) {
                        AppointmentCard(
                            appointment = appointments.first(),
                            rubikFontFamily = rubikFontFamily,
                            onCancel = null
                        )
                    } else if (pendingBookings.isNotEmpty()) {
                        AppointmentCard(
                            pendingBooking = pendingBookings.first(),
                            rubikFontFamily = rubikFontFamily,
                            onCancel = { viewModel.cancelBooking(pendingBookings.first().id) }
                        )
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                }
                is AppointmentViewModel.DashboardState.Error -> {
                    Text(
                        text = state.message,
                        fontFamily = rubikFontFamily,
                        fontSize = 16.sp,
                        color = Color.Red,
                        modifier = Modifier.padding(16.dp)
                    )
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
                    .fillMaxWidth(0.9f) // 90% of screen width
                    .height(300.dp) // Fixed height for larger popup
                    .wrapContentHeight(align = Alignment.CenterVertically) // Center vertically
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
                                    popUpTo("patient_dashboard") { inclusive = true }
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

    // Notification Subpage
    if (showNotificationPage) {
        NotificationScreen(navController) { showNotificationPage = false }
    }
}

@Composable
fun DoctorConsultCard(rubikFontFamily: FontFamily) {
    Card(
        shape = RoundedCornerShape(24.dp),
        modifier = Modifier
            .fillMaxWidth()
            .height(200.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    brush = Brush.horizontalGradient(
                        colors = listOf(Color(0xFF7563F7), Color(0xFF1D7885))
                    )
                )
        ) {
            Row(
                modifier = Modifier.fillMaxSize(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(
                    modifier = Modifier
                        .weight(1.1f)
                        .fillMaxHeight()
                        .padding(16.dp),
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = "Trusted doctor on your schedule",
                        color = Color.White,
                        fontSize = 14.sp,
                        fontFamily = rubikFontFamily
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Consult A Doctor\n— Book Today!",
                        color = Color.White,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        fontFamily = rubikFontFamily
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Start,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row {
                            val avatarIds = listOf(R.drawable.profiles)
                            avatarIds.forEachIndexed { index, id ->
                                Image(
                                    painter = painterResource(id = id),
                                    contentDescription = "Patient Avatar",
                                    modifier = Modifier.size(95.dp)
                                )
                            }
                        }
                        Spacer(modifier = Modifier.width(5.dp))
                        Text(
                            text = "30,000+\nHappy Patients",
                            color = Color.White,
                            fontSize = 12.sp,
                            fontFamily = rubikFontFamily
                        )
                    }
                }
                Image(
                    painter = painterResource(id = R.drawable.doctor1),
                    contentDescription = "Doctor",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .weight(0.9f)
                        .fillMaxHeight()
                )
            }
        }
    }
}

@Composable
fun TopDoctorCard(doctor: Doctor, rubikFontFamily: FontFamily) {
    Card(
        shape = RoundedCornerShape(16.dp),
        modifier = Modifier
            .width(130.dp)
            .height(180.dp)
            .border(0.8.dp, Color(0xFFD3D3D3), RoundedCornerShape(16.dp)),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.3.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Image(
                painter = painterResource(
                    id = when (doctor.imageRes) {
                        "ic_doctor" -> R.drawable.doctor
                        else -> R.drawable.doctor
                    }
                ),
                contentDescription = doctor.name,
                modifier = Modifier
                    .size(80.dp)
                    .clip(CircleShape)
                    .padding(top = 20.dp),
                contentScale = ContentScale.Fit
            )
            Column(
                modifier = Modifier
                    .padding(8.dp)
                    .fillMaxWidth(),
                horizontalAlignment = Alignment.Start
            ) {
                Text(
                    text = doctor.name,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium,
                    fontFamily = rubikFontFamily
                )
                Text(
                    text = doctor.specialty ?: "General Doctor",
                    fontSize = 12.sp,
                    color = Color.Gray,
                    fontFamily = rubikFontFamily
                )
                doctor.rating?.let { rating ->
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .background(Color(0xFFF1E6FF), RoundedCornerShape(4.dp))
                            .padding(horizontal = 4.dp, vertical = 2.dp)
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_star),
                            contentDescription = "Rating",
                            modifier = Modifier.size(16.dp),
                            tint = Color(0xFF6B5FF8)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "$rating",
                            fontSize = 12.sp,
                            color = Color.Gray,
                            fontFamily = rubikFontFamily
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun AppointmentCard(
    appointment: Appointment? = null,
    pendingBooking: PendingBooking? = null,
    rubikFontFamily: FontFamily,
    onCancel: (() -> Unit)? = null
) {
    Card(
        shape = RoundedCornerShape(16.dp),
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 16.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF6B5FF8)),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = if (appointment != null) "Upcoming Appointments" else "Pending Booking",
                color = Color.White,
                fontSize = 20.sp,
                fontWeight = FontWeight.Medium,
                fontFamily = rubikFontFamily
            )
            Spacer(modifier = Modifier.height(8.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier
                        .background(Color(0xFF8A7DFF), RoundedCornerShape(8.dp))
                        .padding(8.dp)
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.calendar_blank_outline),
                        contentDescription = "Date",
                        modifier = Modifier.size(32.dp),
                        tint = Color.White
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = formatDate(appointment?.date ?: pendingBooking?.preferredDate),
                        color = Color.White,
                        fontSize = 14.sp,
                        fontFamily = rubikFontFamily
                    )
                    Text(
                        text = "Appointment Date",
                        color = Color.White,
                        fontSize = 12.sp,
                        fontFamily = rubikFontFamily
                    )
                }
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier
                        .background(Color(0xFF8A7DFF), RoundedCornerShape(8.dp))
                        .padding(8.dp)
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.clock_check_outline),
                        contentDescription = "Time",
                        modifier = Modifier.size(32.dp),
                        tint = Color.White
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = appointment?.time ?: pendingBooking?.preferredTime ?: "N/A",
                        color = Color.White,
                        fontSize = 14.sp,
                        fontFamily = rubikFontFamily
                    )
                    Text(
                        text = "Appointment Time",
                        color = Color.White,
                        fontSize = 12.sp,
                        fontFamily = rubikFontFamily
                    )
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.White, RoundedCornerShape(8.dp))
                    .padding(8.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Image(
                        painter = painterResource(id = R.drawable.doctor1),
                        contentDescription = appointment?.doctor?.name ?: "Pending",
                        modifier = Modifier
                            .size(40.dp)
                            .clip(CircleShape)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Column {
                        Text(
                            text = appointment?.doctor?.name ?: pendingBooking?.lookingFor?.capitalize() ?: "Pending",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Medium,
                            fontFamily = rubikFontFamily,
                            color = Color.Black
                        )
                        Text(
                            text = appointment?.doctor?.specialization ?: "Awaiting Assignment",
                            fontSize = 12.sp,
                            color = Color.Gray,
                            fontFamily = rubikFontFamily
                        )
                    }
                    Spacer(modifier = Modifier.weight(1f))
                    if (pendingBooking != null && onCancel != null) {
                        Button(
                            onClick = onCancel,
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF8A7DFF)),
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Text(
                                text = "Cancel",
                                color = Color.White,
                                fontFamily = rubikFontFamily,
                                fontSize = 12.sp
                            )
                        }
                    } else {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_chat),
                            contentDescription = "Chat",
                            modifier = Modifier.size(20.dp),
                            tint = Color(0xFF6B5FF8)
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun NotificationScreen(navController: NavHostController, onDismiss: () -> Unit) {
    Surface(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black.copy(alpha = 0.5f)),
        color = Color.Transparent
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.White, RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp))
                .padding(16.dp)
        ) {
            Text(
                text = "Notifications",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text("No new notifications.", fontSize = 16.sp)
            Spacer(modifier = Modifier.weight(1f))
            Button(
                onClick = onDismiss,
                modifier = Modifier.align(Alignment.End)
            ) {
                Text("Close")
            }
        }
    }
}

fun formatDate(dateString: String?): String {
    if (dateString == null) return "N/A"
    return try {
        val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault())
        inputFormat.timeZone = TimeZone.getTimeZone("UTC")
        val outputFormat = SimpleDateFormat("MMM dd, yyyy", Locale.getDefault())
        val date = inputFormat.parse(dateString)
        date?.let { outputFormat.format(it) } ?: "N/A"
    } catch (e: Exception) {
        "N/A"
    }
}