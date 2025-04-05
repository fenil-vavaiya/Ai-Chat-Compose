package com.example.ai_chat_compose.data.model

import androidx.room.Embedded
import androidx.room.Relation

data class ConversationWithMessages(
    @Embedded val conversation: Conversation,
    @Relation(
        parentColumn = "id",
        entityColumn = "conversationId"
    )
    val messages: List<ChatMessage>
)
