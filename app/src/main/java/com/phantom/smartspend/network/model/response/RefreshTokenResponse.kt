package com.phantom.smartspend.network.model.response


import com.google.gson.annotations.SerializedName

data class RefreshTokenResponse(
    @SerializedName("access_token")
    val renewedAccessToken: String
)