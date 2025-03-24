package com.example.ai_chat_compose.ui.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.*
import androidx.navigation.NavController
import com.example.ai_chat_compose.presentation.viewmodel.MainViewModel
import kotlinx.coroutines.launch

@Composable
fun OnboardingScreen(navController: NavController, viewModel: MainViewModel) {
    val scope = rememberCoroutineScope()

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "Onboarding Screen")
        Button(onClick = {
            scope.launch {
                viewModel.saveOnboardingStatus(true)
                navController.navigate("home") {
                    popUpTo("onboarding") { inclusive = true }
                }
            }
        }) {
            Text("Finish Onboarding")
        }
    }
}
