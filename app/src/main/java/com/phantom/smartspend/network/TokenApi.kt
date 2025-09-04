package com.phantom.smartspend.network

import com.phantom.smartspend.network.model.response.RefreshTokenResponse
import retrofit2.http.Header
import retrofit2.http.POST

interface TokenApi {

    @POST("token")
    suspend fun refreshToken(
        @Header("Authorization") expiredAccessToken: String,
        @Header("Refresh-Token") refreshToken: String
    ): RefreshTokenResponse
}