package com.phantom.smartspend.ui.screens.home

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import com.phantom.smartspend.ui.components.TransactionList
import com.phantom.smartspend.ui.components.TransactionsBalanceCard
import com.phantom.smartspend.viewmodels.TransactionViewModel
import com.phantom.smartspend.viewmodels.UserViewModel


@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun TransactionsScreen(transactionViewModel: TransactionViewModel, userViewModel: UserViewModel) {

    val transactions by transactionViewModel.transactions.collectAsState()
    val isRefreshing by transactionViewModel.isRefreshing.collectAsState()
    val swipeRefreshState = rememberSwipeRefreshState(isRefreshing)

    LaunchedEffect(Unit) {
        transactionViewModel.getTransactions()
    }

    SwipeRefresh(
        state = swipeRefreshState,
        onRefresh = {
            transactionViewModel.getTransactions()
        }
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            TransactionsBalanceCard()

            TransactionList(transactionViewModel, userViewModel,transactions)
        }
    }

}