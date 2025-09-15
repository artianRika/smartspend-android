package com.phantom.smartspend.network

import com.phantom.smartspend.data.model.MonthlySpendingDto
import com.phantom.smartspend.network.model.request.AddTransactionRequest
import com.phantom.smartspend.network.model.request.EditTransactionRequest
import com.phantom.smartspend.network.model.request.SignInRequest
import com.phantom.smartspend.network.model.request.UpdateCurrencyRequest
import com.phantom.smartspend.network.model.request.UpdateUserOnboardingRequest
import com.phantom.smartspend.network.model.request.UpdateUserRequest
import com.phantom.smartspend.network.model.response.LogoutResponse
import com.phantom.smartspend.network.model.response.PieChartWrapper
import com.phantom.smartspend.network.model.response.SignInResponse
import com.phantom.smartspend.network.model.response.TransactionResponse
import com.phantom.smartspend.network.model.response.UpdateUserResponse
import com.phantom.smartspend.network.model.response.UploadImageResponse
import com.phantom.smartspend.network.model.response.UserResponse
import okhttp3.MultipartBody
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Path
import retrofit2.http.Query

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

    @PATCH("user/update")
    suspend fun updatePreferredCurrency(@Body request: UpdateCurrencyRequest): UpdateUserResponse


    //Transactions
    @GET("transaction")
    suspend fun getTransactions(): TransactionResponse

    @POST("transaction")
    suspend fun addTransaction(@Body request: AddTransactionRequest): UpdateUserResponse

    @PATCH("transaction/{id}")
    suspend fun editTransaction(@Path("id") id: Int, @Body request: EditTransactionRequest): UpdateUserResponse

    @DELETE("transaction/{id}")
    suspend fun deleteTransaction(@Path("id") id: Int): UpdateUserResponse

    @Multipart
    @POST("transaction/receipt")
    suspend fun uploadImage(@Part image: MultipartBody.Part): UploadImageResponse

    //Monthly data get
    @GET("spending/monthly")
    suspend fun getMonthlySpending(
        @Query("from") fromRfc3339: String? = null,
        @Query("to")   toRfc3339: String? = null
    ): List<MonthlySpendingDto>

    //PIE CHART GET
    @GET("statistics/pie")
    suspend fun getPieChart(
        @Query("from") from: String,
        @Query("to") to: String
    ): PieChartWrapper
}