package com.phantom.smartspend.data.local

import android.content.Context

class AuthTokenProvider(private val context: Context) {
    suspend fun getAccessToken(): String? {
        return AuthPreferences.getAccessToken(context)
    }

    suspend fun getRefreshToken(): String? {
        return AuthPreferences.getRefreshToken(context)
    }

    suspend fun getRefreshExpiry(): String? {
        return AuthPreferences.getRefreshExpiry(context)
    }
}
