package com.phantom.smartspend.network

import com.phantom.smartspend.network.request_models.SignInRequest
import com.phantom.smartspend.network.model.response.SignInResponse
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface ApiService {

    //Auth
    @POST("auth/google")
    suspend fun signIn(@Body request: SignInRequest): SignInResponse


    //Transactions
//    @GET("transactions")
//    suspend fun getTransactions()


}