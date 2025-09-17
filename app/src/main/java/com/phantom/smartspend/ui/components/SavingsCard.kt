package com.phantom.smartspend.ui.components

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.phantom.smartspend.data.model.UserData
import com.phantom.smartspend.data.model.Transaction
import java.time.YearMonth
import java.time.format.TextStyle
import java.util.Locale

@Composable
fun SavingsCard(
    showViewMore: Boolean,
    userData: UserData?,
    onShowViewMoreClick: (() -> Unit)?,
    transactions: List<Transaction> = emptyList(),
    selectedMonth: YearMonth = YearMonth.now()
) {
    val currency = userData?.preferredCurrency ?: "EUR"
    val monthlyGoal = userData?.monthlySavingGoal ?: 0f

    val savedThisMonth = transactions
        .filter { tx ->
            runCatching {
                val ym = YearMonth.parse(tx.date.substring(0, 7))
                ym == selectedMonth
            }.getOrDefault(false)
        }
        .sumOf { tx ->
            when (tx.type) {
                "Income" -> tx.amount.toDouble()
                "Expense" -> -tx.amount.toDouble()
                else -> 0.0
            }
        }
        .toFloat()

    val monthName = selectedMonth.month.getDisplayName(TextStyle.FULL, Locale.getDefault())

    val rawProgress = if (monthlyGoal > 0f) {
        (savedThisMonth / monthlyGoal).coerceIn(0f, 1f)
    } else 0f
    val animatedProgress by animateFloatAsState(
        targetValue = rawProgress,
        animationSpec = tween(durationMillis = 1000, easing = FastOutSlowInEasing),
        label = "progressAnim"
    )

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(8.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.tertiary)
    ) {
        Column(
            modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp)
        ) {
            Row(
                Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    "Saved in $monthName",
                    color = MaterialTheme.colorScheme.onPrimaryContainer,
                    fontSize = 12.sp
                )
                TextButton(
                    onClick = { onShowViewMoreClick?.invoke() },
                    contentPadding = PaddingValues(0.dp)
                ) {
                    Text(
                        if (showViewMore) "View More" else "Edit Goal",
                        color = MaterialTheme.colorScheme.primary,
                        fontSize = 12.sp
                    )
                }
            }

            Column {
                Text(
                    "$currency ${String.format("%.2f", savedThisMonth)}",
                    color = MaterialTheme.colorScheme.onPrimaryContainer,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.SemiBold
                )
                HorizontalProgressBar(animatedProgress)
            }

            Row(
                Modifier.fillMaxWidth().padding(bottom = 8.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text("0", color = MaterialTheme.colorScheme.onPrimaryContainer, fontSize = 12.sp)
                Text(
                    "$currency ${String.format("%.2f", monthlyGoal)}",
                    color = MaterialTheme.colorScheme.onPrimaryContainer,
                    fontSize = 12.sp
                )
            }
        }
    }
}
