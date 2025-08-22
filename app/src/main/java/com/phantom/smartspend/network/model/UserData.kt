package com.phantom.smartspend.network.model

import com.google.gson.annotations.SerializedName

data class UserData(
    @SerializedName("apple_email")
    val appleEmail: String,
    @SerializedName("avatar_url")
    val avatarUrl: String,
    @SerializedName("balance")
    val balance: Int,
    @SerializedName("created_at")
    val createdAt: String,
    @SerializedName("first_name")
    val firstName: String,
    @SerializedName("google_email")
    val googleEmail: String,
    @SerializedName("ID")
    val iD: String,
    @SerializedName("last_name")
    val lastName: String,
    @SerializedName("monthly_saving_goal")
    val monthlySavingGoal: Int,
    @SerializedName("RefreshToken")
    val refreshToken: String,
    @SerializedName("RefreshTokenExpiryDate")
    val refreshTokenExpiryDate: String,
    @SerializedName("username")
    val username: String
)