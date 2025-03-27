package com.example.ai_chat_compose.data.datastore

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.dataStore by preferencesDataStore("user_prefs")

class DataStoreManager(private val context: Context) {

    private val gson = Gson()

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

    // Generic Save Method for String, Int, Long
    suspend fun <T> saveValue(key: String, value: T) {
        context.dataStore.edit { preferences ->
            when (value) {
                is String -> preferences[stringPreferencesKey(key)] = value
                is Int -> preferences[intPreferencesKey(key)] = value
                is Long -> preferences[longPreferencesKey(key)] = value
                else -> throw IllegalArgumentException("Unsupported type")
            }
        }
    }

    // Generic Retrieve Method for String, Int, Long
    fun <T> getValue(key: String, defaultValue: T): Flow<T> {
        return context.dataStore.data.map { preferences ->
            when (defaultValue) {
                is String -> preferences[stringPreferencesKey(key)] ?: defaultValue
                is Int -> preferences[intPreferencesKey(key)] ?: defaultValue
                is Long -> preferences[longPreferencesKey(key)] ?: defaultValue
                else -> throw IllegalArgumentException("Unsupported type")
            } as T // Safe casting
        }
    }

    // Save Custom Object
    suspend fun <T> saveObject(key: String, obj: T) {
        val json = gson.toJson(obj)
        context.dataStore.edit { it[stringPreferencesKey(key)] = json }
    }

    // Retrieve Custom Object
    fun <T> getObject(key: String, clazz: Class<T>): Flow<T?> {
        return context.dataStore.data.map { preferences ->
            preferences[stringPreferencesKey(key)]?.let { gson.fromJson(it, clazz) }
        }
    }

    // Save List of Objects
    suspend fun <T> saveList(key: String, list: List<T>) {
        val json = gson.toJson(list)
        context.dataStore.edit { it[stringPreferencesKey(key)] = json }
    }

    // Retrieve List of Objects
    fun <T> getList(key: String, clazz: Class<T>): Flow<List<T>> {
        return context.dataStore.data.map { preferences ->
            preferences[stringPreferencesKey(key)]?.let {
                val type = TypeToken.getParameterized(List::class.java, clazz).type
                gson.fromJson(it, type)
            } ?: emptyList()
        }
    }
}
