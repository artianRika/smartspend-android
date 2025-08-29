package com.phantom.smartspend.ui.screens.home

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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.phantom.smartspend.ui.components.SavingsCard
import java.time.Instant
import java.time.LocalDate
import java.time.YearMonth
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.time.temporal.WeekFields
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import java.time.format.TextStyle
import java.util.Locale
import com.phantom.smartspend.ui.screens.home.StatsScreen
@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)

@Composable
fun SavingsScreen() {

    var currency by remember { mutableStateOf("MKD") }

    var totalIncome by remember { mutableIntStateOf(2000) }
    var totalExpense by remember { mutableIntStateOf(500) }

    // --- Month-only selection state ---
    var selectedMonth by remember { mutableStateOf(YearMonth.now()) }
    var showMonthPicker by remember { mutableStateOf(false) }
    val pickerState = rememberDatePickerState(
        // initialize to the first day of the current month
        initialSelectedDateMillis = selectedMonth.atDay(1).toEpochMillis()
    )
    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Centered month header (tap to change month)
        DateHeader(
            text = selectedMonth.formatMonthYear(),
            onClick = { showMonthPicker = true },
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 12.dp)
        )

        SavingsCard(showViewMore = false, null)

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
        }
    }
    // --- Month picker dialog (uses DatePicker, we coerce to YearMonth on OK) ---
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

/** If you need the full month range later: */
@RequiresApi(Build.VERSION_CODES.O)
private fun YearMonth.asDateRange(): DateRange =
    DateRange(this.atDay(1), this.atEndOfMonth())
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






/** e.g., "8–15 Aug 2025" or "01–31 Aug 2025" */
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