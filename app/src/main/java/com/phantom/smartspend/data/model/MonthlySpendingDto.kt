package com.phantom.smartspend.data.model

data class MonthlySpendingDto(
    val date: String,   // RFC3339
    val amount: Float
)