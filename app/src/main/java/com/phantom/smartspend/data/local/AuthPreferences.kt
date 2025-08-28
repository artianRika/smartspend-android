package com.phantom.smartspend.data.local

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

val Context.authDataStore by preferencesDataStore(name = "auth_prefs")

object AuthPreferences {
    private val ACCESS_TOKEN = stringPreferencesKey("access_token")
    private val REFRESH_TOKEN = stringPreferencesKey("refresh_token")

    suspend fun saveTokens(context: Context, access: String, refresh: String) {
        context.authDataStore.edit { prefs ->
            prefs[ACCESS_TOKEN] = access
            prefs[REFRESH_TOKEN] = refresh
        }
    }

    suspend fun getAccessToken(context: Context): String? {
        return context.authDataStore.data.map { prefs ->
            prefs[ACCESS_TOKEN]
        }.first()
    }

    suspend fun getRefreshToken(context: Context): String? {
        return context.authDataStore.data.map { prefs ->
            prefs[REFRESH_TOKEN]
        }.first()
    }

    suspend fun clearTokens(context: Context) {
        context.authDataStore.edit { prefs ->
            prefs.clear()
        }
    }
}