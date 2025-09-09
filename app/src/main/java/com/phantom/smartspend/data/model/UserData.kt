package com.phantom.smartspend.data.model

import com.google.gson.annotations.SerializedName

data class UserData(
    @SerializedName("apple_email")
    val appleEmail: String,
    @SerializedName("avatar_url")
    val avatarUrl: String,
    @SerializedName("balance")
    val balance: Float,
    @SerializedName("created_at")
    val createdAt: String,
    @SerializedName("first_name")
    val firstName: String,
    @SerializedName("google_email")
    val googleEmail: String,
    @SerializedName("last_name")
    val lastName: String,
    @SerializedName("monthly_saving_goal")
    val monthlySavingGoal: Float,
    @SerializedName("preferred_currency")
    val preferredCurrency: String,
    @SerializedName("username")
    val username: String
)