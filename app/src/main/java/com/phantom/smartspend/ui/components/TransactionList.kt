package com.phantom.smartspend.ui.components

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.phantom.smartspend.data.model.Transaction
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun TransactionList(transactionList: List<Transaction>) {

    var showEditTransactionBottomSheet by remember { mutableStateOf(false) }


    val grouped = transactionList.groupBy { it.date }

    LazyColumn {
        grouped.forEach { (date, transactions) ->
            val headerText =
                if (date == LocalDate.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy"))) {
                    "Today"
                } else {
                    val parsedDate =
                        LocalDate.parse(date, DateTimeFormatter.ofPattern("dd-MM-yyyy"))
                    parsedDate.format(DateTimeFormatter.ofPattern("dd MMM yyyy"))
                }

            item {
                Text(
                    text = headerText,
                    color = Color.Gray,
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.padding(vertical = 8.dp, horizontal = 16.dp)
                )
            }

            items(transactions) { transaction ->
                SwipeableTransactionItem(
                    description = transaction.description,
                    amount = transaction.amount,
                    type = transaction.type,
                    showBackground = false,
                    onEdit = {
                        //setSelectedTransaction(transaction)
                        showEditTransactionBottomSheet = true
                    },
                    onDelete = { /* Handle delete */ }
                )
            }
        }
    }

//    if(showEditTransactionBottomSheet){
//        EditTransactionBottomSheet(
//            ,
//        ) { }
//    }
}
