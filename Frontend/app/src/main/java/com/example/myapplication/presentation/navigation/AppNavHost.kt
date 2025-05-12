//package com.example.myapplication.presentation.navigation
//
//import androidx.compose.runtime.Composable
//import androidx.compose.runtime.LaunchedEffect
//import androidx.compose.runtime.remember
//import androidx.compose.ui.platform.LocalContext
//import androidx.lifecycle.viewmodel.compose.viewModel
//import androidx.navigation.NavType
//import androidx.navigation.compose.NavHost
//import androidx.navigation.compose.composable
//import androidx.navigation.compose.rememberNavController
//import androidx.navigation.navArgument
//import com.example.myapplication.SystemUiController
//import com.example.myapplication.data.model.AuthPreferences
//import com.example.myapplication.data.repository.AuthRepository
//import com.example.myapplication.presentation.auth.LoginScreen
//import com.example.myapplication.presentation.auth.LoginViewModel
//import com.example.myapplication.presentation.auth.LoginViewModelFactory
//import com.example.myapplication.presentation.auth.RegisterScreen
//import com.example.myapplication.presentation.screens.admin.AdminAppointmentsScreen
//import com.example.myapplication.presentation.screens.admin.AdminAddScreen
//import com.example.myapplication.presentation.screens.admin.AdminDashboardScreen
//import com.example.myapplication.presentation.screens.admin.AdminProfileScreen
//import com.example.myapplication.presentation.screens.admin.AdminUsersScreen
//import com.example.myapplication.presentation.screens.admin.NotificationsScreen
//import com.example.myapplication.presentation.screens.doctor.DoctorDashboardScreen
//import com.example.myapplication.presentation.screens.intro.IntroScreen
//import com.example.myapplication.presentation.screens.patient.AppointmentBookingScreen
//import com.example.myapplication.presentation.screens.patient.DoctorChatScreen
//import com.example.myapplication.presentation.screens.patient.DoctorDetailScreen
//import com.example.myapplication.presentation.screens.patient.DoctorsScreen
//import com.example.myapplication.presentation.screens.patient.MedicalHistoryScreen
//import com.example.myapplication.presentation.screens.patient.PatientDashboardScreen
//import com.example.myapplication.presentation.screens.patient.PrescriptionScreen
//import com.example.myapplication.presentation.screens.splash.SplashScreen
//import com.example.myapplication.presentation.screens.triage.TriageHomeScreen
//import com.example.myapplication.presentation.screens.triage.TriagePatientDetailsScreen
//import java.net.URLDecoder
//import java.net.URLEncoder
//
//@Composable
//fun AppNavHost() {
//    SystemUiController()
//    val navController = rememberNavController()
//    val context = LocalContext.current
//    val authPreferences = remember { AuthPreferences(context) }
//    val loginViewModel: LoginViewModel = viewModel(
//        factory = LoginViewModelFactory(AuthRepository(), authPreferences)
//    )
//
//    LaunchedEffect(Unit) {
//        if (authPreferences.isLoggedIn()) {
//            val name = authPreferences.getName() ?: "Guest"
//            val encodedName = URLEncoder.encode(name, "UTF-8")
//            val role = authPreferences.getRole() ?: "patient"
//            val destination = when (role.lowercase()) {
//                "patient" -> "patient_dashboard/$encodedName"
//                "doctor" -> "doctor_dashboard/$encodedName"
//                "admin" -> "admin_dashboard/$encodedName"
//                "triage" -> "triage_home"
//                else -> "patient_dashboard/$encodedName"
//            }
//            navController.navigate(destination) {
//                popUpTo("splash") { inclusive = true }
//                launchSingleTop = true
//            }
//        }
//    }
//
//    NavHost(navController = navController, startDestination = "splash") {
//        composable("splash") {
//            SplashScreen(navController)
//        }
//        composable("intro1") {
//            IntroScreen(navController, screenNumber = 0)
//        }
//        composable("intro2") {
//            IntroScreen(navController, screenNumber = 1)
//        }
//        composable("intro3") {
//            IntroScreen(navController, screenNumber = 2)
//        }
//        composable("login") {
//            LoginScreen(navController, loginViewModel, authPreferences)
//        }
//        composable("register") {
//            RegisterScreen(navController, authPreferences)
//        }
//        composable(
//            route = "patient_dashboard/{name}",
//            arguments = listOf(navArgument("name") { type = NavType.StringType })
//        ) { backStackEntry ->
//            val name = backStackEntry.arguments?.getString("name")?.let { URLDecoder.decode(it, "UTF-8") } ?: "Guest"
//            PatientDashboardScreen(navController, name, authPreferences)
//        }
//        composable(
//            route = "doctor_dashboard/{name}",
//            arguments = listOf(navArgument("name") { type = NavType.StringType })
//        ) { backStackEntry ->
//            val name = backStackEntry.arguments?.getString("name")?.let { URLDecoder.decode(it, "UTF-8") } ?: "Guest"
//            // Replace with actual DoctorDashboardScreen
//            // Placeholder
//        }
//        composable("appointment_booking") {
//            AppointmentBookingScreen(navController, authPreferences)
//        }
//        composable("doctors") {
//            DoctorDashboardScreen(navController, authPreferences)
//        }
//        composable(
//            "doctor_detail/{doctorName}",
//            arguments = listOf(navArgument("doctorName") { type = NavType.StringType })
//        ) { backStackEntry ->
//            val doctorName = backStackEntry.arguments?.getString("doctorName") ?: ""
//            DoctorDetailScreen(navController, doctorName, authPreferences)
//        }
//        composable(
//            route = "admin_dashboard/{name}",
//            arguments = listOf(navArgument("name") { type = NavType.StringType })
//        ) { backStackEntry ->
//            val name = backStackEntry.arguments?.getString("name")?.let { URLDecoder.decode(it, "UTF-8") } ?: "Guest"
//            AdminDashboardScreen(navController, name, authPreferences)
//        }
//        composable(
//            route = "admin_users/{name}",
//            arguments = listOf(navArgument("name") { type = NavType.StringType })
//        ) { backStackEntry ->
//            val name = backStackEntry.arguments?.getString("name")?.let { URLDecoder.decode(it, "UTF-8") } ?: "Guest"
//            AdminUsersScreen(navController, name, authPreferences)
//        }
//        composable(
//            route = "admin_add/{name}",
//            arguments = listOf(navArgument("name") { type = NavType.StringType })
//        ) { backStackEntry ->
//            val name = backStackEntry.arguments?.getString("name")?.let { URLDecoder.decode(it, "UTF-8") } ?: "Admin"
//            AdminAddScreen(
//                navController = navController,
//                name = name,
//                authPreferences = authPreferences
//            )
//        }
//        composable(
//            route = "admin_appointments/{name}",
//            arguments = listOf(navArgument("name") { type = NavType.StringType })
//        ) { backStackEntry ->
//            val name = backStackEntry.arguments?.getString("name")?.let { URLDecoder.decode(it, "UTF-8") } ?: "Guest"
//            AdminAppointmentsScreen(navController, name)
//        }
//        composable("admin_profile") {
//            AdminProfileScreen(navController)
//        }
//        composable("medical_history") {
//            MedicalHistoryScreen(navController, authPreferences)
//        }
//        composable("prescriptions") {
//            PrescriptionScreen(navController, authPreferences)
//        }
//        composable("doctor_chat") {
//            DoctorChatScreen(navController, authPreferences)
//        }
//        composable("notifications") {
//            NotificationsScreen(navController) { navController.popBackStack() }
//        }
//        composable("triage_home") {
//            TriageHomeScreen(navController, authPreferences)
//        }
//        composable(
//            route = "triage_patient_details/{patientId}",
//            arguments = listOf(navArgument("patientId") { type = NavType.StringType })
//        ) { backStackEntry ->
//            val patientId = backStackEntry.arguments?.getString("patientId") ?: ""
//            TriagePatientDetailsScreen(navController, authPreferences, patientId)
//        }
//    }
//}
package com.example.myapplication.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.myapplication.SystemUiController
import com.example.myapplication.data.model.AuthPreferences
import com.example.myapplication.data.repository.AuthRepository
import com.example.myapplication.presentation.auth.LoginScreen
import com.example.myapplication.presentation.auth.LoginViewModel
import com.example.myapplication.presentation.auth.LoginViewModelFactory
import com.example.myapplication.presentation.auth.RegisterScreen
import com.example.myapplication.presentation.screens.admin.AdminAppointmentsScreen
import com.example.myapplication.presentation.screens.admin.AdminAddScreen
import com.example.myapplication.presentation.screens.admin.AdminDashboardScreen
import com.example.myapplication.presentation.screens.admin.AdminProfileScreen
import com.example.myapplication.presentation.screens.admin.AdminUsersScreen
import com.example.myapplication.presentation.screens.admin.NotificationsScreen
import com.example.myapplication.presentation.screens.doctor.AppointmentsScreen
import com.example.myapplication.presentation.screens.doctor.DoctorDashboardScreen
import com.example.myapplication.presentation.screens.doctor.DoctorProfileScreen
import com.example.myapplication.presentation.screens.doctor.MedicalRecordDetailsScreen
import com.example.myapplication.presentation.screens.doctor.PatientDetailsScreen
import com.example.myapplication.presentation.screens.doctor.PrescriptionDetailsScreen
import com.example.myapplication.presentation.screens.intro.IntroScreen
import com.example.myapplication.presentation.screens.patient.AppointmentBookingScreen
import com.example.myapplication.presentation.screens.patient.DoctorChatScreen
import com.example.myapplication.presentation.screens.patient.DoctorDetailScreen
import com.example.myapplication.presentation.screens.patient.DoctorsScreen
import com.example.myapplication.presentation.screens.patient.MedicalHistoryScreen
import com.example.myapplication.presentation.screens.patient.PatientDashboardScreen
import com.example.myapplication.presentation.screens.patient.PrescriptionScreen
import com.example.myapplication.presentation.screens.splash.SplashScreen
import com.example.myapplication.presentation.screens.triage.TriageHomeScreen
import com.example.myapplication.presentation.screens.triage.TriagePatientDetailsScreen
import java.net.URLDecoder
import java.net.URLEncoder

@Composable
fun AppNavHost() {
    SystemUiController()
    val navController = rememberNavController()
    val context = LocalContext.current
    val authPreferences = remember{ AuthPreferences(context) }
    val loginViewModel: LoginViewModel = viewModel(
        factory = LoginViewModelFactory(AuthRepository(), authPreferences)
    )

    LaunchedEffect(Unit) {
        if (authPreferences.isLoggedIn()) {
            val name = authPreferences.getName() ?: "Guest"
            val encodedName = URLEncoder.encode(name, "UTF-8")
            val role = authPreferences.getRole() ?: "patient"
            val destination = when (role.lowercase()) {
                "patient" -> "patient_dashboard/$encodedName"
                "doctor" -> "doctor_dashboard/$encodedName"
                "admin" -> "admin_dashboard/$encodedName"
                "triage" -> "triage_home"
                else -> "patient_dashboard/$encodedName"
            }
            navController.navigate(destination) {
                popUpTo("splash") { inclusive = true }
                launchSingleTop = true
            }
        }
    }

    NavHost(navController = navController, startDestination = "splash") {
        composable("splash") {
            SplashScreen(navController)
        }
        composable("intro1") {
            IntroScreen(navController, screenNumber = 0)
        }
        composable("intro2") {
            IntroScreen(navController, screenNumber = 1)
        }
        composable("intro3") {
            IntroScreen(navController, screenNumber = 2)
        }
        composable("login") {
            LoginScreen(navController, loginViewModel, authPreferences)
        }
        composable("register") {
            RegisterScreen(navController, authPreferences)
        }
        composable(
            route = "patient_dashboard/{name}",
            arguments = listOf(navArgument("name") { type = NavType.StringType })
        ) { backStackEntry ->
            val name = backStackEntry.arguments?.getString("name")?.let { URLDecoder.decode(it, "UTF-8") } ?: "Guest"
            PatientDashboardScreen(navController, name, authPreferences)
        }
        composable(
            route = "doctor_dashboard/{name}",
            arguments = listOf(navArgument("name") { type = NavType.StringType })
        ) { backStackEntry ->
            val name = backStackEntry.arguments?.getString("name")?.let { URLDecoder.decode(it, "UTF-8") } ?: "Guest"
            DoctorDashboardScreen(navController, authPreferences)
        }
        composable("appointment_booking") {
            AppointmentBookingScreen(navController, authPreferences)
        }
        composable("doctors") {
            DoctorsScreen(navController, authPreferences)
        }
        composable(
            "doctor_detail/{doctorName}",
            arguments = listOf(navArgument("doctorName") { type = NavType.StringType })
        ) { backStackEntry ->
            val doctorName = backStackEntry.arguments?.getString("doctorName") ?: ""
            DoctorDetailScreen(navController, doctorName, authPreferences)
        }
        composable(
            route = "admin_dashboard/{name}",
            arguments = listOf(navArgument("name") { type = NavType.StringType })
        ) { backStackEntry ->
            val name = backStackEntry.arguments?.getString("name")?.let { URLDecoder.decode(it, "UTF-8") } ?: "Guest"
            AdminDashboardScreen(navController, name, authPreferences)
        }
        composable(
            route = "admin_users/{name}",
            arguments = listOf(navArgument("name") { type = NavType.StringType })
        ) { backStackEntry ->
            val name = backStackEntry.arguments?.getString("name")?.let { URLDecoder.decode(it, "UTF-8") } ?: "Guest"
            AdminUsersScreen(navController, name, authPreferences)
        }
        composable(
            route = "admin_add/{name}",
            arguments = listOf(navArgument("name") { type = NavType.StringType })
        ) { backStackEntry ->
            val name = backStackEntry.arguments?.getString("name")?.let { URLDecoder.decode(it, "UTF-8") } ?: "Admin"
            AdminAddScreen(
                navController = navController,
                name = name,
                authPreferences = authPreferences
            )
        }
        composable(
            route = "admin_appointments/{name}",
            arguments = listOf(navArgument("name") { type = NavType.StringType })
        ) { backStackEntry ->
            val name = backStackEntry.arguments?.getString("name")?.let { URLDecoder.decode(it, "UTF-8") } ?: "Guest"
            AdminAppointmentsScreen(navController, name)
        }
        composable("admin_profile") {
            AdminProfileScreen(navController)
        }
        composable("medical_history/{name}") { backStackEntry ->
            val name = backStackEntry.arguments?.getString("name") ?: "Patient"
            MedicalHistoryScreen(navController, name, authPreferences)
        }
        composable("prescriptions") {
            PrescriptionScreen(navController, authPreferences)
        }
        composable("doctor_chat") {
            DoctorChatScreen(navController, authPreferences)
        }
        composable("notifications") {
            NotificationsScreen(navController) { navController.popBackStack() }
        }
        composable("triage_home") {
            TriageHomeScreen(navController, authPreferences)
        }
        composable(
            route = "triage_patient_details/{patientId}",
            arguments = listOf(navArgument("patientId") { type = NavType.StringType })
        ) { backStackEntry ->
            val patientId = backStackEntry.arguments?.getString("patientId") ?: ""
            TriagePatientDetailsScreen(navController, authPreferences, patientId)
        }
        composable(
            route = "patient_details/{patientId}",
            arguments = listOf(navArgument("patientId") { type = NavType.StringType })
        ) { backStackEntry ->
            val patientId = backStackEntry.arguments?.getString("patientId") ?: ""
            PatientDetailsScreen(navController, patientId, authPreferences)
        }
        composable("profile") {
            DoctorProfileScreen(navController, authPreferences)
        }
        composable("appointments") {
            AppointmentsScreen(navController, authPreferences)
        }
        composable(
            route = "medical_record_details/{recordId}",
            arguments = listOf(navArgument("recordId") { type = NavType.StringType })
        ) { backStackEntry ->
            val recordId = backStackEntry.arguments?.getString("recordId") ?: ""
            MedicalRecordDetailsScreen(navController, recordId, authPreferences)
        }
        composable("prescription_details/{prescriptionId}") { backStackEntry ->
            val prescriptionId = backStackEntry.arguments?.getString("prescriptionId") ?: ""
            PrescriptionDetailsScreen(navController, prescriptionId, authPreferences)
        }
    }
}