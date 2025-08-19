package com.phantom.smartspend.network

import com.phantom.smartspend.network.request_models.signInResponse
import retrofit2.http.Body
import retrofit2.http.POST

interface ApiService {

    //Auth
    @POST("auth/signin/google")
    suspend fun signIn(@Body request: signInResponse): String

}