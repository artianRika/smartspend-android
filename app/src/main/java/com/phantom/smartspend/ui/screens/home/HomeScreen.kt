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
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import com.phantom.smartspend.nav.Screen
import com.phantom.smartspend.ui.components.BalanceCard
import com.phantom.smartspend.ui.components.LastTransactions
import com.phantom.smartspend.ui.components.SavingsCard
import com.phantom.smartspend.ui.theme.Primary
import com.phantom.smartspend.ui.theme.PrimaryDark
import com.phantom.smartspend.ui.theme.PrimaryLight
import com.phantom.smartspend.ui.theme.Secondary
import com.phantom.smartspend.ui.theme.SecondaryLight
import com.phantom.smartspend.ui.theme.Tertiary
import com.phantom.smartspend.viewmodels.AuthViewModel
import com.phantom.smartspend.viewmodels.TransactionViewModel
import com.phantom.smartspend.viewmodels.UserViewModel
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel
import java.time.LocalDate
import java.time.YearMonth
import kotlin.math.roundToLong

@RequiresApi(Build.VERSION_CODES.UPSIDE_DOWN_CAKE)
@Composable
fun HomeScreen(
    modifier: Modifier,
    navController: NavController,
    authViewModel: AuthViewModel,
    userViewModel: UserViewModel,
    transactionViewModel: TransactionViewModel
) {

    val scrollState = rememberScrollState()
    val userData = userViewModel.userData.collectAsState()
    val transactions = transactionViewModel.transactions.collectAsState()


    val isRefreshing = transactionViewModel.isRefreshing.collectAsState()
    val swipeRefreshState = rememberSwipeRefreshState(isRefreshing.value)


    LaunchedEffect(Unit) {
        userViewModel.getUserData()
        transactionViewModel.getTransactions()
    }

    LaunchedEffect(transactions.value) {
        if (!transactions.value.isNullOrEmpty()) {
            userViewModel.getUserData()
        }
    }
    var anchorDate by remember { mutableStateOf(LocalDate.now()) }
    val startOfMonth = remember(anchorDate) { currentMonthRange(anchorDate).start }
    val endOfMonth = remember(anchorDate) { currentMonthRange(anchorDate).end }
    val pieChart by userViewModel.pieChart.collectAsState()
    val scope = rememberCoroutineScope()
    LaunchedEffect(anchorDate) {
        userViewModel.loadPieChart(
            from = startOfMonth.toRfc3339StartOfDay(),
            to = endOfMonth.toRfc3339EndOfDay()
        )

        println("📊 Requesting pie chart from $startOfMonth to $endOfMonth")
    }
    SwipeRefresh(
        state = swipeRefreshState,
        onRefresh = {
            scope.launch {
                userViewModel.getUserData()
                transactionViewModel.getTransactions()
                userViewModel.loadPieChart(
                    from = startOfMonth.toRfc3339StartOfDay(),
                    to = endOfMonth.toRfc3339EndOfDay()
                )
            }
        }

    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
                .verticalScroll(scrollState),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top
        ) {

            BalanceCard(userData.value)
            LastTransactions(navController, userViewModel, transactionViewModel)
            Spacer(modifier = Modifier.height(16.dp))
            SavingsCard(
                showViewMore = true,
                userData = userData.value,
                onShowViewMoreClick = { navController.navigate(Screen.Savings.route) },
                transactions = transactionViewModel.transactions.collectAsState().value ?: emptyList(),
                selectedMonth = YearMonth.now()
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
                        Text(
                            "View More",
                            color = MaterialTheme.colorScheme.primary,
                            fontSize = 12.sp
                        )
                    }
                }
                pieChart?.let { chart ->
                    val slices = chart.statistics.entries
                        .withIndex()
                        .map { (idx, e) ->
                            val (name, pct) = e
                            CategorySlice(name, pct.roundToLong(), getCategoryColor(idx))
                        }

                    if (slices.isNotEmpty()) {
                        PieChartCard(
                            slices = slices,
                            totalLabel = "This Month’s Spending",
                            height = 230.dp
                        )
                    } else {
                        Text("No spending data yet")
                    }
                } ?: Text("No data yet")


            }
        }
    }
}

    private val categoryColors = listOf(
    Primary,
    PrimaryDark,
    PrimaryLight,
    Secondary,
    SecondaryLight,
    Tertiary
)

fun getCategoryColor(index: Int): Color {
    return categoryColors[index % categoryColors.size]
}