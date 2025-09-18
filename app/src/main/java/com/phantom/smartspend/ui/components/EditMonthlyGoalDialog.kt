package com.phantom.smartspend.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.phantom.smartspend.data.model.UserData

@Composable
fun EditMonthlyGoalDialog(
    userData: UserData?,
    onDismiss: () -> Unit,
    onConfirm: (goal: Float?) -> Unit,
){

    var goal by remember { mutableStateOf(userData?.monthlySavingGoal.toString()) }

    AlertDialog(
        onDismissRequest = { onDismiss() },
        title = { Text("Edit Monthly Goal") },

        text = {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedTextField(
                    value = goal,
                    onValueChange = { goal = it },
                    label = { Text("Monthly Goal") },
                    singleLine = true,
                    keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number)
                )
            }
        },
        confirmButton = {
            TextButton(
                onClick = {
                    onDismiss()
                    onConfirm(goal.toFloat())
                }
            ) { Text("Save", color = MaterialTheme.colorScheme.primary) }
        },
        dismissButton = {
            TextButton(
                onClick = {
                    onDismiss()
                }
            ) { Text("Cancel", color = Color.Gray) }
        }
    )
}