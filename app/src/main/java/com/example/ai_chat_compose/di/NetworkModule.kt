package com.example.ai_chat_compose.di

import android.content.Context
import androidx.room.Room
import com.example.ai_chat_compose.data.dao.ChatMessageDao
import com.example.ai_chat_compose.data.dao.ConversationDao
import com.example.ai_chat_compose.data.db.AppDatabase
import com.example.ai_chat_compose.data.repo.ChatRepository
import com.example.ai_chat_compose.util.Const
import com.google.ai.client.generativeai.GenerativeModel
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.qualifiers.ApplicationContext

@Module
@InstallIn(ViewModelComponent::class) // Only for ViewModel Scope
object NetworkModule {

    @Provides
    fun provideGenerativeModel(): GenerativeModel {
        return GenerativeModel(
            modelName = "gemini-1.5-flash-latest",
            apiKey = Const.API_KEY
        )
    }
    @Provides
    fun provideDatabase(@ApplicationContext context: Context): AppDatabase =
        Room.databaseBuilder(context, AppDatabase::class.java, "chat-db").build()

    @Provides
    fun provideConversationDao(db: AppDatabase) = db.conversationDao()

    @Provides
    fun provideChatMessageDao(db: AppDatabase) = db.chatMessageDao()

    @Provides
    fun provideRepository(
        conversationDao: ConversationDao,
        chatMessageDao: ChatMessageDao
    ) = ChatRepository(conversationDao, chatMessageDao)

}
