package com.example.ai_chat_compose.di

import com.example.ai_chat_compose.util.Const
import com.google.ai.client.generativeai.GenerativeModel
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

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
}
