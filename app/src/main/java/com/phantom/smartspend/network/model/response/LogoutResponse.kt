package com.phantom.smartspend.network.model.response


import com.google.gson.annotations.SerializedName

data class LogoutResponse(
    @SerializedName("message")
    val message: String
)