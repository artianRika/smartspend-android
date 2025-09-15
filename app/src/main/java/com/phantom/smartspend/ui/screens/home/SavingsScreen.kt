package com.phantom.smartspend.ui.screens.home

import android.annotation.SuppressLint
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color

import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.phantom.smartspend.ui.components.SavingsCard

import java.time.YearMonth

import java.time.format.DateTimeFormatter

import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.collectAsState
import com.patrykandpatrick.vico.compose.common.component.rememberTextComponent
import androidx.compose.runtime.LaunchedEffect


import java.time.format.TextStyle
import java.util.Locale
import com.phantom.smartspend.ui.screens.home.StatsScreen
import com.phantom.smartspend.viewmodels.UserViewModel



import com.patrykandpatrick.vico.compose.cartesian.CartesianChartHost
import com.patrykandpatrick.vico.compose.cartesian.rememberCartesianChart
import com.patrykandpatrick.vico.compose.cartesian.layer.rememberLineCartesianLayer

import com.patrykandpatrick.vico.compose.cartesian.axis.rememberBottom
import com.patrykandpatrick.vico.compose.cartesian.axis.rememberStart
import com.patrykandpatrick.vico.core.cartesian.layer.LineCartesianLayer
import com.patrykandpatrick.vico.core.cartesian.data.lineSeries


import com.patrykandpatrick.vico.core.cartesian.axis.HorizontalAxis
import com.patrykandpatrick.vico.core.cartesian.axis.VerticalAxis
import com.patrykandpatrick.vico.compose.cartesian.layer.continuous
import com.patrykandpatrick.vico.compose.cartesian.layer.rememberLine
import com.patrykandpatrick.vico.compose.common.fill

import com.patrykandpatrick.vico.core.cartesian.data.CartesianChartModelProducer
import com.patrykandpatrick.vico.compose.cartesian.marker.rememberDefaultCartesianMarker
import com.patrykandpatrick.vico.compose.common.component.rememberTextComponent
import com.patrykandpatrick.vico.compose.common.component.rememberLineComponent

import com.patrykandpatrick.vico.core.cartesian.data.CartesianValueFormatter
import com.phantom.smartspend.ui.components.SavingsInsightsSection
import com.phantom.smartspend.utils.Transaction


import kotlin.math.abs
import kotlin.math.round

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)

@Composable
fun SavingsScreen(
    userViewModel: UserViewModel
) {

    var currency by remember { mutableStateOf("MKD") }

    var totalIncome by remember { mutableIntStateOf(2000) }
    var totalExpense by remember { mutableIntStateOf(500) }

    var selectedMonth by remember { mutableStateOf(YearMonth.now()) }
    var showMonthPicker by remember { mutableStateOf(false) }
    val pickerState = rememberDatePickerState(
        initialSelectedDateMillis = selectedMonth.atDay(1).toEpochMillis()
    )
    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        DateHeader(
            text = selectedMonth.formatMonthYear(),
            onClick = { showMonthPicker = true },
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 12.dp)
        )

        SavingsCard(showViewMore = false, userViewModel.userData.collectAsState().value,null)

        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(
                Modifier.fillMaxWidth().padding(vertical = 16.dp, horizontal = 50.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(
                    horizontalAlignment = Alignment.Start
                ) {
                    Text("Total Income", fontSize = 12.sp)
                    Text("$currency $totalIncome", fontSize = 18.sp, color = MaterialTheme.colorScheme.secondary)

                }

                VerticalDivider(Modifier.height(35.dp), thickness = 3.dp)

                Column(
                    horizontalAlignment = Alignment.Start
                ) {
                    Text("Total Expense", fontSize = 12.sp)
                    Text("$currency $totalExpense", fontSize = 18.sp, color = Color.Red)

                }
            }
            val months = listOf("Jan","Feb","Mar","Apr","May","Jun","Jul","Aug","Sep","Oct","Nov","Dec")
            val amounts = listOf(80f,120f,200f,260f,300f,360f,420f,500f,590f,680f,760f,900f)
            SavingsLineChartVicoStyled(months, amounts)
            Spacer(Modifier.height(16.dp))
            val sampleTransactions = listOf(
                Transaction(
                    amountMinor = 200000,
                    category = "Salary",
                    timestamp = System.currentTimeMillis()
                ),
                Transaction(amountMinor = -50000, category = "Groceries", timestamp = System.currentTimeMillis()),
                Transaction(amountMinor = -30000, category = "Transport", timestamp = System.currentTimeMillis()),
                Transaction(amountMinor = -20000, category = "Dining", timestamp = System.currentTimeMillis())
            )
            SavingsInsightsSection(
                transactions = sampleTransactions,
                goalMinor = 150000,
                currencyCode = "MKD"
            )
        }
    }
    if (showMonthPicker) {
        DatePickerDialog(
            onDismissRequest = { showMonthPicker = false },
            confirmButton = {
                TextButton(onClick = {
                    pickerState.selectedDateMillis?.let { millis ->
                        selectedMonth = YearMonth.from(millisToLocalDate(millis))
                    }
                    showMonthPicker = false
                }) { Text("OK") }
            },
            dismissButton = {
                TextButton(onClick = { showMonthPicker = false }) { Text("Cancel") }
            }
        ) {
            DatePicker(state = pickerState)
        }
    }
}


/*----------------------DATE UTILITY----------------------*/
@RequiresApi(Build.VERSION_CODES.O)
private fun YearMonth.formatMonthYear(): String =
    "${this.month.getDisplayName(TextStyle.FULL, Locale.getDefault())} ${this.year}"

@RequiresApi(Build.VERSION_CODES.O)
private fun YearMonth.asDateRange(): DateRange =
    DateRange(this.atDay(1), this.atEndOfMonth())
@Composable
fun DateHeader(
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






@RequiresApi(Build.VERSION_CODES.O)
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


//---------------------------------------------LINE CHART---------------------------------

@SuppressLint("RestrictedApi")
@Composable
fun SavingsLineChartVicoStyled(
    months: List<String>,
    amounts: List<Float>,
    modifier: Modifier = Modifier,
) {



    val markerLabel = rememberTextComponent()

    val markerGuideline = rememberLineComponent(
        thickness = 1.dp,
    )

    val marker = rememberDefaultCartesianMarker(
        label = markerLabel,
        guideline = markerGuideline
    )

    val modelProducer = remember { CartesianChartModelProducer() }
    val values = remember(amounts) {
        amounts.map { v -> v.takeIf { it.isFinite() && it >= 0f }?.toDouble() ?: 0.0 }
            .ifEmpty { listOf(0.0) }
    }
    LaunchedEffect(values) {
        modelProducer.runTransaction {
            lineSeries { series(values) }
        }
    }


    val xFormatter = CartesianValueFormatter { _, x, _ -> months.getOrNull(x.toInt()) ?: "" }
    val yFormatter = CartesianValueFormatter { _, v, _ ->
        val a = abs(v)
        when {
            a >= 1_000_000 -> "${round(v / 100_000) / 10.0}M"
            a >= 1_000     -> "${round(v / 100) / 10.0}k"
            else           -> v.toInt().toString()
        }
    }


    val primary = MaterialTheme.colorScheme.primary
    val line = LineCartesianLayer.rememberLine(
        stroke   = LineCartesianLayer.LineStroke.continuous(thickness = 3.dp),
        fill     = LineCartesianLayer.LineFill.single(fill(primary)),
        areaFill = LineCartesianLayer.AreaFill.single(fill(primary.copy(alpha = 0.70f))),
    )

    val lineLayer = rememberLineCartesianLayer(
        lineProvider = LineCartesianLayer.LineProvider.series(line),
    )

    CartesianChartHost(
        chart = rememberCartesianChart(
            lineLayer,
            startAxis  = VerticalAxis.rememberStart(valueFormatter = yFormatter),
            bottomAxis = HorizontalAxis.rememberBottom(valueFormatter = xFormatter),
            marker = marker,
        ),
        modelProducer = modelProducer,

        modifier = modifier
    )
}
