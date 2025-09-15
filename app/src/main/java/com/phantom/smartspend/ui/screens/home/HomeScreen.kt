package com.phantom.smartspend.ui.screens.home

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ElevatedCard
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
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.phantom.smartspend.nav.Screen
import com.phantom.smartspend.ui.components.BalanceCard
import com.phantom.smartspend.ui.components.LastTransactions
import com.phantom.smartspend.ui.components.SavingsCard
import com.phantom.smartspend.viewmodels.AuthViewModel
import com.phantom.smartspend.viewmodels.TransactionViewModel
import com.phantom.smartspend.viewmodels.UserViewModel
import com.phantom.smartspend.data.repository.UserRepository
import com.phantom.smartspend.ui.theme.Primary
import com.phantom.smartspend.ui.theme.PrimaryDark
import com.phantom.smartspend.ui.theme.PrimaryLight
import com.phantom.smartspend.ui.theme.Secondary
import com.phantom.smartspend.ui.theme.SecondaryLight
import com.phantom.smartspend.ui.theme.Tertiary
import com.phantom.smartspend.utils.DateUtils
import org.koin.androidx.compose.koinViewModel
import java.time.LocalDate
import java.time.LocalTime
import java.time.YearMonth
import java.time.ZoneOffset
import kotlin.math.roundToLong

@RequiresApi(Build.VERSION_CODES.UPSIDE_DOWN_CAKE)
@Composable
fun HomeScreen(
    modifier: Modifier,
    navController: NavController,
    authViewModel: AuthViewModel,
    userVm: UserViewModel = koinViewModel(),
    transactionViewModel: TransactionViewModel
) {
    val scrollState = rememberScrollState()
    val userData = userVm.userData.collectAsState()

    var anchorDate by remember { mutableStateOf(LocalDate.now()) }
    val startOfMonth = remember(anchorDate) { currentMonthRange(anchorDate).start }
    val endOfMonth = remember(anchorDate) { currentMonthRange(anchorDate).end }

    val pieChart by userVm.pieChart.collectAsState()

    LaunchedEffect(anchorDate) {
        userVm.loadPieChart(
            from = startOfMonth.toRfc3339StartOfDay(),
            to = endOfMonth.toRfc3339EndOfDay()
        )

        println("📊 Requesting pie chart from $startOfMonth to $endOfMonth")
    }
//    val fakePieChartData = listOf(
//        CategorySlice("Food", 35, Primary),          // 35%
//        CategorySlice("Transport", 20, Secondary),   // 20%
//        CategorySlice("Shopping", 25, PrimaryDark),  // 25%
//        CategorySlice("Entertainment", 10, SecondaryLight), // 10%
//        CategorySlice("Other", 10, PrimaryLight)     // 10%
//    )


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
                Text("Spending by category this month")
                TextButton(
                    onClick = { navController.navigate(Screen.Stats.route) },
                ) {
                    Text("View More", color = MaterialTheme.colorScheme.primary, fontSize = 12.sp)
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

/* ---------------- Helpers ---------------- */

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
