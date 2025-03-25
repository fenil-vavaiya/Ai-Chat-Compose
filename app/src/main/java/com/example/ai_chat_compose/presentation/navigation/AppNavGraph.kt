package com.example.ai_chat_compose.presentation.navigation

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.navigation.NavHostController
import com.example.ai_chat_compose.presentation.viewmodel.MainViewModel
import com.example.ai_chat_compose.ui.screen.HomeScreen
import com.example.ai_chat_compose.ui.screen.OnboardingScreen
import com.example.ai_chat_compose.ui.screen.SignUpScreen
import com.example.ai_chat_compose.ui.screen.SplashScreen
import com.google.accompanist.navigation.animation.AnimatedNavHost
import com.google.accompanist.navigation.animation.composable
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.first

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun AppNavGraph(navController: NavHostController, viewModel: MainViewModel) {
    var isSplashFinished by remember { mutableStateOf(false) }
    var isLoggedIn by remember { mutableStateOf<Boolean?>(null) }
    var isOnboardingSeen by remember { mutableStateOf<Boolean?>(null) }

    LaunchedEffect(Unit) {
        delay(3000)
        isLoggedIn = viewModel.isLoggedIn.first()
        isOnboardingSeen = viewModel.isOnboardingSeen.first()
        isSplashFinished = true
    }

    if (!isSplashFinished || isLoggedIn == null || isOnboardingSeen == null) {
        SplashScreen()
        return
    }

    val destination = when {
        isLoggedIn == true -> if (isOnboardingSeen == true) "home" else "onboarding"
        else -> "signup"
    }

    LaunchedEffect(destination) {
        navController.navigate(destination) {
            popUpTo("splash") { inclusive = true }
        }
    }

    AnimatedNavHost(
        navController = navController,
        startDestination = "splash",
        enterTransition = { slideInHorizontally(initialOffsetX = { it },animationSpec = tween(1500)) },
        exitTransition = { slideOutHorizontally(targetOffsetX = { it },animationSpec = tween(1500)) },
        popEnterTransition = { slideInHorizontally(initialOffsetX = { -it },animationSpec = tween(1500)) },
        popExitTransition = {  slideOutHorizontally(targetOffsetX = { it }, animationSpec = tween(1500)) }
    ) {
        composable("splash") { SplashScreen() }
        composable("signup") { SignUpScreen(navController, viewModel) }
        composable("onboarding") { OnboardingScreen(navController, viewModel) }
        composable("home") { HomeScreen() }
    }
}


