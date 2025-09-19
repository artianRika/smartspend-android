package com.phantom.smartspend.ui.components

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
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
import com.phantom.smartspend.viewmodels.TransactionViewModel
import com.phantom.smartspend.viewmodels.UserViewModel
import java.time.LocalDate
import java.time.OffsetDateTime
import java.time.format.DateTimeFormatter

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun TransactionList(
    transactionViewModel: TransactionViewModel,
    userViewModel: UserViewModel,
    transactionList: List<Transaction>?
) {
    if (transactionList == null) return

    val groupedTransactions = transactionList
        .sortedByDescending { OffsetDateTime.parse(it.date) }
        .groupBy { OffsetDateTime.parse(it.date).toLocalDate() }

    LazyColumn {
        groupedTransactions.forEach { (date, transactions) ->
            item(key = "header_$date") {
                Text(
                    text = if (date == LocalDate.now()) "Today"
                    else date.format(DateTimeFormatter.ofPattern("dd MMM yyyy")),
                    color = Color.Gray,
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.padding(vertical = 8.dp, horizontal = 16.dp)
                )
            }

            items(transactions, key = { it.id }) { transaction ->
                var visible by remember(transaction.id) { mutableStateOf(true) }

                AnimatedVisibility(
                    visible = visible,
                    enter = fadeIn() + slideInHorizontally(initialOffsetX = { it }),
                    exit = fadeOut() + slideOutHorizontally(targetOffsetX = { it }),
                ) {
                    SwipeableTransactionItem(
                        transactionViewModel,
                        userViewModel,
                        transaction,
                        showBackground = false,
                        onEdit = { /* edit logic */ },
                        onDelete = {
                            visible = false
                            transactionViewModel.deleteTransaction(transaction.id)
                        }
                    )
                }
            }
        }
    }
}