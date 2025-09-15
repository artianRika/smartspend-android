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
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.patrykandpatrick.vico.compose.cartesian.CartesianChartHost
import com.patrykandpatrick.vico.compose.cartesian.axis.rememberBottom
import com.patrykandpatrick.vico.compose.cartesian.axis.rememberStart
import com.patrykandpatrick.vico.compose.cartesian.layer.rememberColumnCartesianLayer
import com.patrykandpatrick.vico.compose.cartesian.rememberCartesianChart
import com.patrykandpatrick.vico.compose.common.component.rememberLineComponent
import com.patrykandpatrick.vico.compose.common.component.rememberTextComponent
import com.patrykandpatrick.vico.compose.common.fill
import com.patrykandpatrick.vico.compose.common.shape.toVicoShape
import com.patrykandpatrick.vico.core.cartesian.axis.HorizontalAxis
import com.patrykandpatrick.vico.core.cartesian.axis.VerticalAxis
import com.patrykandpatrick.vico.core.cartesian.data.CartesianChartModelProducer
import com.patrykandpatrick.vico.core.cartesian.data.CartesianValueFormatter
import com.patrykandpatrick.vico.core.cartesian.data.columnSeries
import com.patrykandpatrick.vico.core.cartesian.layer.ColumnCartesianLayer
import com.phantom.smartspend.data.model.MonthlySpendingDto
import com.phantom.smartspend.ui.theme.Primary
import com.phantom.smartspend.ui.theme.PrimaryDark
import com.phantom.smartspend.ui.theme.PrimaryLight
import com.phantom.smartspend.ui.theme.Secondary
import com.phantom.smartspend.ui.theme.SecondaryLight
import com.phantom.smartspend.ui.theme.Tertiary
import com.phantom.smartspend.utils.DateUtils
import com.phantom.smartspend.viewmodels.UserViewModel
import org.koin.androidx.compose.koinViewModel
import java.text.NumberFormat
import java.time.*
import java.time.format.DateTimeFormatter
import java.time.temporal.WeekFields
import kotlin.math.abs
import kotlin.math.roundToInt
import kotlin.random.Random

/* -------------------------------- Public Screen ----------------------------- */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StatsScreen(
    userVm: UserViewModel = koinViewModel()
) {
    val spending by userVm.monthlySpending.collectAsState()
    val pieChart by userVm.pieChart.collectAsState()

    LaunchedEffect(Unit) { userVm.loadMonthlySpending() }

    var period by remember { mutableStateOf(StatsPeriod.WEEK) }
    var anchorDate by remember { mutableStateOf(LocalDate.now()) }

    val dateRange = remember(period, anchorDate) {
        if (period == StatsPeriod.WEEK) currentWeekRange(anchorDate) else currentMonthRange(anchorDate)
    }

    LaunchedEffect(dateRange) {
        userVm.loadPieChart(
            from = dateRange.start.toRfc3339StartOfDay(),
            to = dateRange.end.toRfc3339EndOfDay()
        )
    }

    var showDatePicker by remember { mutableStateOf(false) }
    val pickerState = rememberDatePickerState(
        initialSelectedDateMillis = anchorDate.toEpochMillis()
    )

    val summary by remember(period, dateRange) {
        mutableStateOf(if (period == StatsPeriod.WEEK) demoWeeklySummary() else demoMonthlySummary())
    }

    val palette = listOf(Primary, PrimaryDark, PrimaryLight, Secondary, SecondaryLight, Tertiary)

    val slices: List<CategorySlice> = pieChart?.statistics?.entries
        ?.mapIndexed { i, entry ->
            val value = (entry.value * 100).toLong().coerceAtLeast(1L)
            CategorySlice(
                name = entry.key,
                valueMinor = value,
                color = palette[i % palette.size]
            )
        }
        ?: emptyList()

   //DEBUG
    LaunchedEffect(pieChart, spending) {
        android.util.Log.d(
            "StatsScreen",
            "📊 PieChart: ${pieChart?.statistics?.entries?.joinToString() ?: "null"} | " +
                    "MonthlySpending: ${spending.size} items"
        )
    }

    val scroll = rememberScrollState()
    Scaffold { _ ->
        Column(
            modifier = Modifier
                .padding(top = 36.dp)
                .fillMaxSize()
                .verticalScroll(scroll)
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            DateHeader(
                text = dateRange.pretty(),
                onClick = { showDatePicker = true },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 2.dp),
            )

            PeriodToggle(
                selected = period,
                onSelect = { period = it }
            )

            SummaryRow(
                incomeMinor = summary.totalIncomeMinor,
                expenseMinor = summary.totalExpenseMinor
            )

            AnimatedContent(
                targetState = period,
                transitionSpec = {
                    (fadeIn(animationSpec = tween(250, easing = FastOutSlowInEasing)))
                        .togetherWith(fadeOut(animationSpec = tween(250, easing = FastOutSlowInEasing)))
                }
            ) { p ->
                if (p == StatsPeriod.WEEK) {
                    if (slices.isNotEmpty()) {
                        PieChartCard(
                            slices = slices,
                            totalLabel = "Spending by category",
                            height = 280.dp
                        )
                    } else {
                        Text("No weekly data yet")
                    }
                } else {
                    if (spending.isNotEmpty()) {
                        MonthlySpendingBarChart(spendingData = spending)
                    } else {
                        Text("No monthly spending data yet")
                    }
                }
            }
        }
    }

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
            .background(Color.Transparent, RoundedCornerShape(28.dp))
            .padding(6.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        PeriodToggleItem("Weekly", StatsPeriod.WEEK, selected == StatsPeriod.WEEK, onSelect)
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
            colors = ButtonDefaults.textButtonColors(contentColor = MaterialTheme.colorScheme.onPrimaryContainer)
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
            Text(currency(incomeMinor), color = Color(0xFF00A36C), fontWeight = FontWeight.SemiBold)
        }
        Column(horizontalAlignment = Alignment.End) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text("● ", color = Color(0xFFDE3B3B))
                Text("Total Expense", style = MaterialTheme.typography.labelLarge)
            }
            Text(currency(expenseMinor), color = Color(0xFFDE3B3B), fontWeight = FontWeight.SemiBold)
        }
    }
}

/* ------------------------------ Pie Chart -------------------------------- */
data class CategorySlice(val name: String, val valueMinor: Long, val color: Color)

@Composable
fun PieChartCard(
    slices: List<CategorySlice>,
    totalLabel: String,
    height: Dp
) {
    val totalMinor = slices.sumOf { it.valueMinor }.coerceAtLeast(1)
    val sweep by animateFloatAsState(360f, tween(650, easing = FastOutSlowInEasing), label = "pie_sweep")
    val fade by animateFloatAsState(1f, tween(400, delayMillis = 120, easing = FastOutSlowInEasing), label = "pie_fade")

    ElevatedCard(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(8.dp)
    ) {
        Column(Modifier.padding(16.dp)) {
            Text(totalLabel, style = MaterialTheme.typography.titleMedium)

            Row(
                Modifier.fillMaxWidth().height(height),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier.weight(1f).fillMaxHeight(),
                    contentAlignment = Alignment.Center
                ) {
                    Canvas(Modifier.fillMaxSize()) {
                        val diameter = size.minDimension
                        val radius = diameter / 2f
                        val center = Offset(size.width / 2f, size.height / 2f)
                        val topLeft = Offset(center.x - radius, center.y - radius)
                        val arcSize = Size(diameter, diameter)
                        var startAngle = -90f

                        slices.forEach { s ->
                            val pct = s.valueMinor.toFloat() / totalMinor.toFloat()
                            val angle = pct * sweep
                            drawArc(
                                color = s.color.copy(alpha = fade),
                                startAngle = startAngle,
                                sweepAngle = angle,
                                useCenter = true,
                                topLeft = topLeft,
                                size = arcSize
                            )
                            startAngle += angle
                        }
                    }
                }

                Spacer(Modifier.width(16.dp))

                Column(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    slices.forEach { s ->
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(Modifier.size(12.dp).background(s.color, CircleShape))
                            Spacer(Modifier.width(8.dp))
                            Text("${s.name} • ${percent(s.valueMinor, totalMinor)}", style = MaterialTheme.typography.bodyMedium)
                        }
                    }
                }
            }
        }
    }
}

/* ------------------------------ Monthly Bar Chart ------------------------------ */
@Composable
fun MonthlySpendingBarChart(
    spendingData: List<MonthlySpendingDto>,
    modifier: Modifier = Modifier
) {
    val months = spendingData.map { DateUtils.rfc3339ToMonthAbbrev(it.date) }
    val values = spendingData.map { it.amount.takeIf { a -> a >= 0f } ?: 0f }

    val modelProducer = remember { CartesianChartModelProducer() }
    LaunchedEffect(values) {
        modelProducer.runTransaction {
            columnSeries { series(values.map { it.toDouble() }) }
        }
    }

    val yFormatter = CartesianValueFormatter { _, y, _ ->
        if (abs(y) >= 1000.0) "${(y / 1000.0).toInt()}k" else y.toInt().toString()
    }
    val xFormatter = CartesianValueFormatter { _, x, _ -> months.getOrNull(x.toInt()) ?: "" }

    val columnLayer = rememberColumnCartesianLayer(
        columnProvider = ColumnCartesianLayer.ColumnProvider.series(
            rememberLineComponent(
                fill = fill(MaterialTheme.colorScheme.primary),
                thickness = 14.dp,
                shape = RoundedCornerShape(6.dp).toVicoShape()
            )
        )
    )

    ElevatedCard(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(Modifier.padding(16.dp)) {
            Text("Monthly Spending", style = MaterialTheme.typography.titleMedium)
            Spacer(Modifier.height(8.dp))

            CartesianChartHost(
                chart = rememberCartesianChart(
                    columnLayer,
                    startAxis = VerticalAxis.rememberStart(valueFormatter = yFormatter),
                    bottomAxis = HorizontalAxis.rememberBottom(
                        valueFormatter = xFormatter,
                        itemPlacer = HorizontalAxis.ItemPlacer.aligned(),
                        labelRotationDegrees = -45f,
                        label = rememberTextComponent(
                            color = MaterialTheme.colorScheme.onBackground,
                            textSize = 10.sp
                        )
                    ),
                ),
                modelProducer = modelProducer,
                modifier = Modifier.fillMaxWidth().height(250.dp)
            )
        }
    }
}

/* ------------------------------ Date Utilities ------------------------------ */
data class DateRange(val start: LocalDate, val end: LocalDate)

private fun currentWeekRange(today: LocalDate): DateRange {
    val wf = WeekFields.ISO
    val start = today.with(wf.dayOfWeek(), 1)
    val end = start.plusDays(6)
    return DateRange(start, end)
}

fun currentMonthRange(today: LocalDate): DateRange {
    val ym = YearMonth.from(today)
    return DateRange(ym.atDay(1), ym.atEndOfMonth())
}

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

fun LocalDate.toEpochMillis(): Long =
    this.atStartOfDay(ZoneId.systemDefault()).toInstant().toEpochMilli()

fun millisToLocalDate(millis: Long): LocalDate =
    Instant.ofEpochMilli(millis).atZone(ZoneId.systemDefault()).toLocalDate()

fun LocalDate.toRfc3339StartOfDay(): String =
    this.atStartOfDay(ZoneId.systemDefault()).toInstant().toString()

fun LocalDate.toRfc3339EndOfDay(): String =
    this.atTime(LocalTime.MAX).atZone(ZoneId.systemDefault()).toInstant().toString()

/* ------------------------------ Demo Data -------------------------------- */
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

//fun demoCategorySlices(): List<CategorySlice> {
//    val palette = listOf(Primary, PrimaryDark, PrimaryLight, Secondary, SecondaryLight, Tertiary)
//    val names = listOf("Food", "Fuel", "Going out", "Household", "Other")
//    return names.mapIndexed { i, n ->
//        CategorySlice(n, Random.nextLong(50_00, 400_00), palette[i % palette.size])
//    }.sortedByDescending { it.valueMinor }
//}
