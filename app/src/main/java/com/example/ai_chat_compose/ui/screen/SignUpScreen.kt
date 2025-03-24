package com.example.ai_chat_compose.ui.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import com.example.ai_chat_compose.presentation.viewmodel.MainViewModel
import kotlinx.coroutines.launch

@Composable
fun SignUpScreen(navController: NavController, viewModel: MainViewModel) {
    val scope = rememberCoroutineScope()
    
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "Sign Up Screen")
        Button(onClick = {
            scope.launch {
                viewModel.saveLoginStatus(true)
                navController.navigate("onboarding") {
                    popUpTo("signup") { inclusive = true }
                }
            }
        }) {
            Text("Sign Up")
        }
    }
}
