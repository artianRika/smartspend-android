package com.phantom.smartspend.network.model.response

data class PieChartWrapper(
    val data: List<PieChartResponse>
)

data class PieChartResponse(
    val statistics: Map<String, Float>,
    val total_expenses: Float,
    val total_income: Float,
    val from: String,
    val to: String
)
