package com.example.myapplication.presentation.screens.doctor

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
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
import androidx.navigation.NavHostController
import com.example.myapplication.R
import com.example.myapplication.data.model.AuthPreferences
import com.example.myapplication.navigation.DoctorBottomNavBar

@Composable
fun DoctorProfileScreen(
    navController: NavHostController,
    authPreferences: AuthPreferences
) {
    val rubikFontFamily = FontFamily(
        Font(R.font.rubik_regular, FontWeight.Normal),
        Font(R.font.rubik_medium, FontWeight.Medium),
        Font(R.font.rubik_bold, FontWeight.Bold)
    )
    val doctorName = authPreferences.getName() ?: "Doctor"
    val phone = "123-456-7890" // Placeholder
    val email = "doctor@example.com" // Placeholder

    Scaffold(
        topBar = {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color(0xFFD8C4E7))
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Profile",
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
                currentRoute = "profile"
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White)
                .padding(innerPadding)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Profile Picture
            Image(
                painter = painterResource(id = R.drawable.doctor1),
                contentDescription = "Profile Picture",
                modifier = Modifier
                    .size(120.dp)
                    .clip(CircleShape)
            )
            Spacer(modifier = Modifier.height(16.dp))

            // Info
            Column(
                horizontalAlignment = Alignment.Start
            ) {
                Text(
                    text = "Name: $doctorName",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Medium,
                    fontFamily = rubikFontFamily
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Phone: $phone",
                    fontSize = 16.sp,
                    fontFamily = rubikFontFamily
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Email: $email",
                    fontSize = 16.sp,
                    fontFamily = rubikFontFamily
                )
            }
            Spacer(modifier = Modifier.height(24.dp))

            // Logout Button
            Button(
                onClick = {
                    authPreferences.clearAuthData()
                    navController.navigate("login") {
                        popUpTo("profile") { inclusive = true }
                    }
                },
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF6B5FF8))
            ) {
                Text("Logout", color = Color.White, fontFamily = rubikFontFamily, fontSize = 16.sp)
            }

            // Placeholder for future content
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "More details to be added here (e.g., specialization, experience, settings)",
                fontSize = 14.sp,
                color = Color.Gray,
                fontFamily = rubikFontFamily
            )
        }
    }
}