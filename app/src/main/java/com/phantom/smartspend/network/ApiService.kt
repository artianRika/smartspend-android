package com.phantom.smartspend.network

import com.phantom.smartspend.network.model.request.AddTransactionRequest
import com.phantom.smartspend.network.model.request.DeleteTransactionRequest
import com.phantom.smartspend.network.model.request.UpdateUserOnboardingRequest
import com.phantom.smartspend.network.model.request.UpdateUserRequest
import com.phantom.smartspend.network.model.response.LogoutResponse
import com.phantom.smartspend.network.model.response.SignInResponse
import com.phantom.smartspend.network.model.response.TransactionResponse
import com.phantom.smartspend.network.model.response.UpdateUserResponse
import com.phantom.smartspend.network.model.response.UploadImageResponse
import com.phantom.smartspend.network.model.response.UserResponse
import com.phantom.smartspend.network.model.request.SignInRequest
import okhttp3.MultipartBody
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Multipart
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Path

interface ApiService {

    //Auth
    @POST("auth/google")
    suspend fun signIn(@Body request: SignInRequest): SignInResponse

    @POST("auth/logout")
    suspend fun logout(): LogoutResponse


    //User data
    @GET("user/me")
    suspend fun getUserData(): UserResponse

    @PATCH("user/update")
    suspend fun updateUserOnboarding(@Body request: UpdateUserOnboardingRequest): UpdateUserResponse
    
    @PATCH("user/update")
    suspend fun updateUserData(@Body request: UpdateUserRequest): UpdateUserResponse


    //Transactions
    @GET("transaction")
    suspend fun getTransactions(): TransactionResponse

    @POST("transaction")
    suspend fun addTransaction(@Body request: AddTransactionRequest): UpdateUserResponse

    @DELETE("transaction/{id}")
    suspend fun deleteTransaction(@Path("id") id: Int): UpdateUserResponse

    @Multipart
    @POST("transaction/receipt")
    suspend fun uploadImage(@Part image: MultipartBody.Part): UploadImageResponse

}