package com.example.ai_chat_compose.data.datastore

import android.content.Context
import androidx.datastore.preferences.core.*
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.dataStore by preferencesDataStore("user_prefs")

class DataStoreManager(private val context: Context) {

    companion object {
        private val KEY_IS_LOGGED_IN = booleanPreferencesKey("is_logged_in")
        private val KEY_IS_ONBOARDING_SEEN = booleanPreferencesKey("is_onboarding_seen")
    }

    val isLoggedIn: Flow<Boolean> = context.dataStore.data
        .map { it[KEY_IS_LOGGED_IN] ?: false }

    val isOnboardingSeen: Flow<Boolean> = context.dataStore.data
        .map { it[KEY_IS_ONBOARDING_SEEN] ?: false }

    suspend fun saveLoginStatus(isLoggedIn: Boolean) {
        context.dataStore.edit { it[KEY_IS_LOGGED_IN] = isLoggedIn }
    }

    suspend fun saveOnboardingStatus(isSeen: Boolean) {
        context.dataStore.edit { it[KEY_IS_ONBOARDING_SEEN] = isSeen }
    }
}
