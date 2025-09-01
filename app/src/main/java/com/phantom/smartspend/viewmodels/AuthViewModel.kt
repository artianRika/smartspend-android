package com.phantom.smartspend.viewmodels

import android.net.Uri
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.phantom.smartspend.data.model.UserData
import kotlinx.coroutines.launch
import com.phantom.smartspend.data.repository.AuthRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class AuthViewModel(private val repo: AuthRepository) : ViewModel() {

    private val _userData = MutableStateFlow<UserData?>(null)
    val userData: StateFlow<UserData?> = _userData

    private val _isAuthenticated = MutableStateFlow(false)
    val isAuthenticated: StateFlow<Boolean> = _isAuthenticated

    private val _navigateToWelcome = MutableStateFlow(false)
    val navigateToWelcome: StateFlow<Boolean> = _navigateToWelcome

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage


    @RequiresApi(Build.VERSION_CODES.UPSIDE_DOWN_CAKE)
    fun signInWithGoogleNative() {
        viewModelScope.launch {
            try {
                val backendResponse = repo.signInWithGoogleNative()
                _isAuthenticated.value = true

                _userData.value = backendResponse.userData
                Log.d("TOKENS", "is auth: ${_isAuthenticated.value}")
            } catch (e: Exception) {
                _isAuthenticated.value = false
                _errorMessage.value = e.localizedMessage ?: "Unknown error"
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
            try {
                repo.logout()
            } catch (e: Exception) {
                throw e
            }
            repo.clearTokens()
            _isAuthenticated.value = false
            _userData.value = null
            _navigateToWelcome.value = true

            Log.d("TOKENS", "is auth: ${_isAuthenticated.value}")
        }
    }

    fun resetNavigateToWelcome() {
        _navigateToWelcome.value = false
    }

    fun clearError() {
        _errorMessage.value = null
    }
}