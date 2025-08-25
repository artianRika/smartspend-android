package com.phantom.smartspend.ui.screens.home

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import java.text.NumberFormat
import java.time.LocalDate
import java.time.YearMonth
import java.time.format.DateTimeFormatter
import java.time.temporal.WeekFields
import kotlin.math.roundToInt
import kotlin.random.Random
import androidx.compose.foundation.layout.RowScope
import com.phantom.smartspend.ui.theme.Primary
import com.phantom.smartspend.ui.theme.PrimaryDark
import com.phantom.smartspend.ui.theme.PrimaryLight
import com.phantom.smartspend.ui.theme.Secondary
import com.phantom.smartspend.ui.theme.SecondaryLight
import com.phantom.smartspend.ui.theme.Tertiary
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.rememberDatePickerState
import java.time.ZoneId
import java.time.Instant

/* -------------------------------- Public Screen ----------------------------- */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StatsScreen(
    onBack: () -> Unit = {}
) {
    var period by remember { mutableStateOf(StatsPeriod.WEEK) }


    var anchorDate by remember { mutableStateOf(LocalDate.now()) }

    // Date line
    val today = LocalDate.now()
    val dateRange = remember(period, anchorDate) {
        if (period == StatsPeriod.WEEK) currentWeekRange(anchorDate) else currentMonthRange(anchorDate)
    }


    //Date picker dialog state
    var showDatePicker by remember { mutableStateOf(false) }
    val pickerState = rememberDatePickerState(
        initialSelectedDateMillis = anchorDate.toEpochMillis()
    )
    // Demo data (replace with repository later)
    val summary by remember(period, dateRange) {
        mutableStateOf(if (period == StatsPeriod.WEEK) demoWeeklySummary() else demoMonthlySummary())
    }
    val categories: List<CategorySlice> by remember(period, dateRange) {
        mutableStateOf(demoCategorySlices())
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                },
                title = { Text("Stats", style = MaterialTheme.typography.titleMedium) }
            )
        }
    ) { inner ->
        Column(
            modifier = Modifier
                .padding(inner)
                .fillMaxSize()
                .systemBarsPadding()
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            DateHeader(
                text = dateRange.pretty(),
                onClick = { showDatePicker = true },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 6.dp),
            )

            Spacer(Modifier.height(8.dp))
            PeriodToggle(
                selected = period,
                onSelect = { period = it }
            )

            SummaryRow(
                incomeMinor = summary.totalIncomeMinor,
                expenseMinor = summary.totalExpenseMinor
            )

            Text(
                text = summary.aiNote,
                style = MaterialTheme.typography.titleMedium.copy(fontSize = 18.sp),
                color = MaterialTheme.colorScheme.onBackground
            )

            // Pie card (animated content swap)
            AnimatedContent(
                targetState = categories,
                transitionSpec = {
                    (fadeIn(animationSpec = tween(250, easing = FastOutSlowInEasing)))
                        .togetherWith(fadeOut(animationSpec = tween(250, easing = FastOutSlowInEasing)))
                }
            ) { slices ->
                PieChartCard(
                    slices = slices,
                    totalLabel = "Spending by category",
                    height = 280.dp
                )
            }
        }

    }

    // ---- Date Picker Dialog ----
    if (showDatePicker) {
        DatePickerDialog(
            onDismissRequest = { showDatePicker = false },
            confirmButton = {
                TextButton(onClick = {
                    pickerState.selectedDateMillis?.let { millis ->
                        anchorDate = millisToLocalDate(millis)
                    }
                    showDatePicker = false
                }) { Text("OK") }
            },
            dismissButton = {
                TextButton(onClick = { showDatePicker = false }) { Text("Cancel") }
            }
        ) {
            DatePicker(state = pickerState)
        }
    }
}



/* ------------------------------- UI Building -------------------------------- */

enum class StatsPeriod { WEEK, MONTH }

@Composable
private fun PeriodToggle(selected: StatsPeriod, onSelect: (StatsPeriod) -> Unit) {
    Row(
        Modifier
            .fillMaxWidth()
            .background(Color(0xFFE8F5EE), RoundedCornerShape(28.dp))
            .padding(6.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        PeriodToggleItem("Weekly",  StatsPeriod.WEEK,  selected == StatsPeriod.WEEK,  onSelect)
        PeriodToggleItem("Monthly", StatsPeriod.MONTH, selected == StatsPeriod.MONTH, onSelect)
    }
}

@Composable
private fun RowScope.PeriodToggleItem(
    label: String,
    value: StatsPeriod,
    selectedState: Boolean,
    onSelect: (StatsPeriod) -> Unit
) {
    if (selectedState) {
        Button(
            onClick = { onSelect(value) },
            modifier = Modifier.weight(1f),
            shape = RoundedCornerShape(22.dp),
            contentPadding = PaddingValues(vertical = 10.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.secondary,
                contentColor = Color.Black
            )
        ) { Text(label, fontWeight = FontWeight.SemiBold) }
    } else {
        TextButton(
            onClick = { onSelect(value) },
            modifier = Modifier.weight(1f),
            shape = RoundedCornerShape(22.dp),
            colors = ButtonDefaults.textButtonColors(contentColor = Color.Black)
        ) { Text(label) }
    }
}


@Composable
private fun SummaryRow(incomeMinor: Long, expenseMinor: Long) {
    Row(
        Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Column(horizontalAlignment = Alignment.Start) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text("● ", color = Color(0xFF00A36C))
                Text("Total Income", style = MaterialTheme.typography.labelLarge)
            }
            Text(
                currency(incomeMinor),
                color = Color(0xFF00A36C),
                fontWeight = FontWeight.SemiBold
            )
        }
        Column(horizontalAlignment = Alignment.End) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text("● ", color = Color(0xFFDE3B3B))
                Text("Total Expense", style = MaterialTheme.typography.labelLarge)
            }
            Text(
                currency(expenseMinor),
                color = Color(0xFFDE3B3B),
                fontWeight = FontWeight.SemiBold
            )
        }
    }
}

/* -------------------------------- Pie Chart -------------------------------- */

data class CategorySlice(val name: String, val valueMinor: Long, val color: Color)

@Composable
private fun PieChartCard(
    slices: List<CategorySlice>,
    totalLabel: String,
    height: Dp
) {
    val totalMinor = slices.sumOf { it.valueMinor }.coerceAtLeast(1) // avoid /0
    val animatedSweep by remember(totalMinor, slices) { mutableStateOf(true) }

    // Grow from 0 -> 360 on first draw
    val sweep by animateFloatAsState(
        targetValue = if (animatedSweep) 360f else 0f,
        animationSpec = tween(600, easing = FastOutSlowInEasing)
    )

    ElevatedCard(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(28.dp)
    ) {
        Column(Modifier.padding(16.dp)) {
            Text(totalLabel, style = MaterialTheme.typography.titleMedium)
            Spacer(Modifier.height(8.dp))

            Row(
                Modifier
                    .fillMaxWidth()
                    .height(height),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Chart
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight(),
                    contentAlignment = Alignment.Center
                ) {
                    Canvas(Modifier.fillMaxSize()) {
                        val diameter = size.minDimension
                        val topLeftX = (size.width - diameter) / 2f
                        val topLeftY = (size.height - diameter) / 2f
                        val arcSize = Size(diameter, diameter)

                        var startAngle = -90f
                        slices.forEach { s ->
                            val pct = s.valueMinor.toFloat() / totalMinor.toFloat()
                            val angle = pct * sweep
                            drawArc(
                                color = s.color,
                                startAngle = startAngle,
                                sweepAngle = angle,
                                useCenter = true,
                                topLeft = Offset(topLeftX, topLeftY),
                                size = arcSize
                            )
                            startAngle += angle
                        }
                    }
                }

                Spacer(Modifier.width(16.dp))

                // Legend
                Column(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    slices.forEach { s ->
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                Modifier
                                    .size(12.dp)
                                    .background(s.color, CircleShape)
                            )
                            Spacer(Modifier.width(8.dp))
                            Text(
                                "${s.name} • ${percent(s.valueMinor, totalMinor)}",
                                style = MaterialTheme.typography.bodyMedium
                            )
                        }
                    }
                }
            }
        }
    }
}

/* ------------------------------ Date Utilities ------------------------------ */



@Composable
private fun DateHeader(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.clickable(onClick = onClick),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center // <--- centers content
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.labelLarge,
            color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.85f)
        )
        Spacer(Modifier.width(6.dp))
        Icon(
            imageVector = Icons.Default.ExpandMore,
            contentDescription = "Pick date",
            modifier = Modifier.size(18.dp),
            tint = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f)
        )
    }
}

private data class DateRange(val start: LocalDate, val end: LocalDate)

private fun currentWeekRange(today: LocalDate): DateRange {
    val wf = WeekFields.ISO
    val start = today.with(wf.dayOfWeek(), 1) // Monday
    val end = start.plusDays(6)
    return DateRange(start, end)
}

private fun currentMonthRange(today: LocalDate): DateRange {
    val ym: YearMonth = YearMonth.from(today)
    return DateRange(ym.atDay(1), ym.atEndOfMonth())
}

/** e.g., "8–15 Aug 2025" or "01–31 Aug 2025" */
private fun DateRange.pretty(): String {
    val sameMonth = start.month == end.month && start.year == end.year
    return if (sameMonth) {
        val dFmt = DateTimeFormatter.ofPattern("d")
        val mFmt = DateTimeFormatter.ofPattern("MMM yyyy")
        "${start.format(dFmt)}–${end.format(dFmt)} ${end.format(mFmt)}"
    } else {
        val fmt = DateTimeFormatter.ofPattern("d MMM yyyy")
        "${start.format(fmt)} – ${end.format(fmt)}"
    }
}
//HELPERS TO CONVERT BETWEEN LOCAL DATE AND MILLIS
private fun LocalDate.toEpochMillis(): Long =
    this.atStartOfDay(ZoneId.systemDefault()).toInstant().toEpochMilli()

private fun millisToLocalDate(millis: Long): LocalDate =
    Instant.ofEpochMilli(millis).atZone(ZoneId.systemDefault()).toLocalDate()


/* ------------------------------ Demo Data Only ------------------------------ */

data class StatsSummary(val totalIncomeMinor: Long, val totalExpenseMinor: Long, val aiNote: String)

private fun currency(minor: Long): String =
    NumberFormat.getCurrencyInstance().format(minor / 100.0)

private fun percent(partMinor: Long, totalMinor: Long): String {
    val pct = (partMinor.toDouble() / totalMinor.toDouble()) * 100.0
    val rounded = (pct * 10).roundToInt() / 10.0
    return "${rounded}%"
}

private fun demoWeeklySummary(): StatsSummary {
    val income = Random.nextLong(120_00, 420_00)
    val expense = Random.nextLong(80_00, 300_00)
    val diffPct = Random.nextInt(10, 40)
    return StatsSummary(income, -expense, "$diffPct% higher than yesterday")
}

private fun demoMonthlySummary(): StatsSummary {
    val income = Random.nextLong(1200_00, 5200_00)
    val expense = Random.nextLong(900_00, 4800_00)
    val diffPct = Random.nextInt(5, 30)
    return StatsSummary(income, -expense, "AI: $diffPct% higher than last month’s expenses")
}

private fun demoCategorySlices(): List<CategorySlice> {
    val palette = listOf(
        Primary,
        PrimaryDark,
        PrimaryLight,
        Secondary,
        SecondaryLight,
        Tertiary
    )
    val names = listOf("Food", "Fuel", "Going out", "Household", "Other")
    return names.mapIndexed { i, n ->
        CategorySlice(
            name = n,
            valueMinor = Random.nextLong(50_00, 400_00),
            color = palette[i % palette.size]
        )
    }.sortedByDescending { it.valueMinor }
}
