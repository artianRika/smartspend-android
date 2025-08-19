package com.phantom.smartspend.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ArrowDownward
import androidx.compose.material.icons.outlined.ArrowUpward
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun TransactionsBalanceCard() {

    var balance by remember { mutableIntStateOf(1500) }
    var currency by remember { mutableStateOf("MKD") }

    var weeklyIncome by remember { mutableIntStateOf(3700) }
    var weeklyExpense by remember { mutableIntStateOf(2200) }

    Column {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text("Balance", fontSize = 12.sp)
            Spacer(Modifier.fillMaxWidth())
            Text("$currency $balance", fontSize = 28.sp)
        }

        Row(
            Modifier.fillMaxWidth().padding(vertical = 8.dp, horizontal = 50.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Box(
                    modifier = Modifier
                        .size(45.dp)
                        .background(color = Color(0xC4C1FFC1), shape = CircleShape),
                    contentAlignment = Alignment.Center
                ){
                    Icon(
                        imageVector = Icons.Outlined.ArrowUpward,
                        contentDescription = "Income",
                        tint = Color.Green,
                    )
                }

                Column(
                    Modifier.padding(vertical = 16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text("+ $currency $weeklyIncome", fontSize = 12.sp)
                    Text("Weekly Income", fontSize = 12.sp)
                }
            }
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Box(
                    modifier = Modifier
                        .size(45.dp)
                        .background(color = Color(0xC4FFC1C6), shape = CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Outlined.ArrowDownward,
                        contentDescription = "Expenses",
                        tint = Color.Red
                    )
                }

                Column(
                    Modifier.padding(vertical = 16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text("- $currency $weeklyExpense", fontSize = 12.sp)
                    Text("Weekly Expense", fontSize = 12.sp)
                }
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
fun TransactionsBalanceCardPreview() {
    TransactionsBalanceCard()
}