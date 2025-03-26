package com.example.ai_chat_compose.core

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.rememberNavController
import com.example.ai_chat_compose.presentation.navigation.AppNavGraph
import com.example.ai_chat_compose.presentation.viewmodel.MainViewModel
import com.example.ai_chat_compose.ui.theme.AiChatComposeTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            AiChatComposeTheme {
                setContent {
                    val navController = rememberNavController()
                    val mainViewModel: MainViewModel = viewModel()

                    AppNavGraph(navController = navController, viewModel = mainViewModel)
                }
            }
        }
    }
}

