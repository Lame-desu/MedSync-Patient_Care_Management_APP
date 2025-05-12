package com.example.myapplication.presentation.auth

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.myapplication.R
import com.example.myapplication.data.model.AuthPreferences
import com.example.myapplication.data.repository.AuthRepository
import java.net.URLEncoder
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

@Composable
fun ProgressBar(currentStep: Int, totalSteps: Int) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp)
            .padding(top = 16.dp)
    ) {
        for (i in 1..totalSteps) {
            Box(
                modifier = Modifier
                    .weight(1f)
                    .height(4.dp)
                    .background(if (i == currentStep) Color(0xFF6B5FF8) else Color(0xFFE0E0E0))
            )
            if (i < totalSteps) {
                Spacer(modifier = Modifier.width(4.dp))
            }
        }
    }
}

@Composable
fun StepOne(viewModel: RegisterViewModel, rubikFontFamily: FontFamily) {
    val context = LocalContext.current
    Column {
        Text(
            text = "Start Your Health Journey",
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold,
            fontFamily = rubikFontFamily,
            color = Color(0xFF6B5FF8)
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = "Let’s tailor your experience with a few steps.",
            fontSize = 16.sp,
            fontWeight = FontWeight.Medium,
            fontFamily = rubikFontFamily,
            color = Color.Black
        )
        Spacer(modifier = Modifier.height(32.dp))

        Text(
            text = "Your Name",
            color = Color(0xFF6B5FF8),
            fontFamily = rubikFontFamily,
            fontWeight = FontWeight.Medium
        )
        OutlinedTextField(
            value = viewModel.name,
            onValueChange = { viewModel.name = it },
            placeholder = { Text("E.g. Abebe Kebede", color = Color.Gray, fontFamily = rubikFontFamily) },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )
        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Date of Birth",
            color = Color(0xFF6B5FF8),
            fontFamily = rubikFontFamily,
            fontWeight = FontWeight.Medium
        )
        OutlinedTextField(
            value = viewModel.dateOfBirth,
            onValueChange = {},
            placeholder = { Text("YYYY-MM-DD", color = Color.Gray, fontFamily = rubikFontFamily) },
            modifier = Modifier
                .fillMaxWidth()
                .clickable {
                    val calendar = Calendar.getInstance()
                    android.app.DatePickerDialog(
                        context,
                        { _, year, month, dayOfMonth ->
                            val selectedDate = Calendar.getInstance()
                            selectedDate.set(year, month, dayOfMonth)
                            val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                            viewModel.dateOfBirth = dateFormat.format(selectedDate.time)
                        },
                        calendar.get(Calendar.YEAR),
                        calendar.get(Calendar.MONTH),
                        calendar.get(Calendar.DAY_OF_MONTH)
                    ).show()
                },
            enabled = false
        )
        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Gender",
            color = Color(0xFF6B5FF8),
            fontFamily = rubikFontFamily,
            fontWeight = FontWeight.Medium
        )
        var expandedGender by remember { mutableStateOf(false) }
        Box {
            OutlinedTextField(
                value = viewModel.gender,
                onValueChange = {},
                modifier = Modifier.fillMaxWidth(),
                readOnly = true,
                placeholder = { Text("Select gender", color = Color.Gray, fontFamily = rubikFontFamily) },
                trailingIcon = {
                    IconButton(onClick = { expandedGender = true }) {
                        Icon(Icons.Default.ArrowDropDown, contentDescription = null)
                    }
                }
            )
            DropdownMenu(
                expanded = expandedGender,
                onDismissRequest = { expandedGender = false }
            ) {
                listOf("Male", "Female", "Other").forEach { genderOption ->
                    DropdownMenuItem(
                        text = { Text(genderOption, fontFamily = rubikFontFamily) },
                        onClick = {
                            viewModel.gender = genderOption
                            expandedGender = false
                        }
                    )
                }
            }
        }
        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Age",
            color = Color(0xFF6B5FF8),
            fontFamily = rubikFontFamily,
            fontWeight = FontWeight.Medium
        )
        OutlinedTextField(
            value = viewModel.age,
            onValueChange = { viewModel.age = it },
            placeholder = { Text("E.g. 30", color = Color.Gray, fontFamily = rubikFontFamily) },
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            singleLine = true
        )
    }
}

@Composable
fun StepTwo(viewModel: RegisterViewModel, rubikFontFamily: FontFamily) {
    Column {
        Text(
            text = "Share a Bit More",
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold,
            fontFamily = rubikFontFamily,
            color = Color(0xFF6B5FF8)
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = "Let’s tailor your experience with a few steps.",
            fontSize = 16.sp,
            fontWeight = FontWeight.Medium,
            fontFamily = rubikFontFamily,
            color = Color.Black
        )
        Spacer(modifier = Modifier.height(32.dp))

        Text(
            text = "Blood Group",
            color = Color(0xFF6B5FF8),
            fontFamily = rubikFontFamily,
            fontWeight = FontWeight.Medium
        )
        OutlinedTextField(
            value = viewModel.bloodGroup,
            onValueChange = { viewModel.bloodGroup = it },
            placeholder = { Text("E.g., A+ or B-", fontFamily = rubikFontFamily) },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )
        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Emergency Contact Name",
            color = Color(0xFF6B5FF8),
            fontFamily = rubikFontFamily,
            fontWeight = FontWeight.Medium
        )
        OutlinedTextField(
            value = viewModel.emergencyContactName,
            onValueChange = { viewModel.emergencyContactName = it },
            placeholder = { Text("E.g., Alex Johnson", fontFamily = rubikFontFamily) },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )
        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Emergency Contact Number",
            color = Color(0xFF6B5FF8),
            fontFamily = rubikFontFamily,
            fontWeight = FontWeight.Medium
        )
        OutlinedTextField(
            value = viewModel.emergencyContactNumber,
            onValueChange = { viewModel.emergencyContactNumber = it },
            placeholder = { Text("E.g., +251979123456", fontFamily = rubikFontFamily) },
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
            singleLine = true
        )
    }
}

@Composable
fun StepThree(viewModel: RegisterViewModel, rubikFontFamily: FontFamily) {
    Column {
        Text(
            text = "You’re Ready to Connect",
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold,
            fontFamily = rubikFontFamily,
            color = Color(0xFF6B5FF8)
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = "Let’s tailor your experience with a few steps.",
            fontSize = 16.sp,
            fontWeight = FontWeight.Medium,
            fontFamily = rubikFontFamily,
            color = Color.Black
        )
        Spacer(modifier = Modifier.height(32.dp))

        Text(
            text = "Email",
            color = Color(0xFF6B5FF8),
            fontFamily = rubikFontFamily,
            fontWeight = FontWeight.Medium
        )
        OutlinedTextField(
            value = viewModel.email,
            onValueChange = { viewModel.email = it },
            placeholder = { Text("E.g., you@example.com", fontFamily = rubikFontFamily) },
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
            singleLine = true
        )
        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Password",
            color = Color(0xFF6B5FF8),
            fontFamily = rubikFontFamily,
            fontWeight = FontWeight.Medium
        )
        OutlinedTextField(
            value = viewModel.password,
            onValueChange = { viewModel.password = it },
            placeholder = { Text("Your password", fontFamily = rubikFontFamily) },
            modifier = Modifier.fillMaxWidth(),
            visualTransformation = PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            singleLine = true
        )
        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Confirm Password",
            color = Color(0xFF6B5FF8),
            fontFamily = rubikFontFamily,
            fontWeight = FontWeight.Medium
        )
        OutlinedTextField(
            value = viewModel.confirmPassword,
            onValueChange = { viewModel.confirmPassword = it },
            placeholder = { Text("Confirm your password", fontFamily = rubikFontFamily) },
            modifier = Modifier.fillMaxWidth(),
            visualTransformation = PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            singleLine = true
        )
    }
}

@Composable
fun RegisterScreen(
    navController: NavHostController,
    authPreferences: AuthPreferences
) {
    val rubikFontFamily = FontFamily(
        Font(R.font.rubik_regular, FontWeight.Normal),
        Font(R.font.rubik_medium, FontWeight.Medium),
        Font(R.font.rubik_bold, FontWeight.Bold)
    )
    val context = LocalContext.current
    val viewModel: RegisterViewModel = viewModel(
        factory = RegisterViewModelFactory(AuthRepository(), authPreferences)
    )
    val registerState by viewModel.registerState.collectAsState()

    LaunchedEffect(registerState) {
        Log.d("RegisterScreen", "RegisterState: $registerState")
        when (registerState) {
            is RegisterState.Loading -> {
                Toast.makeText(context, "Registering...", Toast.LENGTH_SHORT).show()
            }
            is RegisterState.Success -> {
                val response = (registerState as RegisterState.Success).response
                Toast.makeText(context, "Registration successful!", Toast.LENGTH_SHORT).show()
                val encodedName = URLEncoder.encode(viewModel.name.takeIf { it.isNotBlank() } ?: "Guest", "UTF-8")
                val destination = "patient_dashboard/$encodedName"
                navController.navigate(destination) {
                    popUpTo("register") { inclusive = true }
                }
                viewModel.resetState()
            }
            is RegisterState.Error -> {
                val errorMessage = (registerState as RegisterState.Error).message
                Log.e("RegisterScreen", "Registration error: $errorMessage")
                Toast.makeText(context, errorMessage, Toast.LENGTH_LONG).show()
                viewModel.resetState()
            }
            is RegisterState.Idle -> {
                // No action needed
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(24.dp)
    ) {
        ProgressBar(currentStep = viewModel.currentStep, totalSteps = 3)
        Spacer(modifier = Modifier.height(16.dp))
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
        ) {
            when (viewModel.currentStep) {
                1 -> StepOne(viewModel, rubikFontFamily)
                2 -> StepTwo(viewModel, rubikFontFamily)
                3 -> StepThree(viewModel, rubikFontFamily)
            }
        }
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            if (viewModel.currentStep > 1) {
                Button(
                    onClick = { viewModel.previousStep() },
                    modifier = Modifier
                        .weight(1f)
                        .height(50.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFFE0E0E0),
                        contentColor = Color.Black
                    )
                ) {
                    Text(
                        text = "Back",
                        fontFamily = rubikFontFamily,
                        fontWeight = FontWeight.Bold
                    )
                }
            } else {
                Spacer(modifier = Modifier.weight(1f))
            }
            Spacer(modifier = Modifier.width(16.dp))
            Button(
                onClick = {
                    if (viewModel.currentStep < 3) {
                        viewModel.nextStep()
                    } else {
                        viewModel.register()
                    }
                },
                modifier = Modifier
                    .weight(1f)
                    .height(50.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF6B5FF8),
                    contentColor = Color.White
                ),
                enabled = registerState !is RegisterState.Loading
            ) {
                Text(
                    text = if (viewModel.currentStep < 3) "Next" else "Register",
                    fontFamily = rubikFontFamily,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}