package com.phantom.smartspend.ui.components

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FilterAlt
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun TransactionsBalanceCard() {

    var showDatePicker by remember { mutableStateOf(false) }

    val formatter = DateTimeFormatter.ofPattern("dd MMM yyyy")

    val now = LocalDate.now()
    val twoWeeksAgo = now.minusDays(14)
    var fromDate by remember { mutableStateOf(twoWeeksAgo.format(formatter)) }
    var toDate by remember { mutableStateOf(now.format(formatter)) }


    val enhancedToDate =
        if (toDate == LocalDate.now().format(DateTimeFormatter.ofPattern("dd MMM yyyy"))) {
            "Today"
        } else {
            toDate
        }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        shape = RoundedCornerShape(8.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.tertiary)
    ) {
        Row(
            Modifier
                .fillMaxWidth()
                .padding(start = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("$fromDate - $enhancedToDate", color = MaterialTheme.colorScheme.onPrimaryContainer,)

            IconButton(onClick = { showDatePicker = true }) {
                Icon(
                    imageVector = Icons.Default.FilterAlt,
                    contentDescription = "Filter Date",
                    tint = MaterialTheme.colorScheme.onPrimaryContainer,
                )
            }
        }
    }
    if (showDatePicker) {
        DateRangePicker(
            fromDate,
            toDate,
            formatter,
            onDismiss = { showDatePicker = false },
            onDateRangeSelected = { from, to ->
                fromDate = from
                toDate = to
            }
        )
    }
}


@RequiresApi(Build.VERSION_CODES.O)
@Preview(showBackground = true)
@Composable
fun TransactionsBalanceCardPreview() {
    TransactionsBalanceCard()
}