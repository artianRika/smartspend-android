package com.phantom.smartspend.network

import android.util.Log
import com.phantom.smartspend.data.local.AuthStateListener
import com.phantom.smartspend.data.local.AuthTokenProvider
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.Response

class AuthInterceptor(
    private val tokenProvider: AuthTokenProvider,
    private val tokenApi: TokenApi,
    private val listener: AuthStateListener? = null)
    : Interceptor {
    @Volatile
    private var cachedToken: String? = null

    fun updateToken(newToken: String?) {
        cachedToken = newToken
        if (newToken.isNullOrEmpty()) {
            listener?.onTokenInvalidated()
        }
    }

//    override fun intercept(chain: Interceptor.Chain): Response {
//        val original = chain.request()
//        val token = cachedToken
//
//        Log.d("AuthInterceptor", "Injected token: $token")
//
//        val requestBuilder = original.newBuilder()
//        if (!token.isNullOrEmpty()) {
//            requestBuilder.header("Authorization", "Bearer $token")
//        }
//
//        return chain.proceed(requestBuilder.build())
//    }

    override fun intercept(chain: Interceptor.Chain): Response {
        var request = chain.request()
        val token = cachedToken


        Log.d("AuthInterceptor", "Injected token: $token")

        // Add Authorization if we have a token
        if (!token.isNullOrEmpty()) {
            request = request.newBuilder()
                .header("Authorization", "Bearer $token")
                .build()
        }

        var response = chain.proceed(request)

        if (response.code == 401) {
            response.close() // close before retry
            val newToken = runBlocking { refreshAccessToken() }

            if (!newToken.isNullOrEmpty()) {
                updateToken(newToken)
                val newRequest = request.newBuilder()
                    .header("Authorization", "Bearer $newToken")
                    .build()
                response = chain.proceed(newRequest)
            } else {
                listener?.onTokenInvalidated()
            }
        }

        return response
    }

    private suspend fun refreshAccessToken(): String? {
        val accessToken = tokenProvider.getAccessToken() ?: return null
        val refreshToken = tokenProvider.getRefreshToken() ?: return null

        return try {
            val response = tokenApi.refreshToken("Bearer $accessToken", refreshToken)
            tokenProvider.updateAccessToken(response.renewedAccessToken)

            response.renewedAccessToken
        } catch (e: Exception) {
            Log.e("AuthInterceptor", "Token refresh failed", e)
            null
        }
    }


}
