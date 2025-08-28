package com.phantom.smartspend.viewmodels

import android.net.Uri
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import com.phantom.smartspend.data.repository.AuthRepository

class AuthViewModel(private val repo: AuthRepository) : ViewModel() {

    @RequiresApi(Build.VERSION_CODES.UPSIDE_DOWN_CAKE)
    fun signInWithGoogleNative(onResult: (Result<Unit>) -> Unit) {
        viewModelScope.launch {
            try {
                repo.signInWithGoogleNative()
                onResult(Result.success(Unit))
            } catch (e: Exception) {
                onResult(Result.failure(e))
            }
        }
    }

    fun getAccessToken(onResult: (String?) -> Unit) {
        viewModelScope.launch {
            val token = repo.getAccessToken()
            onResult(token)
        }
    }

    fun getRefreshToken(onResult: (String?) -> Unit) {
        viewModelScope.launch {
            val token = repo.getRefreshToken()
            onResult(token)
        }
    }

    fun logout() {
        viewModelScope.launch {
            repo.clearTokens()
        }
    }

}