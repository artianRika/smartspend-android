package com.phantom.smartspend.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun SavingsCard(
    showViewMore: Boolean,
    onShowViewMoreClick: (() -> Unit)?
) {

    var month by remember { mutableStateOf("August") }

    var saved by remember { mutableIntStateOf(1500) }
    var currency by remember { mutableStateOf("MKD") }
    var monthlyGoal by remember { mutableIntStateOf(2000) }

    Card(
        modifier = Modifier
            .fillMaxWidth(),
        shape = RoundedCornerShape(8.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.tertiary)
    ) {

        Column(
            modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp)
        ) {
            Row(
                Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text("Saved this $month", color = MaterialTheme.colorScheme.onPrimaryContainer,
                    fontSize = 12.sp)
                if (showViewMore) {
                    TextButton(
                        onClick = { onShowViewMoreClick?.invoke() },
                        contentPadding = PaddingValues(0.dp)
                    ) {
                        Text(
                            "View More",
                            color = MaterialTheme.colorScheme.primary,
                            fontSize = 12.sp
                        )
                    }
                }else{
                    TextButton(
                        onClick = { onShowViewMoreClick?.invoke() },
                        contentPadding = PaddingValues(0.dp)
                    ) {
                        Text(
                            "Edit Goal",
                            color = MaterialTheme.colorScheme.primary,
                            fontSize = 12.sp
                        )
                    }
                }
            }


            Column {
                Text("$currency $saved", color = MaterialTheme.colorScheme.onPrimaryContainer, fontSize = 24.sp, fontWeight = FontWeight.SemiBold)
                HorizontalProgressBar(0.75f)
            }
            Row(
                Modifier.fillMaxWidth().padding(bottom = 8.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text("0", color = MaterialTheme.colorScheme.onPrimaryContainer, fontSize = 12.sp)
                Text("$monthlyGoal", color = MaterialTheme.colorScheme.onPrimaryContainer,
                    fontSize = 12.sp)
            }
        }
    }
}

@Composable
@Preview(showBackground = true)
fun showw() {
    SavingsCard(true, null)
}
