package com.phantom.smartspend.network.response_models


import com.google.gson.annotations.SerializedName

data class SignInResponse(
    @SerializedName("access_token")
    val accessToken: String,
    @SerializedName("message")
    val message: String,
    @SerializedName("refresh_token")
    val refreshToken: String,
    @SerializedName("user_data")
    val userData: UserData
)