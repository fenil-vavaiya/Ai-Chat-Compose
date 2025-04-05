package com.example.ai_chat_compose.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.UUID

@Entity(tableName = "conversations")
data class Conversation(
    @PrimaryKey val id: String = UUID.randomUUID().toString(),
    val userId: String,
    val title: String = "New Chat",
    val createdAt: Long = System.currentTimeMillis()
)
