package com.phantom.smartspend.network.model.request

import com.google.gson.annotations.SerializedName

data class UpdateUserOnboardingRequest(
    @SerializedName("balance")
    val balance: Float,
    @SerializedName("monthly_saving_goal")
    val monthlySavingGoal: Float,
    @SerializedName("preferred_currency")
    val preferredCurrency: String,
)