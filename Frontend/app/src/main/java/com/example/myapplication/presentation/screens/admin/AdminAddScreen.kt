package com.example.myapplication.presentation.screens.admin

import android.app.DatePickerDialog
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.myapplication.R
import com.example.myapplication.data.model.AuthPreferences
import com.example.myapplication.data.remote.NetworkProvider
import com.example.myapplication.data.repository.admin.StaffRepository
import java.net.URLEncoder
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun AdminAddScreen(
    navController: NavHostController,
    name: String,
    authPreferences: AuthPreferences
) {
    val rubikFontFamily = FontFamily(
        Font(R.font.rubik_regular, FontWeight.Normal),
        Font(R.font.rubik_medium, FontWeight.Medium),
        Font(R.font.rubik_bold, FontWeight.Bold)
    )
    val context = LocalContext.current
    val viewModel: AdminAddViewModel = viewModel {
        AdminAddViewModel(StaffRepository(authPreferences, NetworkProvider.staffApi))
    }
    val registrationState by viewModel.registrationState.collectAsState()

    // Form state
    var fullName by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var selectedRole by remember { mutableStateOf("Select Role") }
    var roleExpanded by remember { mutableStateOf(false) }
    var selectedSpecialization by remember { mutableStateOf("Select Specialization") }
    var specializationExpanded by remember { mutableStateOf(false) }
    var dateOfBirth by remember { mutableStateOf("") }
    var experienceYears by remember { mutableStateOf("") }
    var phone by remember { mutableStateOf("") }
    var rating by remember { mutableStateOf("") }
    var hospital by remember { mutableStateOf("") }

    // Handle registration state
    LaunchedEffect(registrationState) {
        when (val state = registrationState) {
            is AdminAddViewModel.RegistrationState.Success -> {
                Toast.makeText(context, "Registration successful", Toast.LENGTH_SHORT).show()
                val encodedName = URLEncoder.encode(name, "UTF-8")
                navController.navigate("admin_dashboard/$encodedName") {
                    popUpTo("admin_add/$encodedName") { inclusive = true }
                    launchSingleTop = true
                }
            }
            is AdminAddViewModel.RegistrationState.Error -> {
                Toast.makeText(context, state.message, Toast.LENGTH_SHORT).show()
            }
            else -> Unit
        }
    }


    Scaffold(
        modifier = Modifier
            .background(Color.White)
            .fillMaxSize(),
        bottomBar = { AdminBottomBar(navController, name) }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White)
                .padding(innerPadding)
                .padding(horizontal = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            item {
                // Title
                Text(
                    text = "Add New User",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = rubikFontFamily,
                    color = Color(0xFF6B5FF8),
                    modifier = Modifier.padding(vertical = 16.dp)
                )
            }

            // Full Name
            item {
                InputFieldLabel("Full Name", rubikFontFamily)
                TextField(
                    value = fullName,
                    onValueChange = { fullName = it },
                    placeholder = { Text("E.g., Alex Johnson", color = Color.Gray, fontFamily = rubikFontFamily, fontSize = 14.sp) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp)
                        .background(Color(0xFFF1F1F9), RoundedCornerShape(25.dp)),
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = Color(0xFFF1F1F9),
                        unfocusedContainerColor = Color(0xFFF1F1F9),
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent,
                        focusedTextColor = Color.Black,
                        unfocusedTextColor = Color.Black
                    ),
                    textStyle = LocalTextStyle.current.copy(fontFamily = rubikFontFamily, fontSize = 14.sp)
                )
                Spacer(modifier = Modifier.height(12.dp))
            }

            // Email
            item {
                InputFieldLabel("Email", rubikFontFamily)
                TextField(
                    value = email,
                    onValueChange = { email = it },
                    placeholder = { Text("E.g., you@example.com", color = Color.Gray, fontFamily = rubikFontFamily, fontSize = 14.sp) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp)
                        .background(Color(0xFFF1F1F9), RoundedCornerShape(25.dp)),
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = Color(0xFFF1F1F9),
                        unfocusedContainerColor = Color(0xFFF1F1F9),
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent,
                        focusedTextColor = Color.Black,
                        unfocusedTextColor = Color.Black
                    ),
                    textStyle = LocalTextStyle.current.copy(fontFamily = rubikFontFamily, fontSize = 14.sp)
                )
                Spacer(modifier = Modifier.height(12.dp))
            }

            // Password
            item {
                InputFieldLabel("Password", rubikFontFamily)
                TextField(
                    value = password,
                    onValueChange = { password = it },
                    placeholder = { Text("Password", color = Color.Gray, fontFamily = rubikFontFamily, fontSize = 14.sp) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp)
                        .background(Color(0xFFF1F1F9), RoundedCornerShape(25.dp)),
                    visualTransformation = PasswordVisualTransformation(),
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = Color(0xFFF1F1F9),
                        unfocusedContainerColor = Color(0xFFF1F1F9),
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent,
                        focusedTextColor = Color.Black,
                        unfocusedTextColor = Color.Black
                    ),
                    textStyle = LocalTextStyle.current.copy(fontFamily = rubikFontFamily, fontSize = 14.sp)
                )
                Spacer(modifier = Modifier.height(12.dp))
            }

            // Confirm Password
            item {
                InputFieldLabel("Confirm Password", rubikFontFamily)
                TextField(
                    value = confirmPassword,
                    onValueChange = { confirmPassword = it },
                    placeholder = { Text("Confirm Password", color = Color.Gray, fontFamily = rubikFontFamily, fontSize = 14.sp) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp)
                        .background(Color(0xFFF1F1F9), RoundedCornerShape(25.dp)),
                    visualTransformation = PasswordVisualTransformation(),
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = Color(0xFFF1F1F9),
                        unfocusedContainerColor = Color(0xFFF1F1F9),
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent,
                        focusedTextColor = Color.Black,
                        unfocusedTextColor = Color.Black
                    ),
                    textStyle = LocalTextStyle.current.copy(fontFamily = rubikFontFamily, fontSize = 14.sp)
                )
                Spacer(modifier = Modifier.height(12.dp))
            }

            // Role Dropdown
            item {
                InputFieldLabel("Role", rubikFontFamily)
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp)
                        .background(Color(0xFFF1F1F9), RoundedCornerShape(25.dp))
                        .clickable { roleExpanded = true }
                        .padding(horizontal = 16.dp, vertical = 12.dp),
                    contentAlignment = Alignment.CenterStart
                ) {
                    Text(
                        text = selectedRole,
                        fontFamily = rubikFontFamily,
                        fontSize = 14.sp,
                        color = if (selectedRole == "Select Role") Color.Gray else Color.Black
                    )
                    DropdownMenu(
                        expanded = roleExpanded,
                        onDismissRequest = { roleExpanded = false },
                        modifier = Modifier.background(Color.White)
                    ) {
                        listOf("Doctor", "Triage", "Admin").forEach { role ->
                            DropdownMenuItem(
                                text = { Text(role, fontFamily = rubikFontFamily, fontSize = 14.sp) },
                                onClick = {
                                    selectedRole = role
                                    roleExpanded = false
                                }
                            )
                        }
                    }
                }
                Spacer(modifier = Modifier.height(12.dp))
            }

            // Specialization Dropdown
            item {
                InputFieldLabel("Specialization", rubikFontFamily)
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp)
                        .background(Color(0xFFF1F1F9), RoundedCornerShape(25.dp))
                        .clickable { specializationExpanded = true }
                        .padding(horizontal = 16.dp, vertical = 12.dp),
                    contentAlignment = Alignment.CenterStart
                ) {
                    Text(
                        text = selectedSpecialization,
                        fontFamily = rubikFontFamily,
                        fontSize = 14.sp,
                        color = if (selectedSpecialization == "Select Specialization") Color.Gray else Color.Black
                    )
                    DropdownMenu(
                        expanded = specializationExpanded,
                        onDismissRequest = { specializationExpanded = false },
                        modifier = Modifier.background(Color.White)
                    ) {
                        listOf("Cardiologist", "Dermatologist", "Neurologist", "Pediatrician").forEach { spec ->
                            DropdownMenuItem(
                                text = { Text(spec, fontFamily = rubikFontFamily, fontSize = 14.sp) },
                                onClick = {
                                    selectedSpecialization = spec
                                    specializationExpanded = false
                                }
                            )
                        }
                    }
                }
                Spacer(modifier = Modifier.height(12.dp))
            }

            // Date of Birth
            item {
                InputFieldLabel("Date of Birth", rubikFontFamily)
                TextField(
                    value = dateOfBirth,
                    onValueChange = {},
                    placeholder = { Text("YYYY-MM-DD", color = Color.Gray, fontFamily = rubikFontFamily, fontSize = 14.sp) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp)
                        .background(Color(0xFFF1F1F9), RoundedCornerShape(25.dp))
                        .clickable {
                            val calendar = Calendar.getInstance()
                            DatePickerDialog(
                                context,
                                { _, year, month, dayOfMonth ->
                                    val selected = Calendar.getInstance()
                                    selected.set(year, month, dayOfMonth)
                                    val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                                    dateOfBirth = dateFormat.format(selected.time)
                                },
                                calendar.get(Calendar.YEAR),
                                calendar.get(Calendar.MONTH),
                                calendar.get(Calendar.DAY_OF_MONTH)
                            ).show()
                        },
                    enabled = false,
                    colors = TextFieldDefaults.colors(
                        disabledContainerColor = Color(0xFFF1F1F9),
                        disabledIndicatorColor = Color.Transparent,
                        disabledTextColor = Color.Black,
                        disabledPlaceholderColor = Color.Gray
                    ),
                    textStyle = LocalTextStyle.current.copy(fontFamily = rubikFontFamily, fontSize = 14.sp)
                )
                Spacer(modifier = Modifier.height(12.dp))
            }

            // Experience Years
            item {
                InputFieldLabel("Experience Years (Optional)", rubikFontFamily)
                TextField(
                    value = experienceYears,
                    onValueChange = { input ->
                        experienceYears = input.filter { it.isDigit() }
                    },
                    placeholder = { Text("E.g., 5", color = Color.Gray, fontFamily = rubikFontFamily, fontSize = 14.sp) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp)
                        .background(Color(0xFFF1F1F9), RoundedCornerShape(25.dp)),
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = Color(0xFFF1F1F9),
                        unfocusedContainerColor = Color(0xFFF1F1F9),
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent,
                        focusedTextColor = Color.Black,
                        unfocusedTextColor = Color.Black
                    ),
                    textStyle = LocalTextStyle.current.copy(fontFamily = rubikFontFamily, fontSize = 14.sp)
                )
                Spacer(modifier = Modifier.height(12.dp))
            }

            // Phone
            item {
                InputFieldLabel("Phone (Optional)", rubikFontFamily)
                TextField(
                    value = phone,
                    onValueChange = { phone = it },
                    placeholder = { Text("E.g., 123-456-7890", color = Color.Gray, fontFamily = rubikFontFamily, fontSize = 14.sp) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp)
                        .background(Color(0xFFF1F1F9), RoundedCornerShape(25.dp)),
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = Color(0xFFF1F1F9),
                        unfocusedContainerColor = Color(0xFFF1F1F9),
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent,
                        focusedTextColor = Color.Black,
                        unfocusedTextColor = Color.Black
                    ),
                    textStyle = LocalTextStyle.current.copy(fontFamily = rubikFontFamily, fontSize = 14.sp)
                )
                Spacer(modifier = Modifier.height(12.dp))
            }

            // Rating
            item {
                InputFieldLabel("Rating (Optional)", rubikFontFamily)
                TextField(
                    value = rating,
                    onValueChange = { input ->
                        rating = input.filter { it.isDigit() || it == '.' }
                        if (input.contains("..")) rating = input.substring(0, input.length - 1)
                    },
                    placeholder = { Text("E.g., 4.5", color = Color.Gray, fontFamily = rubikFontFamily, fontSize = 14.sp) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp)
                        .background(Color(0xFFF1F1F9), RoundedCornerShape(25.dp)),
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = Color(0xFFF1F1F9),
                        unfocusedContainerColor = Color(0xFFF1F1F9),
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent,
                        focusedTextColor = Color.Black,
                        unfocusedTextColor = Color.Black
                    ),
                    textStyle = LocalTextStyle.current.copy(fontFamily = rubikFontFamily, fontSize = 14.sp)
                )
                Spacer(modifier = Modifier.height(12.dp))
            }

            // Hospital
            item {
                InputFieldLabel("Hospital (Optional)", rubikFontFamily)
                TextField(
                    value = hospital,
                    onValueChange = { hospital = it },
                    placeholder = { Text("E.g., General Hospital", color = Color.Gray, fontFamily = rubikFontFamily, fontSize = 14.sp) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp)
                        .background(Color(0xFFF1F1F9), RoundedCornerShape(25.dp)),
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = Color(0xFFF1F1F9),
                        unfocusedContainerColor = Color(0xFFF1F1F9),
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent,
                        focusedTextColor = Color.Black,
                        unfocusedTextColor = Color.Black
                    ),
                    textStyle = LocalTextStyle.current.copy(fontFamily = rubikFontFamily, fontSize = 14.sp)
                )
                Spacer(modifier = Modifier.height(24.dp))
            }

            // Register Button
            item {
                Button(
                    onClick = {
                        viewModel.registerStaff(
                            fullName = fullName,
                            email = email,
                            password = password,
                            confirmPassword = confirmPassword,
                            role = selectedRole,
                            specialization = selectedSpecialization,
                            dateOfBirth = dateOfBirth,
                            experienceYears = experienceYears.toIntOrNull(),
                            phone = phone.takeIf { it.isNotBlank() },
                            rating = rating.toFloatOrNull(),
                            hospital = hospital.takeIf { it.isNotBlank() }
                        )
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF6B5FF8)),
                    shape = RoundedCornerShape(25.dp),
                    enabled = registrationState !is AdminAddViewModel.RegistrationState.Loading
                ) {
                    if (registrationState is AdminAddViewModel.RegistrationState.Loading) {
                        CircularProgressIndicator(
                            color = Color.White,
                            modifier = Modifier.size(24.dp)
                        )
                    } else {
                        Text(
                            text = "Register",
                            color = Color.White,
                            fontFamily = rubikFontFamily,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }
                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    }
}

@Composable
fun InputFieldLabel(text: String, fontFamily: FontFamily) {
    Text(
        text = text,
        fontSize = 14.sp,
        fontWeight = FontWeight.Medium,
        fontFamily = fontFamily,
        color = Color.Gray,
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 4.dp)
    )
}