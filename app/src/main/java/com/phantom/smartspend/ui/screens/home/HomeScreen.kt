package com.phantom.smartspend.ui.screens.home

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.phantom.smartspend.data.model.Transaction
import com.phantom.smartspend.nav.Screen
import com.phantom.smartspend.ui.components.BalanceCard
import com.phantom.smartspend.ui.components.DeleteTransactionDialog
import com.phantom.smartspend.ui.components.LastTransactions
import com.phantom.smartspend.ui.components.SavingsCard
import com.phantom.smartspend.viewmodels.AuthViewModel
import com.phantom.smartspend.viewmodels.TransactionViewModel
import com.phantom.smartspend.viewmodels.UserViewModel

@RequiresApi(Build.VERSION_CODES.UPSIDE_DOWN_CAKE)
@Composable
fun HomeScreen(
    modifier: Modifier,
    navController: NavController,
    authViewModel: AuthViewModel,
    userViewModel: UserViewModel,
    transactionViewModel: TransactionViewModel
) {

    LaunchedEffect(Unit) {
        userViewModel.getUserData()
        transactionViewModel.getTransactions()
    }

    val scrollState = rememberScrollState()
    val userData = userViewModel.userData.collectAsState()

    var showDeleteDialog by remember { mutableStateOf(false) }

    val selectedLog = transactionViewModel.selectedTransaction.collectAsState()



    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(scrollState),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {

        BalanceCard(userData.value)
        LastTransactions(navController, transactionViewModel)
        Spacer(modifier = Modifier.height(16.dp))
        SavingsCard(
            true,
            userData.value,
            onShowViewMoreClick = { navController.navigate(Screen.Savings.route) }
        )
        Column(modifier = Modifier.fillMaxWidth()) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("Balance Over Time")
                TextButton(
                    onClick = { navController.navigate(Screen.Stats.route) },
                ) {
                    Text("View More", color = MaterialTheme.colorScheme.primary, fontSize = 12.sp)
                }
            }
        }
    }

    if(showDeleteDialog){
        DeleteTransactionDialog(
            {
                showDeleteDialog = false
            },
            {
                transactionViewModel.deleteTransaction(selectedLog.value?.id)
            }
        )
    }
}
