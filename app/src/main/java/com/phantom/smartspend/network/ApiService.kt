package com.phantom.smartspend.network

import com.phantom.smartspend.network.model.request.UpdateUserRequest
import com.phantom.smartspend.network.model.response.LogoutResponse
import com.phantom.smartspend.network.model.response.RefreshTokenResponse
import com.phantom.smartspend.network.model.response.SignInResponse
import com.phantom.smartspend.network.model.response.UpdateUserResponse
import com.phantom.smartspend.network.model.response.UserResponse
import com.phantom.smartspend.network.request_models.SignInRequest
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.PATCH
import retrofit2.http.POST

interface ApiService {

    //Auth
    @POST("auth/google")
    suspend fun signIn(@Body request: SignInRequest): SignInResponse

    @POST("auth/logout")
    suspend fun logout(): LogoutResponse

    @POST("token")
    suspend fun refreshToken(@Header("Refresh-Token") refreshToken: String): RefreshTokenResponse



    //User data
    @GET("user/me")
    suspend fun getUserData(): UserResponse

    @PATCH("user/update")
    suspend fun updateUserData(@Body request: UpdateUserRequest): UpdateUserResponse


    //Transactions
//    @GET("transactions")
//    suspend fun getTransactions()


}