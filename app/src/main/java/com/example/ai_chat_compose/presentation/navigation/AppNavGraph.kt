package com.example.ai_chat_compose.presentation.navigation

import android.window.SplashScreen
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavHostController
import androidx.navigation.compose.*
import com.example.ai_chat_compose.presentation.viewmodel.MainViewModel
import com.example.ai_chat_compose.ui.screen.HomeScreen
import com.example.ai_chat_compose.ui.screen.OnboardingScreen
import com.example.ai_chat_compose.ui.screen.SignUpScreen
import com.example.ai_chat_compose.ui.screen.SplashScreen
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@Composable
fun AppNavGraph(navController: NavHostController, viewModel: MainViewModel) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    
    // States to store login & onboarding status
    var isLoggedIn by remember { mutableStateOf<Boolean?>(null) }
    var isOnboardingSeen by remember { mutableStateOf<Boolean?>(null) }

    // Fetch DataStore values
    LaunchedEffect(Unit) {
        scope.launch {
            viewModel.isLoggedIn.collectLatest { isLoggedIn = it }
        }
        scope.launch {
            viewModel.isOnboardingSeen.collectLatest { isOnboardingSeen = it }
        }
    }

    // Show splash screen until values are loaded
    if (isLoggedIn == null || isOnboardingSeen == null) {
        SplashScreen()
        return
    }

    val startDestination = when {
        isLoggedIn == true -> if (isOnboardingSeen == true) "home" else "onboarding"
        else -> "signup"
    }

    NavHost(navController, startDestination = startDestination) {
        composable("splash") { SplashScreen() }
        composable("signup") { SignUpScreen(navController, viewModel) }
        composable("onboarding") { OnboardingScreen(navController, viewModel) }
        composable("home") { HomeScreen() }
    }
}
