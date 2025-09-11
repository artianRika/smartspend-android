package com.phantom.smartspend.ui.components

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

@Composable
fun DeleteTransactionDialog(
    onDismiss: () -> Unit,
    onDelete: () -> Unit
){
    AlertDialog(
        onDismissRequest = {
            onDismiss()
        },
        title = { Text("Are you sure you want to delete this transaction?") },
        confirmButton = {
            TextButton(onClick = {
                //TODO: delete..
                onDelete()
                onDismiss()
            }) { Text("Delete", color = Color.Red) }
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