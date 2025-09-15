package com.phantom.smartspend.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
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
import com.phantom.smartspend.utils.Transaction
import com.phantom.smartspend.utils.buildMonthlyContext
import com.phantom.smartspend.utils.buildSavingsTips
import com.phantom.smartspend.utils.currency
import com.phantom.smartspend.utils.forecastEndOfMonth
import com.phantom.smartspend.utils.minorToMajor

@Composable
fun SavingsInsightsSection(
    transactions: List<Transaction>,
    goalMinor: Long?,                 // pass your monthly goal in minor units (nullable)
    currencyCode: String
) {
    val ctx = remember(transactions) { buildMonthlyContext(transactions) }
    val tips = remember(transactions) { buildSavingsTips(ctx, transactions) }
    val forecast = remember(transactions, goalMinor) { forecastEndOfMonth(ctx, goalMinor) }

    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        // Forecast card
        ElevatedCard(shape = RoundedCornerShape(20.dp)) {
            Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.TrendingUp, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
                    Spacer(Modifier.width(8.dp))
                    Text("Prediction", style = MaterialTheme.typography.titleMedium)
                }

                val projected = currency(minorToMajor(forecast.projectedSavingsMinor), currencyCode)
                val label = "${ctx.month.month.name.lowercase().replaceFirstChar { it.titlecase() }} projection: $projected"

                Text(label, style = MaterialTheme.typography.bodyLarge)

                if (goalMinor != null) {
                    val goalMajor = minorToMajor(goalMinor)
                    val progress = (forecast.projectedSavingsMinor / goalMinor.toFloat()).coerceIn(0f, 1f)
                    LinearProgressIndicator(progress = progress, Modifier.fillMaxWidth())
                    Text(
                        if (forecast.onTrack) "On track to hit your goal" else "Off track — consider a small cut in top categories",
                        color = if (forecast.onTrack) Color(0xFF66BB6A) else Color(0xFFFF7043),
                        style = MaterialTheme.typography.bodyMedium
                    )
                    Text("Goal: ${currency(goalMajor, currencyCode)}", style = MaterialTheme.typography.bodySmall)
                }
            }
        }

        // Tips card
        ElevatedCard(shape = RoundedCornerShape(20.dp)) {
            Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.Lightbulb, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
                    Spacer(Modifier.width(8.dp))
                    Text("Smart tips", style = MaterialTheme.typography.titleMedium)
                }
                tips.forEach { tip ->
                    Text("• $tip", style = MaterialTheme.typography.bodyMedium)
                }
            }
        }
    }
}
