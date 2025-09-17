package com.phantom.smartspend.network.model.response

import java.time.Month

class MonthStat(
    val month: String,
    val value: Double
)

fun MonthlySpendingDto.toMonthStats(): List<MonthStat> {
    return statistics.map {(monthKey, value) ->
        val monthLabel = try {
            Month.of(monthKey.toInt()).name.take(3)
        } catch(e: Exception) {
            monthKey
        }
        MonthStat(monthLabel, value.toDouble())
    }
}