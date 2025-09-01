package com.phantom.smartspend.network

import com.phantom.smartspend.network.model.response.LogoutResponse
import com.phantom.smartspend.network.model.response.SignInResponse
import com.phantom.smartspend.network.request_models.SignInRequest
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST

interface ApiService {

    //Auth
    @POST("auth/google")
    suspend fun signIn(@Body request: SignInRequest): SignInResponse

    @POST("auth/logout")
    suspend fun logout(@Header("Authorization") accessToken: String): LogoutResponse


    //Transactions
//    @GET("transactions")
//    suspend fun getTransactions()


}