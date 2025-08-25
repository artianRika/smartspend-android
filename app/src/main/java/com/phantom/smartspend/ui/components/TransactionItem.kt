package com.phantom.smartspend.ui.components

import androidx.compose.foundation.background
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun TransactionItem(
    description: String,
    amount: Double,
    type: String,
    showBackground: Boolean
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
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
                CardDefaults.cardColors(containerColor = Color.Transparent)
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
                tint = if (type == "Income") Color.Green else Color.Red
            )
            Text(
                modifier = Modifier.weight(1f).padding(horizontal = 16.dp),
                text = description,
                color = MaterialTheme.colorScheme.onPrimaryContainer,
                fontSize = 16.sp
            )
            Text(amount.toString(), color = if (type == "Income") Color.Green else Color.Red)
        }
    }
}

