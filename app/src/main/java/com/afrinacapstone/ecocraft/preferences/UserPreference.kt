package com.afrinacapstone.ecocraft.preferences

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "user")

class UserPreference(private val dataStore: DataStore<Preferences>) {

    companion object {
        val ACCESS_TOKEN_KEY = stringPreferencesKey("access_token")
        val REFRESH_TOKEN_KEY = stringPreferencesKey("refresh_token")
        val IS_LOGGED_IN = booleanPreferencesKey("is_logged_in")
        val USER_ID_KEY = stringPreferencesKey("user_id")
    }

    suspend fun saveAccessToken(token: String) {
        dataStore.edit { preferences ->
            preferences[ACCESS_TOKEN_KEY] = token
        }
    }

    val accessToken: Flow<String?> = dataStore.data
        .map { preferences -> preferences[ACCESS_TOKEN_KEY] }
        .catch { emit(null) }

    suspend fun saveLoginStatus(isLoggedIn: Boolean) {
        dataStore.edit { preferences ->
            preferences[IS_LOGGED_IN] = isLoggedIn
        }
    }

    val isLoggedIn: Flow<Boolean> = dataStore.data
        .map { preferences -> preferences[IS_LOGGED_IN] ?: false }
        .catch { emit(false) }

    suspend fun saveUserId(id: String) {
        dataStore.edit { preferences ->
            preferences[USER_ID_KEY] = id
        }
    }

    val userId: Flow<String?> = dataStore.data
        .map { preferences -> preferences[USER_ID_KEY] }
        .catch { emit(null) }
}
