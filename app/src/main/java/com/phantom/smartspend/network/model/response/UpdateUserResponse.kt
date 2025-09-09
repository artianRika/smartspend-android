package com.phantom.smartspend.network.model.response


import com.google.gson.annotations.SerializedName

data class UpdateUserResponse(
    @SerializedName("message")
    val message: String
)