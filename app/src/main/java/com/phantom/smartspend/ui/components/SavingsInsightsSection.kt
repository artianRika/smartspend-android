package com.phantom.smartspend.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lightbulb
import androidx.compose.material.icons.filled.TrendingUp
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.phantom.smartspend.data.model.Transaction   // 👈 use your real Transaction model
import java.time.YearMonth
import java.time.OffsetDateTime
import java.time.format.TextStyle
import java.util.*

@Composable
fun SavingsInsightsSection(
    transactions: List<Transaction>,
    monthlyGoal: Float,
    currencyCode: String,
    selectedMonth: YearMonth
) {
    val monthTx = remember(transactions, selectedMonth) {
        transactions.filter { tx ->
            runCatching { OffsetDateTime.parse(tx.date).toLocalDate() }
                .getOrNull()
                ?.let { it.month == selectedMonth.month && it.year == selectedMonth.year }
                ?: false
        }
    }

    val netSavings = remember(monthTx) {
        monthTx.fold(0f) { acc, tx ->
            when (tx.type) {
                "Income" -> acc + tx.amount
                "Expense" -> acc - tx.amount
                else -> acc
            }
        }
    }

    val onTrack = netSavings >= monthlyGoal

    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        ElevatedCard(shape = RoundedCornerShape(20.dp)) {
            Column(
                Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.TrendingUp, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
                    Spacer(Modifier.width(8.dp))
                    Text("Prediction", style = MaterialTheme.typography.titleMedium)
                }

                val label = "${selectedMonth.month.getDisplayName(TextStyle.SHORT, Locale.getDefault())} " +
                        "projection: ${netSavings.toInt()} $currencyCode"

                Text(label, style = MaterialTheme.typography.bodyLarge)

                val progress = (netSavings / monthlyGoal).coerceIn(0f, 1f)
                LinearProgressIndicator(progress = progress, Modifier.fillMaxWidth())

                Text(
                    if (onTrack) "On track to hit your goal"
                    else "Off track — consider a small cut in top categories",
                    color = if (onTrack) Color(0xFF66BB6A) else Color(0xFFFF7043),
                    style = MaterialTheme.typography.bodyMedium
                )
                Text("Goal: ${monthlyGoal.toInt()} $currencyCode", style = MaterialTheme.typography.bodySmall)
            }
        }

        ElevatedCard(shape = RoundedCornerShape(20.dp)) {
            Column(
                Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.Lightbulb, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
                    Spacer(Modifier.width(8.dp))
                    Text("Smart tips", style = MaterialTheme.typography.titleMedium)
                }

                if (onTrack) {
                    Text("• Great job! Keep saving consistently.", style = MaterialTheme.typography.bodyMedium)
                } else {
                    Text("• Review dining or shopping expenses.", style = MaterialTheme.typography.bodyMedium)
                    Text("• Try setting aside a fixed amount first each month.", style = MaterialTheme.typography.bodyMedium)
                }
            }
        }
    }
}