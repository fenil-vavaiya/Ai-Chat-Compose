package com.example.ai_chat_compose.ui.screen.home.bottombar

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.ai_chat_compose.ui.screen.home.HomeScreen
import com.example.ai_chat_compose.ui.screen.imagesummary.ImageSummaryScreen
import com.example.ai_chat_compose.ui.screen.news.NewsScreen

@Composable
fun CustomBottomNavApp() {
    val navController = rememberNavController()

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = Color.Black,
        bottomBar = {
            CustomMinimalBottomNav(navController)
        }
    ) { paddingValues ->
        NavHost(
            navController = navController,
            startDestination = Screens.Chat.route,
            modifier = Modifier.padding(paddingValues)
        ) {
            composable(Screens.Chat.route) { HomeScreen() }
            composable(Screens.ImageSummary.route) { ImageSummaryScreen() }
            composable(Screens.News.route) { NewsScreen() }
        }
    }
}
