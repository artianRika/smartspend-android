package com.phantom.smartspend.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun TransactionsBalanceCard() {

    var balance by remember { mutableIntStateOf(1500) }
    var currency by remember { mutableStateOf("MKD") }

    var weeklyIncome by remember { mutableIntStateOf(3700) }
    var weeklyExpense by remember { mutableIntStateOf(2200) }

    Column(Modifier.padding(top = 16.dp)) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text("Balance", fontSize = 12.sp)
            Spacer(Modifier.fillMaxWidth())
            Text("$currency $balance", fontSize = 28.sp)
        }

        Row(
            Modifier.fillMaxWidth().padding(vertical = 16.dp, horizontal = 50.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                horizontalAlignment = Alignment.Start
            ) {
                Text("Weekly Income", fontSize = 12.sp)
                Text("$currency $weeklyIncome", fontSize = 18.sp, color = MaterialTheme.colorScheme.secondary)

            }

            VerticalDivider(Modifier.height(35.dp), thickness = 3.dp)

            Column(
                horizontalAlignment = Alignment.Start
            ) {
                Text("Weekly Expense", fontSize = 12.sp)
                Text("$currency $weeklyExpense", fontSize = 18.sp, color = Color.Red)

            }
        }
    }
}


@Preview(showBackground = true)
@Composable
fun TransactionsBalanceCardPreview() {
    TransactionsBalanceCard()
}