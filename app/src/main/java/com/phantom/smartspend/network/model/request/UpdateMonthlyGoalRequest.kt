package com.phantom.smartspend.network.model.request

import com.google.gson.annotations.SerializedName

data class UpdateMonthlyGoalRequest(
    @SerializedName("monthly_saving_goal")
    val monthlySavingGoal: Float,
)