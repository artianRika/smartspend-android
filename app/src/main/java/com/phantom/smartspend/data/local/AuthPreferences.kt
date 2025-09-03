package com.phantom.smartspend.data.local

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

val Context.authDataStore by preferencesDataStore("auth_prefs")

object AuthPreferences {

    private val ACCESS_TOKEN = stringPreferencesKey("access_token")
    private val REFRESH_TOKEN = stringPreferencesKey("refresh_token")
    private val REFRESH_TOKEN_EXPIRY = stringPreferencesKey("refresh_token_expiry")

    suspend fun saveTokens(context: Context, access: String, refresh: String, refreshExpiry: String) {
        val encryptedRefresh = CryptoHelper.encrypt(context, refresh)
        context.authDataStore.edit { prefs ->
            prefs[ACCESS_TOKEN] = access
            prefs[REFRESH_TOKEN] = encryptedRefresh
            prefs[REFRESH_TOKEN_EXPIRY] = refreshExpiry
        }
    }

    suspend fun updateAccessToken(context: Context, access: String){
        context.authDataStore.edit { prefs ->
            prefs[ACCESS_TOKEN] = access
        }
    }

    suspend fun getAccessToken(context: Context): String? {
        return context.authDataStore.data.map { it[ACCESS_TOKEN] }.first()
    }

    suspend fun getRefreshToken(context: Context): String? {
        return context.authDataStore.data.map { prefs ->
            prefs[REFRESH_TOKEN]?.let { CryptoHelper.decrypt(context, it) }
        }.first()
    }

    suspend fun getRefreshExpiry(context: Context): String? {
        return context.authDataStore.data.map { it[REFRESH_TOKEN_EXPIRY] }.first()
    }

    suspend fun clearTokens(context: Context) {
        context.authDataStore.edit { preferences ->
            preferences.remove(ACCESS_TOKEN)
            preferences.remove(REFRESH_TOKEN)
            preferences.remove(REFRESH_TOKEN_EXPIRY)
        }
    }
}
