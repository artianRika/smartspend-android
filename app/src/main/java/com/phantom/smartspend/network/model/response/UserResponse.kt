package com.phantom.smartspend.network.model.response


import com.google.gson.annotations.SerializedName
import com.phantom.smartspend.data.model.UserData

data class UserResponse(
    @SerializedName("data")
    val data: UserData
)