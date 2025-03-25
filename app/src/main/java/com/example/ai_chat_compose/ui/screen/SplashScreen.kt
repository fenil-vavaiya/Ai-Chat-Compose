package com.example.ai_chat_compose.ui.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.ai_chat_compose.R
import com.example.ai_chat_compose.ui.theme.Theme

@Composable
fun SplashScreen() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(color = Theme),
    ) {
        Image(
            painterResource(R.drawable.ic_logo),
            contentDescription = "Logo",
            modifier = Modifier
                .size(200.dp)
                .align(alignment = Alignment.Center)
        )
    }
}
