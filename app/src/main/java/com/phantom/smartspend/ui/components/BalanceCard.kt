package com.phantom.smartspend.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.CallMade
import androidx.compose.material.icons.outlined.CallReceived
import androidx.compose.material.icons.outlined.QrCodeScanner
import androidx.compose.material.icons.outlined.RemoveRedEye
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview

@Composable
fun BalanceCard() {

    var amount by remember { mutableIntStateOf(20000) }
    var currency by remember { mutableStateOf("MKD") }


    Column(Modifier.fillMaxWidth()) {
        Text("Your Balance", color = MaterialTheme.colorScheme.primary)

        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()




        ) {
            Text("$amount $currency")
            IconButton(onClick = {}) {
                Icon(
                    imageVector = Icons.Outlined.RemoveRedEye,
                    contentDescription = "Show",
                    tint = MaterialTheme.colorScheme.primary
                )
            }
        }
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            Arrangement.SpaceBetween
        ) {
            IconButton(onClick = {}) {
                Icon(
                    imageVector = Icons.Outlined.CallMade,
                    contentDescription = "Expense",
                    tint = MaterialTheme.colorScheme.primary
                )
            }

            IconButton(onClick = {}) {
                Icon(
                    imageVector = Icons.Outlined.CallReceived,
                    contentDescription = "Income",
                    tint = MaterialTheme.colorScheme.primary
                )
            }

            IconButton(onClick = {}) {
                Icon(
                    imageVector = Icons.Outlined.QrCodeScanner,
                    contentDescription = "Scan",
                    tint = MaterialTheme.colorScheme.primary
                )
            }
        }

    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    BalanceCard()
}