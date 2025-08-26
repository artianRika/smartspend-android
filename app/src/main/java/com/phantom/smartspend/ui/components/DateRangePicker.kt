package com.phantom.smartspend.ui.components

import android.app.DatePickerDialog
import android.os.Build
import android.widget.DatePicker
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun DateRangePicker(
    onDismiss: () -> Unit,
    onDateRangeSelected: (from: String, to: String) -> Unit
) {
    val context = LocalContext.current

    val formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy")

    val now = LocalDate.now()
    val twoWeeksAgo = now.minusDays(14)

    var fromDate by remember { mutableStateOf(twoWeeksAgo.format(formatter)) }
    var toDate by remember { mutableStateOf(now.format(formatter)) }

    val fromLocalDate = LocalDate.parse(fromDate, formatter)
    val toLocalDate = LocalDate.parse(toDate, formatter)

    val fromDatePickerDialog = DatePickerDialog(
        context,
        { _: DatePicker, year: Int, month: Int, day: Int ->
            fromDate = LocalDate.of(year, month + 1, day).format(formatter)
        },
        fromLocalDate.year, fromLocalDate.monthValue - 1, fromLocalDate.dayOfMonth
    )

    val toDatePickerDialog = DatePickerDialog(
        context,
        { _: DatePicker, year: Int, month: Int, day: Int ->
            toDate = LocalDate.of(year, month + 1, day).format(formatter)
        },
        toLocalDate.year, toLocalDate.monthValue - 1, toLocalDate.dayOfMonth
    )

    AlertDialog(
        onDismissRequest = { onDismiss() },
        title = { Text("Select Date Range") },
        text = {
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                OutlinedTextField(
                    value = fromDate,
                    onValueChange = {},
                    label = { Text("From Date") },
                    readOnly = true,
                    modifier = Modifier.fillMaxWidth(),
                    trailingIcon = {
                        IconButton(onClick = { fromDatePickerDialog.show() }) {
                            Icon(Icons.Default.DateRange, contentDescription = "Pick From Date")
                        }
                    }
                )

                OutlinedTextField(
                    value = toDate,
                    onValueChange = {},
                    label = { Text("To Date") },
                    readOnly = true,
                    modifier = Modifier.fillMaxWidth(),
                    trailingIcon = {
                        IconButton(onClick = { toDatePickerDialog.show() }) {
                            Icon(Icons.Default.DateRange, contentDescription = "Pick To Date")
                        }
                    }
                )
            }
        },
        confirmButton = {
            TextButton(
                onClick = {
                    onDateRangeSelected(fromDate, toDate)
                    onDismiss()
                }
            ) {
                Text("OK")
            }
        },
        dismissButton = {
            TextButton(onClick = { onDismiss() }) {
                Text("Cancel")
            }
        }
    )
}
