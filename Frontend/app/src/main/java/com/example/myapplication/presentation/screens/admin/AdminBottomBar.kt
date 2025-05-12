package com.example.myapplication.presentation.screens.admin

//
//import androidx.compose.foundation.clickable
//import androidx.compose.foundation.layout.*
//import androidx.compose.foundation.shape.RoundedCornerShape
//import androidx.compose.material3.Icon
//import androidx.compose.material3.NavigationBar
//import androidx.compose.runtime.Composable
//import androidx.compose.runtime.getValue
//import androidx.compose.ui.Alignment
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.draw.clip
//import androidx.compose.ui.graphics.Color
//import androidx.compose.ui.res.painterResource
//import androidx.compose.ui.unit.dp
//import androidx.navigation.NavController
//import androidx.navigation.compose.currentBackStackEntryAsState
//import com.example.myapplication.R
//import java.net.URLEncoder
//
//@Composable
//fun AdminBottomBar(navController: NavController, name: String) {
//    val encodedName = URLEncoder.encode(name, "UTF-8")
//    val adminItems = listOf(
//        BottomNavItem("Dashboard", "admin_dashboard/$encodedName", R.drawable.ic_home_unselected, R.drawable.ic_home_selected),
//        BottomNavItem("Users", "admin_users", R.drawable.ic_users_unselected, R.drawable.ic_users_selected),
//        BottomNavItem("Add", "admin_add", R.drawable.ic_add_unselected, R.drawable.ic_add_selected),
//        BottomNavItem("Appointments", "admin_appointments", R.drawable.ic_appointments_unselected, R.drawable.ic_appointments_selected)
//    )
//
//    NavigationBar(
//        containerColor = Color.White,
//        modifier = Modifier
//            .padding(horizontal = 16.dp, vertical = 16.dp)
//            .fillMaxWidth()
//            .height(50.dp)
//            .clip(RoundedCornerShape(32.dp)),
//        contentColor = Color.Transparent
//    ) {
//        val currentRoute by navController.currentBackStackEntryAsState()
//        val currentDestination = currentRoute?.destination?.route
//
//        Row(modifier = Modifier.fillMaxWidth()) {
//            adminItems.forEach { item ->
//                Box(
//                    modifier = Modifier
//                        .weight(1f)
//                        .height(70.dp)
//                        .clickable {
//                            if (currentDestination != item.route) {
//                                navController.navigate(item.route) {
//                                    popUpTo(navController.graph.startDestinationId) {
//                                        saveState = true
//                                    }
//                                    launchSingleTop = true
//                                    restoreState = true
//                                }
//                            }
//                        }
//                ) {
//                    Icon(
//                        painter = painterResource(
//                            id = if (currentDestination == item.route) item.selectedIcon else item.unselectedIcon
//                        ),
//                        contentDescription = null,
//                        modifier = Modifier
//                            .size(30.dp)
//                            .align(Alignment.Center),
//                        tint = Color.Unspecified
//                    )
//                }
//            }
//        }
//    }
//}
//
//data class BottomNavItem(
//    val title: String,
//    val route: String,
//    val unselectedIcon: Int,
//    val selectedIcon: Int
//)


import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.myapplication.R
import java.net.URLEncoder

@Composable
fun AdminBottomBar(navController: NavController, name: String) {
    val encodedName = URLEncoder.encode(name, "UTF-8")
    val adminItems = listOf(
        BottomNavItem("Dashboard", "admin_dashboard/$encodedName", R.drawable.ic_home_unselected, R.drawable.ic_home_selected),
        BottomNavItem("Users", "admin_users/$encodedName", R.drawable.ic_users_unselected, R.drawable.ic_users_selected),
        BottomNavItem("Add", "admin_add/$encodedName", R.drawable.ic_add_unselected, R.drawable.ic_add_selected),
        BottomNavItem("Appointments", "admin_appointments/$encodedName", R.drawable.ic_appointments_unselected, R.drawable.ic_appointments_selected)
    )

    NavigationBar(
        containerColor = Color(0xFF6B5FF8), // Temporary for debugging
        modifier = Modifier
            .padding(horizontal = 16.dp, vertical = 16.dp)
            .fillMaxWidth()
            .height(60.dp)
            .clip(RoundedCornerShape(32.dp)),
        contentColor = Color.Transparent,
        tonalElevation = NavigationBarDefaults.Elevation // 8.dp elevation
    ) {
        val currentRoute by navController.currentBackStackEntryAsState()
        val currentDestination = currentRoute?.destination?.route

        Row(modifier = Modifier.fillMaxWidth()) {
            adminItems.forEach { item ->
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .height(60.dp)
                        .clickable {
                            if (currentDestination != item.route) {
                                navController.navigate(item.route) {
                                    popUpTo(navController.graph.startDestinationId) {
                                        saveState = true
                                    }
                                    launchSingleTop = true
                                    restoreState = true
                                }
                            }
                        }
                ) {
                    Icon(
                        painter = painterResource(
                            id = if (currentDestination == item.route) item.selectedIcon else item.unselectedIcon
                        ),
                        contentDescription = item.title,
                        modifier = Modifier
                            .size(30.dp)
                            .align(Alignment.Center),
                        tint = Color.Unspecified
                    )
                }
            }
        }
    }
}

data class BottomNavItem(
    val title: String,
    val route: String,
    val unselectedIcon: Int,
    val selectedIcon: Int
)