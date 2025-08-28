package com.phantom.smartspend.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.CallMade
import androidx.compose.material.icons.outlined.CallReceived
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun TransactionItem(
    description: String,
    amount: Double,
    type: String,
    showBackground: Boolean
) {
    val modifier: Modifier = when(showBackground){
        true -> Modifier.fillMaxWidth().padding(vertical = 4.dp)
        false -> Modifier.fillMaxWidth().padding(top = 1.dp, bottom = 1.dp, end = 16.dp)
    }
    var currency by remember { mutableStateOf("MKD") }

    Card(
        modifier = modifier,
        shape = RoundedCornerShape(8.dp),
        elevation =
            if(showBackground)
                CardDefaults.cardElevation(defaultElevation = 2.dp)
            else
                CardDefaults.cardElevation(defaultElevation = 0.dp),
        colors =
            if(showBackground)
                CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.tertiary)
            else
                CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.background)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Icon(
                imageVector = if (type == "Income") Icons.Outlined.CallReceived else Icons.Outlined.CallMade,
                contentDescription = "Money In",
                tint = if (type == "Income") MaterialTheme.colorScheme.secondary else Color.Red
            )
            Text(
                modifier = Modifier.weight(1f).padding(horizontal = 16.dp),
                text = description,
                color = MaterialTheme.colorScheme.onPrimaryContainer,
                fontSize = 16.sp
            )
            Text("$currency $amount", color = if (type == "Income") MaterialTheme.colorScheme.secondary else Color.Red)
        }
    }
}

