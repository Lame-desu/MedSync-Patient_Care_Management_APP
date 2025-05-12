package com.example.myapplication.presentation.screens.intro

import androidx.compose.runtime.*
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import androidx.compose.runtime.LaunchedEffect

@Composable
fun IntroScreen(
    navController: NavHostController,
    screenNumber: Int,
    viewModel: IntroViewModel = viewModel()
) {
    val state by viewModel.introScreenState.collectAsStateWithLifecycle()

    LaunchedEffect(screenNumber) {
        viewModel.setScreen(screenNumber)
    }

    BaseIntroScreen(
        screenNumber = screenNumber,
        title = state.title,
        description = state.description,
        imageRes = state.imageRes,
        onContinue = {
            val next = when (screenNumber) {
                0 -> "intro2"
                1 -> "intro3"
                else -> "login"
            }
            navController.navigate(next) {
                popUpTo("intro1") { inclusive = false }
            }
        },
        onSkip = {
            navController.navigate("login") {
                popUpTo("intro1") { inclusive = true }
            }
        }
    )
}
