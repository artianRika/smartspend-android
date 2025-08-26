package com.phantom.smartspend.ui.screens.home

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.phantom.smartspend.ui.components.TransactionList
import com.phantom.smartspend.ui.components.TransactionsBalanceCard
import com.phantom.smartspend.viewmodels.AuthViewModel
import com.phantom.smartspend.viewmodels.TransactionViewModel
import org.koin.compose.viewmodel.koinViewModel


@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun TransactionsScreen(transactionViewModel: TransactionViewModel) {
    val scrollState = rememberScrollState()

    val transactions by transactionViewModel.transactions.collectAsState()
    
    LaunchedEffect(Unit) {
        transactionViewModel.getTransactions()
    }


    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding()
            .verticalScroll(scrollState),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        TransactionsBalanceCard()

        TransactionList(transactions)
    }


}