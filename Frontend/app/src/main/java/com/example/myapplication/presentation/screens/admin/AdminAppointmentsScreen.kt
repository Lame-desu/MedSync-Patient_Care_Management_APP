package com.example.myapplication.presentation.screens.admin

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
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
import androidx.navigation.NavHostController
import com.example.myapplication.R

@Composable
fun AdminAppointmentsScreen(
    navController: NavHostController,
    name: String
) {
    val rubikFontFamily = FontFamily(
        Font(R.font.rubik_regular, FontWeight.Normal),
        Font(R.font.rubik_medium, FontWeight.Medium),
        Font(R.font.rubik_bold, FontWeight.Bold)
    )
    val context = LocalContext.current
    var selectedTab by remember { mutableStateOf("UPCOMING") }
    val tabs = listOf("COMPLETE", "UPCOMING", "CANCELLED")

    // Placeholder patient data for each tab
    val patients = listOf(
        "Patient Sarah",
        "Patient John",
        "Patient Emma"
    )

    Scaffold(
        modifier = Modifier
            .background(Color.White)
            .fillMaxSize(),
        bottomBar = { AdminBottomBar(navController, name) }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White)
                .padding(innerPadding)
                .padding(16.dp)
        ) {
            // Title
            Text(
                text = "All Appointments",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                fontFamily = rubikFontFamily,
                color = Color(0xFF6B5FF8),
                modifier = Modifier.padding(bottom = 16.dp)
            )

            // Tabs
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                tabs.forEach { tab ->
                    val isSelected = selectedTab == tab
                    FilterChip(
                        selected = isSelected,
                        onClick = { selectedTab = tab },
                        label = {
                            Text(
                                text = tab,
                                color = if (isSelected) Color.White else Color(0xFF6B5FF8),
                                fontWeight = FontWeight.SemiBold,
                                fontFamily = rubikFontFamily,
                                fontSize = 14.sp
                            )
                        },
                        modifier = Modifier.padding(horizontal = 4.dp),
                        shape = RoundedCornerShape(16.dp),
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = Color(0xFF6B5FF8),
                            containerColor = Color(0xFFE8EAFE)
                        )
                    )
                }
            }

            // Patient Cards
            LazyColumn(
                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                modifier = Modifier.weight(1f)
            ) {
                items(patients.size) { index ->
                    PatientCard(
                        patientName = patients[index],
                        rubikFontFamily = rubikFontFamily,
                        onAddReviewClick = {
                            Toast.makeText(context, "Add review clicked for ${patients[index]}", Toast.LENGTH_SHORT).show()
                        }
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                }
            }
        }
    }
}

@Composable
fun PatientCard(
    patientName: String,
    rubikFontFamily: FontFamily,
    onAddReviewClick: () -> Unit
) {
    // Outer Card (Light Purple)
    Card(
        shape = RoundedCornerShape(16.dp),
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFE8EAFE)),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        // Inner Card (Purple)
        Card(
            shape = RoundedCornerShape(12.dp),
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            colors = CardDefaults.cardColors(containerColor = Color(0xFF6B5FF8)),
            elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Profile Image
                    Image(
                        painter = painterResource(id = R.drawable.profile2),
                        contentDescription = "Patient Profile",
                        modifier = Modifier
                            .size(50.dp)
                            .clip(CircleShape),
                        contentScale = androidx.compose.ui.layout.ContentScale.Crop
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    // Patient Name
                    Text(
                        text = patientName,
                        fontWeight = FontWeight.Bold,
                        color = Color.White,
                        fontFamily = rubikFontFamily,
                        fontSize = 16.sp
                    )
                }
                Spacer(modifier = Modifier.height(16.dp))
                // Add Review Button
                Button(
                    onClick = onAddReviewClick,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(40.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF5A4ED8)),
                    shape = RoundedCornerShape(25.dp)
                ) {
                    Text(
                        text = "Add Review",
                        color = Color.White,
                        fontFamily = rubikFontFamily,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Medium
                    )
                }
            }
        }
    }
}