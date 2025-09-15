package com.phantom.smartspend.utils
import java.time.*
import kotlin.math.abs
import androidx.compose.ui.graphics.Color

data class Transaction(
    val amountMinor: Long,          // cents (income > 0, expense < 0) – adjust if your sign is different
    val category: String,
    val timestamp: Long             // epoch millis
)

data class MonthlyContext(
    val month: YearMonth,
    val incomeMinor: Long,
    val expenseMinor: Long,          // positive number (absolute sum of expenses)
    val savingsMinor: Long,          // income - expense
    val dayOfMonth: Int,
    val daysInMonth: Int
)

fun buildMonthlyContext(
    transactions: List<Transaction>,
    clock: Clock = Clock.systemDefaultZone(),
    zone: ZoneId = ZoneId.systemDefault()
): MonthlyContext {
    val now = LocalDate.now(clock)
    val ym = YearMonth.from(now)
    val start = ym.atDay(1).atStartOfDay(zone).toInstant().toEpochMilli()
    val end   = ym.plusMonths(1).atDay(1).atStartOfDay(zone).toInstant().toEpochMilli()

    val monthTx = transactions.filter { it.timestamp in start until end }

    // income positive, expense negative (if you use the opposite, swap signs below)
    val income = monthTx.filter { it.amountMinor > 0 }.sumOf { it.amountMinor }
    val expenseAbs = monthTx.filter { it.amountMinor < 0 }.sumOf { abs(it.amountMinor) }
    val savings = income - expenseAbs

    return MonthlyContext(
        month = ym,
        incomeMinor = income,
        expenseMinor = expenseAbs,
        savingsMinor = savings,
        dayOfMonth = now.dayOfMonth,
        daysInMonth = ym.lengthOfMonth()
    )
}

fun currency(major: Double, code: String) = "%,.2f $code".format(major)
fun minorToMajor(minor: Long, scale: Int = 2) = minor / 10.0.pow(scale)
private fun Double.pow(p: Int) = generateSequence(this) { it }.take(p).fold(1.0) { a, b -> a * b }


//SAVING TIPS
fun buildSavingsTips(
    ctx: MonthlyContext,
    transactions: List<Transaction>
): List<String> {
    val tips = mutableListOf<String>()

    // 1) Over/under spend vs income
    if (ctx.expenseMinor > ctx.incomeMinor) {
        tips += "You spent more than you earned this month. Try pausing non-essential categories for a week."
    } else if (ctx.savingsMinor > 0 && ctx.savingsMinor < (ctx.incomeMinor * 0.1).toLong()) {
        tips += "Savings are under 10% of income. Set a micro-goal for the next 7 days."
    }

    // 2) Biggest expense category this month
    val biggest = transactions
        .filter { it.amountMinor < 0 }
        .groupBy { it.category }
        .mapValues { (_, tx) -> tx.sumOf { abs(it.amountMinor) } }
        .maxByOrNull { it.value }
    biggest?.let {
        tips += "Highest spending category: ${it.key}. Try a simple cap or switch one recurring purchase."
    }

    // 3) Pace vs calendar
    val pace = (ctx.expenseMinor.toDouble() / ctx.dayOfMonth) * ctx.daysInMonth
    if (pace > ctx.expenseMinor * 1.15) {
        tips += "Spending pace is rising mid-month. Review subscriptions and one-off buys."
    }

    // Fallback
    if (tips.isEmpty()) tips += "Nice work keeping expenses in control. Consider increasing your monthly goal slightly."
    return tips
}

data class Forecast(
    val projectedSavingsMinor: Long,
    val onTrack: Boolean
)

fun forecastEndOfMonth(ctx: MonthlyContext, goalMinor: Long?): Forecast {
    // Simple pace forecast: current average per day * total days
    val avgPerDay = ctx.savingsMinor.toDouble() / ctx.dayOfMonth
    val projected = (avgPerDay * ctx.daysInMonth).toLong()
    val onTrack = goalMinor?.let { projected >= it } ?: true
    return Forecast(projectedSavingsMinor = projected, onTrack = onTrack)
}