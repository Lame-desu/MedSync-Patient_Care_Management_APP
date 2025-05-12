package com.example.myapplication.presentation.screens.intro

import androidx.lifecycle.ViewModel
import com.example.myapplication.R
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class IntroViewModel : ViewModel() {

    private val _introScreenState = MutableStateFlow(
        IntroScreenState( // Default to first screen
            title = "Your Health, Streamlined and Simple",
            description = "Say goodbye to missed appointments and lost prescriptions. Medsync keeps you connected to your doctor, anytime, from anywhere.",
            imageRes = R.drawable.intro1
        )
    )
    val introScreenState: StateFlow<IntroScreenState> = _introScreenState.asStateFlow()

    fun setScreen(screenNumber: Int) {
        when (screenNumber) {
            0 -> _introScreenState.value = IntroScreenState(
                title = "Your Health, Streamlined and Simple",
                description = "Say goodbye to missed appointments and lost prescriptions. Medsync keeps you connected to your doctor, anytime, from anywhere.",
                imageRes = R.drawable.intro1
            )
            1 -> _introScreenState.value = IntroScreenState(
                title = "Real Time Access to Your Care",
                description = "Chat with your doctor, view your prescriptions, and receive updates without stepping outside. ",
                imageRes = R.drawable.intro2
            )
            2 -> _introScreenState.value = IntroScreenState(
                title = "All Your Medical Info in One Place",
                description = "From past reports to upcoming appointments—Medsync helps you stay organized and informed throughout your health journey.",
                imageRes = R.drawable.intro3
            )
        }
    }
}
