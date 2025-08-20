package com.phantom.smartspend.data.local

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map


val Context.dataStore by preferencesDataStore("settings")

object OnboardingPreferences {
    private val ONBOARDING_DONE = booleanPreferencesKey("onboarding_done")

    suspend fun isOnboardingDone(context: Context): Boolean {
//        return context.dataStore.data.map { prefs ->
//            prefs[ONBOARDING_DONE] ?: false
//        }.first()
        return false
    }

    suspend fun setOnboardingDone(context: Context, done: Boolean) {
        context.dataStore.edit { prefs ->
            prefs[ONBOARDING_DONE] = done
        }
    }
}