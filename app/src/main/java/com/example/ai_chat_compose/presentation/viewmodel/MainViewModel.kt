package com.example.ai_chat_compose.presentation.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.example.ai_chat_compose.data.datastore.DataStoreManager
import kotlinx.coroutines.flow.Flow

class MainViewModel(application: Application) : AndroidViewModel(application) {
    private val dataStoreManager = DataStoreManager(application)

    val isLoggedIn: Flow<Boolean> = dataStoreManager.isLoggedIn
    val isOnboardingSeen: Flow<Boolean> = dataStoreManager.isOnboardingSeen

    suspend fun saveLoginStatus(status: Boolean) {
        dataStoreManager.saveLoginStatus(status)
    }

    suspend fun saveOnboardingStatus(status: Boolean) {
        dataStoreManager.saveOnboardingStatus(status)
    }
}
