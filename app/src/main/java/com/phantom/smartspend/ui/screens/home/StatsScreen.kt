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
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ExpandMore
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
import java.text.NumberFormat
import java.time.LocalDate
import java.time.YearMonth
import java.time.format.DateTimeFormatter
import java.time.temporal.WeekFields
import kotlin.math.roundToInt
import kotlin.random.Random
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
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


import com.patrykandpatrick.vico.compose.cartesian.axis.rememberBottom
import com.patrykandpatrick.vico.compose.cartesian.axis.rememberStart
import com.patrykandpatrick.vico.compose.common.shape.toVicoShape
import com.patrykandpatrick.vico.core.cartesian.data.CartesianChartModelProducer
import com.patrykandpatrick.vico.core.cartesian.data.columnSeries


/* -------------------------------- Public Screen ----------------------------- */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StatsScreen(
    //onBack: () -> Unit = {}
) {
    var period by remember { mutableStateOf(StatsPeriod.WEEK) }


    var anchorDate by remember { mutableStateOf(LocalDate.now()) }

    // Date line
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
    val scroll = rememberScrollState()
    Scaffold{ _ ->
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

            Text(
                text = if (period == StatsPeriod.WEEK)
                    summary.aiNote.replace("last month", "yesterday")
                 else
                     "AI: 15% higher than last months expenses",
                style = MaterialTheme.typography.titleMedium.copy(fontSize = 18.sp),
                color = MaterialTheme.colorScheme.onBackground
            )

            // Pie card (animated content swap)
            AnimatedContent(
                targetState = period,
                transitionSpec = {
                    (fadeIn(animationSpec = tween(250, easing = FastOutSlowInEasing)))
                        .togetherWith(fadeOut(animationSpec = tween(250, easing = FastOutSlowInEasing)))
                }
            ) { p ->
              if (p == StatsPeriod.WEEK){
                  PieChartCard(
                      slices = categories,
                      totalLabel = "Spending by category",
                      height = 280.dp
                  )
              } else {
                  MonthlyCategoriesList(
                      data = demoMonthlyCategorySeries(),
                      cardBg = SecondaryLight
                  )
              }
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
            .background(Color.Transparent, RoundedCornerShape(28.dp))
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

@Composable
private fun CategoryBarCardVico(
    title: String,
    seriesA: List<Float>,      // we will plot ONLY this series
    seriesB: List<Float>,      // kept in signature but ignored
    cardBg: Color,
    xLabels: List<String> = listOf("Jan","Feb","Mar","Apr","May","Jun","Jul","Aug","Sep","Oct","Nov","Dec")
) {
    // --- sanitize & align (match data to labels) ---
    fun clean(xs: List<Float>) = xs.map { x -> if (x.isFinite() && x >= 0f) x else 0f }
    val a = clean(seriesA)
    val n = minOf(a.size, xLabels.size)
    val safeA = if (n == 0) listOf(0f) else a.take(n)
    val labels = remember(xLabels, n) { xLabels.take(n) }

    // --- model producer: single column series ---
    val modelProducer = remember { CartesianChartModelProducer() }
    LaunchedEffect(safeA) {
        modelProducer.runTransaction {
            columnSeries {
                series(y = safeA.map { it.toDouble() })  // now resolves
            }
        }
    }

    // --- one column style ---
    val columnLayer = com.patrykandpatrick.vico.compose.cartesian.layer.rememberColumnCartesianLayer(
        columnProvider = com.patrykandpatrick.vico.core.cartesian.layer.ColumnCartesianLayer.ColumnProvider.series(
            com.patrykandpatrick.vico.compose.common.component.rememberLineComponent(
                fill = com.patrykandpatrick.vico.compose.common.fill(Secondary),
                thickness = 18.dp,
                shape = RoundedCornerShape(10.dp).toVicoShape(),
            ),
        ),
    )

    // --- axes formatters ---
    val yFormatter = com.patrykandpatrick.vico.core.cartesian.data.CartesianValueFormatter { _, y, _ ->
        if (kotlin.math.abs(y) >= 1000.0) "${kotlin.math.round(y / 1000.0).toInt()}k"
        else kotlin.math.round(y).toInt().toString()
    }
    val xFormatter = com.patrykandpatrick.vico.core.cartesian.data.CartesianValueFormatter { _, x, _ ->
        labels.getOrNull(x.toInt()) ?: ""
    }

    // --- make it horizontally scrollable (chart wider than screen) ---
    val scroll = rememberScrollState()
    val barWidth = 18.dp
    val groupSpacing = 16.dp
    val widthPerGroup = barWidth + groupSpacing         // 1 bar per month
    val chartWidth = (widthPerGroup * n.toFloat()) + 32.dp
    val minChartWidth = 360.dp
    val finalWidth = chartWidth.coerceAtLeast(minChartWidth)

    ElevatedCard(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(28.dp)
    ) {
        Column(Modifier.padding(16.dp)) {
            Text(title, style = MaterialTheme.typography.titleMedium)
            Spacer(Modifier.height(8.dp))

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(scroll)
            ) {
                Box(
                    modifier = Modifier
                        .width(finalWidth)
                        .height(180.dp)
                        .background(cardBg.copy(alpha = 0.22f), RoundedCornerShape(24.dp))
                        .padding(horizontal = 12.dp, vertical = 10.dp)
                ) {
                    com.patrykandpatrick.vico.compose.cartesian.CartesianChartHost(
                        chart = com.patrykandpatrick.vico.compose.cartesian.rememberCartesianChart(
                            columnLayer,
                            startAxis  = com.patrykandpatrick.vico.core.cartesian.axis.VerticalAxis.rememberStart(valueFormatter = yFormatter),
                            bottomAxis = com.patrykandpatrick.vico.core.cartesian.axis.HorizontalAxis.rememberBottom(valueFormatter = xFormatter),
                        ),
                        modelProducer = modelProducer,
                        modifier = Modifier.fillMaxSize()
                    )
                }
            }
        }
    }
}

data class CategorySlice(val name: String, val valueMinor: Long, val color: Color)

@Composable
private fun PieChartCard(
    slices: List<CategorySlice>,
    totalLabel: String,
    height: Dp
) {
    val totalMinor = slices.sumOf { it.valueMinor }.coerceAtLeast(1) // avoid /0

    // Animations: 0→360 sweep + color fade-in
    val sweep by animateFloatAsState(
        targetValue = 360f,
        animationSpec = tween(durationMillis = 650, easing = FastOutSlowInEasing),
        label = "pie_sweep"
    )
    val fade by animateFloatAsState(
        targetValue = 1f,
        animationSpec = tween(durationMillis = 400, delayMillis = 120, easing = FastOutSlowInEasing),
        label = "pie_fade"
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
                // --- Chart ---
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight(),
                    contentAlignment = Alignment.Center
                ) {
                    Canvas(Modifier.fillMaxSize()) {
                        val diameter = size.minDimension
                        val radius = diameter / 2f
                        val center = Offset(size.width / 2f, size.height / 2f)
                        val topLeft = Offset(center.x - radius, center.y - radius)
                        val arcSize = Size(diameter, diameter)

                        var startAngle = -90f

                        // Draw slices + % labels
                        slices.forEach { s ->
                            val pct = s.valueMinor.toFloat() / totalMinor.toFloat()
                            val angle = pct * sweep

                            // 1) Slice with fade-in
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

                // --- Legend (unchanged) ---
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




/* ----------------------- Monthly: category bar list ----------------------- */

@Composable
private fun MonthlyCategoriesList(
    data: List<MonthlyCategoryData>,
    cardBg: Color
) {
    Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
        data.forEach { cat ->
            // Old Canvas-based version:
            // CategoryBarCard(title = cat.name, seriesA = cat.seriesA, seriesB = cat.seriesB, cardBg = cardBg)

            // New Vico version:
            CategoryBarCardVico(
                title = cat.name,
                seriesA = cat.seriesA,
                seriesB = cat.seriesB,
                cardBg = cardBg
            )
        }
    }
}


data class MonthlyCategoryData(
    val name: String,
    val seriesA: List<Float>,
    val seriesB: List<Float>
)





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

data class DateRange(val start: LocalDate, val end: LocalDate)

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
fun LocalDate.toEpochMillis(): Long =
    this.atStartOfDay(ZoneId.systemDefault()).toInstant().toEpochMilli()

fun millisToLocalDate(millis: Long): LocalDate =
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
private fun demoMonthlyCategorySeries(): List<MonthlyCategoryData> {
    val names = listOf("Food", "Going Out", "Household")
    fun randList() = List(12) { Random.nextInt(1_000, 16_000).toFloat() } // 12 months
    return names.map { n ->
        MonthlyCategoryData(
            name = n,
            seriesA = randList(), // used
            seriesB = randList()  // ignored by the single-series chart
        )
    }
}