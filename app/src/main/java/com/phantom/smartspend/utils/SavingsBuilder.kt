package com.phantom.smartspend.utils

import com.phantom.smartspend.data.model.Transaction
import com.phantom.smartspend.data.model.UserData
import java.time.LocalDate
import java.time.YearMonth
import java.time.format.DateTimeFormatter

fun buildSavingsSeries(
    user: UserData,
    transactions: List<Transaction>
): List<Float> {
    val monthlyNet = transactions
        .groupBy { YearMonth.parse(it.date.substring(0, 7)) }
        .mapValues { (_, txns) ->
            val income = txns.filter { it.type == "Income" }
                .sumOf { it.amount.toDouble() }
                .toFloat()
            val expense = txns.filter { it.type == "Expense" }
                .sumOf { it.amount.toDouble() }
                .toFloat()
            income - expense
        }

    val savingsSeries = mutableListOf<Float>()
    var runningTotal = 0f
    for (monthIndex in 0 until 12) {
        val ym = YearMonth.now().withMonth(monthIndex + 1)
        val savedThisMonth = monthlyNet[ym] ?: 0f
        runningTotal += savedThisMonth
        savingsSeries.add(runningTotal)
    }

    return savingsSeries
}
