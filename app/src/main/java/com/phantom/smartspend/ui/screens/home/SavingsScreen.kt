package com.phantom.smartspend.ui.screens.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
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
import com.phantom.smartspend.ui.components.SavingsCard

@Composable
fun SavingsScreen() {

    var currency by remember { mutableStateOf("MKD") }

    var totalIncome by remember { mutableIntStateOf(2000) }
    var totalExpense by remember { mutableIntStateOf(500) }

    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        SavingsCard(showViewMore = false, null)

        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(
                Modifier.fillMaxWidth().padding(vertical = 16.dp, horizontal = 50.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(
                    horizontalAlignment = Alignment.Start
                ) {
                    Text("Total Income", fontSize = 12.sp)
                    Text("$currency $totalIncome", fontSize = 18.sp, color = Color.Green)

                }

                VerticalDivider(Modifier.height(35.dp), thickness = 3.dp)

                Column(
                    horizontalAlignment = Alignment.Start
                ) {
                    Text("Total Expense", fontSize = 12.sp)
                    Text("$currency $totalExpense", fontSize = 18.sp, color = Color.Red)

                }
            }
        }
    }
}

@Composable
@Preview(showBackground = true)
fun SavingsScreenPreview(){
    SavingsScreen()
}