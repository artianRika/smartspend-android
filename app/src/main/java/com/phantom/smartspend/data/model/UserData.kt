package com.phantom.smartspend.data.model

import com.google.gson.annotations.SerializedName

data class UserData(

    @SerializedName("first_name")
    val firstName: String,
    @SerializedName("last_name")
    val lastName: String,
    @SerializedName("username")
    val username: String,
    @SerializedName("google_email")
    val googleEmail: String,
    @SerializedName("apple_email")
    val appleEmail: String,
    @SerializedName("avatar_url")
    val avatarUrl: String,
    @SerializedName("balance")
    val balance: Int,
    @SerializedName("monthly_saving_goal")
    val monthlySavingGoal: Int,
    @SerializedName("preferred_currency")
    val preferredCurrency: Int,
)