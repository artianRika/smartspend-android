package com.phantom.smartspend.viewmodels

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.phantom.smartspend.data.local.AuthStateListener
import com.phantom.smartspend.data.local.AuthTokenProvider
import com.phantom.smartspend.data.model.UserData
import com.phantom.smartspend.data.repository.AuthRepository
import com.phantom.smartspend.network.AuthInterceptor
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.time.Instant
import java.time.OffsetDateTime
import java.time.format.DateTimeFormatter
import kotlin.math.log

class AuthViewModel(private val repo: AuthRepository,
                    private val tokenProvider: AuthTokenProvider,
                    private val authInterceptor: AuthInterceptor,
                    private val userViewModel: UserViewModel
) : ViewModel(), AuthStateListener {


    private val _isAuthenticated = MutableStateFlow(false)
    val isAuthenticated: StateFlow<Boolean> = _isAuthenticated

    fun setIsAuthenticated(isAuth: Boolean){
        _isAuthenticated.value = isAuth
    }

    override fun onTokenInvalidated() {
        _isAuthenticated.value = false
        logout()
    }

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


                userViewModel.setUserData(backendResponse.userData)
                Log.d("AuthInterceptor", "is auth: ${_isAuthenticated.value}")
            } catch (e: Exception) {
                _isAuthenticated.value = false
                _errorMessage.value = e.localizedMessage ?: "Unknown error"
            }
        }
    }

    fun checkAuthStatus() {
        viewModelScope.launch {
            val accessToken = tokenProvider.getAccessToken()
            val refreshToken = tokenProvider.getRefreshToken()


            val refreshExpiry = try {
                val expiryString = tokenProvider.getRefreshExpiry()
                if (expiryString != null) {
                    val odt = OffsetDateTime.parse(expiryString, DateTimeFormatter.ISO_OFFSET_DATE_TIME)
                    odt.toInstant().toEpochMilli()
                } else {
                    0L
                }
            } catch (e: Exception) {
                Log.e("AuthInterceptor", "Failed to parse refresh expiry", e)
                0L
            }

            authInterceptor.updateToken(accessToken)

            _isAuthenticated.value = refreshToken != null && System.currentTimeMillis() < refreshExpiry
            Log.d("AuthInterceptor",
                """refresh: $refreshToken
                |refreshExpiry: $refreshExpiry
                |isAuth: ${_isAuthenticated.value}
                """)
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
            userViewModel.setUserData(null)
            _navigateToWelcome.value = true

            Log.d("AuthInterceptor", "is auth: ${_isAuthenticated.value}")
        }
    }

    fun resetNavigateToWelcome() {
        _navigateToWelcome.value = false
    }

    fun clearError() {
        _errorMessage.value = null
    }
}