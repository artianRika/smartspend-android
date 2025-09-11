package com.phantom.smartspend.ui.components

import android.os.Build
import androidx.annotation.RequiresApi
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
import java.time.OffsetDateTime
import java.time.format.DateTimeFormatter

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun TransactionList(transactionList: List<Transaction>?) {

    var showEditTransactionBottomSheet by remember { mutableStateOf(false) }

    if (transactionList != null) {
        val grouped = transactionList.groupBy { OffsetDateTime.parse(it.date).toLocalDate() }

        LazyColumn {
            grouped.forEach { (localDate, transactions) ->
                val headerText = if (localDate == LocalDate.now()) {
                    "Today"
                } else {
                    localDate.format(DateTimeFormatter.ofPattern("dd MMM yyyy"))
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
                        transaction,
                        showBackground = false,
                        onEdit = { id->
                            //setSelectedTransaction(transaction)
                            showEditTransactionBottomSheet = true
                        },
                        onDelete = { /* Handle delete id-> */ }
                    )
                }
            }
        }
    }
//    if(showEditTransactionBottomSheet){
//        EditTransactionBottomSheet(
//            ,
//        ) { }
//    }
}
