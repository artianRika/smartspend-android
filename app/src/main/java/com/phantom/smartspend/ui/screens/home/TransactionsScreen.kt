package com.phantom.smartspend.ui.screens.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.phantom.smartspend.ui.components.Transaction
import com.phantom.smartspend.ui.components.TransactionList
import com.phantom.smartspend.ui.components.TransactionsBalanceCard


val list = mutableListOf(
    Transaction("ATM", 2600.0, "Income", "07-08-2025"),
    Transaction("Food", 120.0, "Expense", "09-08-2025"),
    Transaction("ATM", 500.0, "Income", "10-08-2025"),
    Transaction("Rent", 1350.45, "Expense", "15-05-2025"),
    Transaction("ATM", 2400.0, "Income", "28-05-2025"),
    Transaction("Food", 85.75, "Expense", "03-06-2025"),
    Transaction("Salary", 3200.0, "Income", "12-06-2025"),
    Transaction("Transport", 60.0, "Expense", "25-06-2025"),
    Transaction("Shopping", 220.5, "Expense", "01-07-2025"),
    Transaction("ATM", 500.0, "Income", "07-07-2025"),
    Transaction("Entertainment", 120.0, "Expense", "19-07-2025"),
    Transaction("Food", 150.0, "Expense", "02-08-2025"),
    Transaction("ATM", 2600.0, "Income", "07-08-2025"),
)
@Composable
fun TransactionsScreen() {
    val scrollState = rememberScrollState()

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                containerColor = MaterialTheme.colorScheme.surface,
                contentColor = MaterialTheme.colorScheme.onSurface,
                onClick = {  }
            ) {
                Icon(Icons.Default.Add, contentDescription = "Add")
            }
        }
    ) { _ ->
        Column(
            modifier = Modifier.fillMaxSize().padding().verticalScroll(scrollState),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            TransactionsBalanceCard()

            TransactionList(list)
        }

    }
}