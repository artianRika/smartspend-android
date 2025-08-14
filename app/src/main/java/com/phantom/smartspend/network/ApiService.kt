package com.phantom.smartspend.network

import com.phantom.smartspend.network.request_models.GetTokens
import retrofit2.http.Body
import retrofit2.http.GET

interface ApiService {

    //Auth
    @GET("auth/signin/google")
    suspend fun getTokens(@Body request: GetTokens): String

}