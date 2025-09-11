package com.phantom.smartspend.network.model.request

import com.google.gson.annotations.SerializedName

data class SignInRequest(
    @SerializedName("id_token")
    val idToken: String,
)