package com.phantom.smartspend.ui.components

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.phantom.smartspend.data.model.Transaction
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun TransactionList(transactionList: List<Transaction>) {
    val grouped = transactionList.groupBy { it.date }

    Column {
        grouped.forEach { (date, transactions) ->
            val headerText = if (date == LocalDate.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy"))) {
                "Today"
            } else {
                val parsedDate = LocalDate.parse(date, DateTimeFormatter.ofPattern("dd-MM-yyyy"))
                parsedDate.format(DateTimeFormatter.ofPattern("dd MMM yyyy"))
            }

            Text(
                text = headerText,
                color = Color.Gray,
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.padding(vertical = 8.dp, horizontal = 16.dp)
            )

            transactions.forEach { item ->
                TransactionItem(
                    description = item.description,
                    amount = item.amount,
                    type = item.type,
                    showBackground = false
                )
            }
        }
    }
}
