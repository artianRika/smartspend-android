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
import androidx.navigation.NavController


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
import com.phantom.smartspend.nav.Screen
import com.phantom.smartspend.ui.components.SavingsInsightsSection
import com.phantom.smartspend.utils.Transaction
import com.phantom.smartspend.utils.buildSavingsSeries
import com.phantom.smartspend.viewmodels.TransactionViewModel


import kotlin.math.abs
import kotlin.math.round

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)

@Composable
fun SavingsScreen(
    userViewModel: UserViewModel,
    transactionViewModel: TransactionViewModel,
    navController: NavController
) {

    var currency by remember { mutableStateOf("MKD") }

    var totalIncome by remember { mutableIntStateOf(2000) }
    var totalExpense by remember { mutableIntStateOf(500) }


    val user = userViewModel.userData.collectAsState().value
    val monthlyGoal = user?.monthlySavingGoal ?: 0f
    val transactions = transactionViewModel.transactions.collectAsState().value ?: emptyList()
    val months = listOf("Jan","Feb","Mar","Apr","May","Jun","Jul","Aug","Sep","Oct","Nov","Dec")
//TODO IF THE USER GIVES MONTHLY SAVING GOAL GO IN THE FUNCTION DO TIMES 12 OF THAT SAVING GOAL
    val amounts = if (user != null) {
        buildSavingsSeries(user, transactions)
    } else {
        List(12) { 0f }
    }
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

        SavingsCard(
            showViewMore = false,
            userData = user,
            onShowViewMoreClick = {navController.navigate(Screen.Profile.route)},//TODO IF U GOT TIME MAKE THIS NOT REDIRECT BUT DISPLAY A INPUT FIELD
            transactions = transactions,
            selectedMonth = selectedMonth
        )
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
            SavingsLineChartVicoStyled(months, amounts, monthlyGoal)
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
            if (user != null) {
                SavingsInsightsSection(
                    transactions = transactions,
                    monthlyGoal = user.monthlySavingGoal,
                    currencyCode = user.preferredCurrency,
                    selectedMonth = selectedMonth
                )
            }
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
        horizontalArrangement = Arrangement.Center
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


///---------------------------------------------LINE CHART---------------------------------

@SuppressLint("RestrictedApi")
@Composable
fun SavingsLineChartVicoStyled(
    months: List<String>,
    amounts: List<Float>,
    monthlyGoal: Float,
    modifier: Modifier = Modifier,
) {
    val yearlyGoal = (monthlyGoal * 12f).coerceAtLeast(1f)

    val primaryValues: List<Double> = remember(amounts) {
        val clamped = amounts.map { v -> v.takeIf { it.isFinite() && it >= 0f } ?: 0f }
        val twelve = when {
            clamped.size == 12 -> clamped
            clamped.size > 12  -> clamped.take(12)
            else               -> clamped + List(12 - clamped.size) { 0f }
        }
        twelve.map { it.toDouble() }
    }

    val boundValues = listOf(0.0, yearlyGoal.toDouble())

    val marker = rememberDefaultCartesianMarker(
        label = rememberTextComponent(),
        guideline = rememberLineComponent(thickness = 1.dp)
    )

    val visibleLine = LineCartesianLayer.rememberLine(
        stroke   = LineCartesianLayer.LineStroke.continuous(thickness = 3.dp),
        fill     = LineCartesianLayer.LineFill.single(fill(MaterialTheme.colorScheme.primary)),
        areaFill = LineCartesianLayer.AreaFill.single(fill(MaterialTheme.colorScheme.primary.copy(alpha = 0.70f))),
    )
    val invisibleLine = LineCartesianLayer.rememberLine( // fully transparent
        stroke   = LineCartesianLayer.LineStroke.continuous(thickness = 0.dp),
        fill     = LineCartesianLayer.LineFill.single(fill(Color.Transparent)),
        areaFill = LineCartesianLayer.AreaFill.single(fill(Color.Transparent)),
    )
    val lineLayer = rememberLineCartesianLayer(
        lineProvider = LineCartesianLayer.LineProvider.series(visibleLine, invisibleLine)
    )

    val modelProducer = remember { CartesianChartModelProducer() }
    LaunchedEffect(primaryValues, yearlyGoal) {
        modelProducer.runTransaction {
            lineSeries {
                series(primaryValues)
                series(boundValues)
            }
        }
    }

    val xFormatter = CartesianValueFormatter { _, x, _ ->
        val i = x.toInt()
        if (i in months.indices) months[i] else "-"
    }
    val yFormatter = CartesianValueFormatter { _, v, _ ->
        val a = kotlin.math.abs(v)
        when {
            a >= 1_000_000 -> "${kotlin.math.round(v / 100_000) / 10.0}M"
            a >= 1_000     -> "${kotlin.math.round(v / 100) / 10.0}k"
            else           -> v.toInt().toString()
        }
    }

    CartesianChartHost(
        chart = rememberCartesianChart(
            lineLayer,
            startAxis  = VerticalAxis.rememberStart(
                valueFormatter = yFormatter,
                itemPlacer = VerticalAxis.ItemPlacer.step(
                    step = { (yearlyGoal / 6f).coerceAtLeast(1f).toDouble() }
                )
            ),
            bottomAxis = HorizontalAxis.rememberBottom(
                valueFormatter = xFormatter,
                itemPlacer = HorizontalAxis.ItemPlacer.aligned()
            ),
            marker = marker,
        ),
        modelProducer = modelProducer,
        modifier = modifier
    )
}

