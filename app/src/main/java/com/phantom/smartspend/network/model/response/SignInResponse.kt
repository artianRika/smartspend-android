package com.phantom.smartspend.network.model.response


import com.google.gson.annotations.SerializedName
import com.phantom.smartspend.data.model.UserData

data class SignInResponse(
    @SerializedName("access_token")
    val accessToken: String,
    @SerializedName("message")
    val message: String,
    @SerializedName("refresh_token")
    val refreshToken: String,
    @SerializedName("refresh_token_expiry_date")
    val refreshTokenExpiryDate: String,
    @SerializedName("user_data")
    val userData: UserData
)