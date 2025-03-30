package com.example.ai_chat_compose.data.model

data class MessageModel(
    val message : String,
    val role : String,
    var hasAnimated: Boolean = false // Default false (new messages animate)
)
