package com.phantom.smartspend.network

import com.phantom.smartspend.network.request_models.SignInRequest
import com.phantom.smartspend.network.response_models.SignInResponse
import retrofit2.http.Body
import retrofit2.http.POST

interface ApiService {

    //Auth
    @POST("auth/signin/google")
    suspend fun signIn(@Body request: SignInRequest): SignInResponse

}