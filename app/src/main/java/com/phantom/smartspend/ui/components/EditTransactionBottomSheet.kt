//package com.phantom.smartspend.ui.components
//
//
//import DatePickerButton
//import androidx.compose.foundation.layout.Arrangement
//import androidx.compose.foundation.layout.Column
//import androidx.compose.foundation.layout.Row
//import androidx.compose.foundation.layout.fillMaxWidth
//import androidx.compose.foundation.layout.height
//import androidx.compose.foundation.layout.padding
//import androidx.compose.foundation.text.KeyboardOptions
//import androidx.compose.material3.Button
//import androidx.compose.material3.ButtonDefaults
//import androidx.compose.material3.ExperimentalMaterial3Api
//import androidx.compose.material3.MaterialTheme
//import androidx.compose.material3.ModalBottomSheet
//import androidx.compose.material3.OutlinedTextField
//import androidx.compose.material3.Text
//import androidx.compose.runtime.Composable
//import androidx.compose.ui.Alignment
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.text.font.FontWeight
//import androidx.compose.ui.text.input.KeyboardType
//import androidx.compose.ui.unit.dp
//
//@OptIn(ExperimentalMaterial3Api::class)
//@Composable
//fun EditTransactionBottomSheet(
//    descriptionValue: String,
//    amountValue: String,
//    selectedType: TransactionType,
//    selectedCategory: String,
//    selectedDate: String?,
//    onDescriptionChange: (String) -> Unit,
//    onAmountChange: (String) -> Unit,
//    onCategoryChange: (String) -> Unit,
//    onDateChange: (String) -> Unit,
//    onSave: () -> Unit,
//    onDismiss: () -> Unit
//) {
//    val expenseCategories = listOf("Food", "Transport", "Shopping", "Bills", "Others") // TODO: fetch from VM
//
//    ModalBottomSheet(onDismissRequest = { onDismiss() }) {
//        Column(
//            modifier = Modifier
//                .fillMaxWidth()
//                .padding(16.dp),
//            verticalArrangement = Arrangement.spacedBy(16.dp)
//        ) {
//            // Header with type and date
//            Row(
//                Modifier.fillMaxWidth(),
//                verticalAlignment = Alignment.CenterVertically,
//                horizontalArrangement = Arrangement.SpaceBetween
//            ) {
//                Text(
//                    text = "${selectedType.name} Details",
//                    style = MaterialTheme.typography.titleMedium
//                )
//
//                DatePickerButton(
//                    selectedDate = selectedDate,
//                    onDateSelected = { onDateChange(it) }
//                )
//            }
//
//            // Description
//            OutlinedTextField(
//                value = descriptionValue,
//                onValueChange = onDescriptionChange,
//                label = { Text("Description") },
//                modifier = Modifier.fillMaxWidth()
//            )
//
//            // Category (only for Expense)
//            if (selectedType == TransactionType.EXPENSE) {
//                CategoryDropdown(
//                    selectedCategory = selectedCategory,
//                    onCategorySelected = onCategoryChange,
//                    categories = expenseCategories
//                )
//            }
//
//            // Amount
//            OutlinedTextField(
//                value = amountValue,
//                onValueChange = onAmountChange,
//                label = { Text("Amount") },
//                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
//                modifier = Modifier.fillMaxWidth()
//            )
//
//            // Save button
//            Button(
//                onClick = onSave,
//                modifier = Modifier
//                    .fillMaxWidth()
//                    .height(56.dp),
//                shape = MaterialTheme.shapes.large,
//                colors = ButtonDefaults.buttonColors(
//                    containerColor = MaterialTheme.colorScheme.primary,
//                    contentColor = MaterialTheme.colorScheme.onPrimary
//                ),
//            ) {
//                Text(
//                    text = "Save",
//                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Medium)
//                )
//            }
//        }
//    }
//}
