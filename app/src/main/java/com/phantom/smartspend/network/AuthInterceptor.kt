package com.phantom.smartspend.network

import android.util.Log
import com.phantom.smartspend.data.local.AuthStateListener
import com.phantom.smartspend.data.local.AuthTokenProvider
import okhttp3.Interceptor
import okhttp3.Response

class AuthInterceptor(private val tokenProvider: AuthTokenProvider, private val listener: AuthStateListener? = null) : Interceptor {
    @Volatile
    private var cachedToken: String? = null

    fun updateToken(newToken: String?) {
        cachedToken = newToken
        if (newToken.isNullOrEmpty()) {
            listener?.onTokenInvalidated()
        }
    }

    override fun intercept(chain: Interceptor.Chain): Response {
        val original = chain.request()
        val token = cachedToken

        Log.d("AuthInterceptor", "Injected token: $token")

        val requestBuilder = original.newBuilder()
        if (!token.isNullOrEmpty()) {
            requestBuilder.header("Authorization", "Bearer $token")
        }

        return chain.proceed(requestBuilder.build())
    }
}
