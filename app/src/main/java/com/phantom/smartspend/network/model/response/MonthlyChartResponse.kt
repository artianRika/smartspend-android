package com.phantom.smartspend.network.model.response

data class MonthlyWrapper(
    val data: List<MonthlySpendingDto>
)
data class MonthlySpendingDto(
    val statistics: Map<String, Float>,
    val total_expenses: Float,
    val total_income: Float,
    val from: String,
    val to: String
)