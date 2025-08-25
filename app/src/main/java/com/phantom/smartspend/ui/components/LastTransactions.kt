package com.phantom.smartspend.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.phantom.smartspend.nav.Screen
import java.sql.Date


data class Transaction(val description: String, val amount: Double, val type: String, val date: String)
@Composable
fun LastTransactions(
    navController: NavController
) {
    val list = mutableListOf(
        Transaction("ATM", 2600.0, "Income", "07-08-2025"),
        Transaction("Food", 120.0, "Expense", "09-08-2025"),
        Transaction("ATM", 500.0, "Income", "10-08-2025"),
        )
    Column(Modifier.fillMaxWidth()) {
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
            TextButton(
                onClick = {
                    navController.navigate(Screen.Transactions.route)
                }
            ) {
                Text("View More", color = MaterialTheme.colorScheme.primary, fontSize = 12.sp)
            }
        }
        Column {
            list.forEach { item->
                TransactionItem(item.description, item.amount, item.type, true)
            }
        }
    }
}