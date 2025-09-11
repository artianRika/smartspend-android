package com.phantom.smartspend.ui.components

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.phantom.smartspend.nav.Screen
import com.phantom.smartspend.viewmodels.TransactionViewModel


@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun LastTransactions(
    navController: NavController,
    transactionViewModel: TransactionViewModel
) {
    LaunchedEffect(Unit) {
        transactionViewModel.getTransactions()
    }

    val transactions by transactionViewModel.transactions.collectAsState()

    val lastThreeTransactions = transactions?.take(3)


    Column(Modifier.fillMaxWidth()) {

        if(transactions.isNullOrEmpty()){
            Row(
                Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text("No transactions to show")
                TextButton(
                    onClick = {
                        navController.navigate(Screen.Transactions.route)
                    }
                ) {
                    Text("View More", color = MaterialTheme.colorScheme.primary, fontSize = 12.sp)
                }
            }
        }else {
            Row(
                Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.End
            ) {
                TextButton(
                    onClick = {
                        navController.navigate(Screen.Transactions.route)
                    }
                ) {
                    Text("View More", color = MaterialTheme.colorScheme.primary, fontSize = 12.sp)
                }
            }
        }
        Column {
            lastThreeTransactions?.forEach { item->
                SwipeableTransactionItem(
                    item,
                    true,
                    { },
                    { id->
                        transactionViewModel.setSelectedTransaction(item)
                        transactionViewModel.deleteTransaction(item.id)
                    }
                )
            }
        }
    }
}