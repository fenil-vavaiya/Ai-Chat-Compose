package com.example.ai_chat_compose.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.ai_chat_compose.data.dao.ChatMessageDao
import com.example.ai_chat_compose.data.dao.ConversationDao
import com.example.ai_chat_compose.data.model.ChatMessage
import com.example.ai_chat_compose.data.model.Conversation

@Database(entities = [Conversation::class, ChatMessage::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun conversationDao(): ConversationDao
    abstract fun chatMessageDao(): ChatMessageDao
}
