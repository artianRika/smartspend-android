package com.phantom.smartspend.network.request_models

import com.google.gson.annotations.SerializedName

data class SignInRequest(
    @SerializedName("id_token")
    val idToken: String,
)