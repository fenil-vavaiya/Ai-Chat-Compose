package com.example.ai_chat_compose.ui.screen.home.bottombar

import com.example.ai_chat_compose.R


sealed class Screens(val route: String, val icon: Int, val label: String) {
    data object Chat : Screens("chat", R.drawable.ic_chat, "Chat")
    data object ImageSummary : Screens("imageSummary", R.drawable.ic_image_generation, "ImageSummary")
    data object News : Screens("news", R.drawable.ic_news, "News")
}
