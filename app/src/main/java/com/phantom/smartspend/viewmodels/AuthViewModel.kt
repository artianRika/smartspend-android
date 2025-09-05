package com.phantom.smartspend.viewmodels

import android.content.Context
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.phantom.smartspend.data.local.AuthStateListener
import com.phantom.smartspend.data.local.AuthTokenProvider
import com.phantom.smartspend.data.local.OnboardingPreferences
import com.phantom.smartspend.data.repository.AuthRepository
import com.phantom.smartspend.network.AuthInterceptor
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.time.OffsetDateTime
import java.time.format.DateTimeFormatter

class AuthViewModel(private val repo: AuthRepository,
                    private val tokenProvider: AuthTokenProvider,
                    private val authInterceptor: AuthInterceptor,
                    private val userViewModel: UserViewModel
) : ViewModel(), AuthStateListener {


    private val _isAuthenticated = MutableStateFlow(false)
    val isAuthenticated: StateFlow<Boolean> = _isAuthenticated


    override fun onTokenInvalidated() {
        _isAuthenticated.value = false
        logout()
    }

    private val _navigateToLogin = MutableStateFlow(false)
    val navigateToLogin: StateFlow<Boolean> = _navigateToLogin

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage


    @RequiresApi(Build.VERSION_CODES.UPSIDE_DOWN_CAKE)
    fun signInWithGoogleNative(context: Context) {

        viewModelScope.launch {
            try {
                val backendResponse = repo.signInWithGoogleNative()
                _isAuthenticated.value = true


                if(backendResponse.message.contains("signed up")){
                    OnboardingPreferences.setOnboardingDone(context, false)
                }else{
                    OnboardingPreferences.setOnboardingDone(context, true)
                }

                userViewModel.setUserData(backendResponse.userData)

                Log.d("AuthInterceptor", "is auth: ${_isAuthenticated.value}")
            } catch (e: Exception) {
                _isAuthenticated.value = false
                _errorMessage.value = e.localizedMessage ?: "Unknown error"
            }
        }
    }

    fun checkAuthStatus(context: Context) {
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

            if(refreshToken != null && System.currentTimeMillis() < refreshExpiry){
                _isAuthenticated.value = true
                OnboardingPreferences.setOnboardingDone(context, true)
            }else{
                _isAuthenticated.value = false
                OnboardingPreferences.setOnboardingDone(context, false)
            }

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
            _navigateToLogin.value = true
            _isAuthenticated.value = false
            userViewModel.setUserData(null)

            Log.d("AuthInterceptor", "is auth: ${_isAuthenticated.value}")
        }
    }

    fun resetNavigateToLogin() {
        _navigateToLogin.value = false
    }

    fun clearError() {
        _errorMessage.value = null
    }
}