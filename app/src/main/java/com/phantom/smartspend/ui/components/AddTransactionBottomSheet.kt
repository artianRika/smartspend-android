package com.phantom.smartspend.ui.components

import DatePickerButton
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.CameraAlt
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.focusModifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp

enum class TransactionType { INCOME, EXPENSE }
enum class AddTransactionStep { TYPE, DETAILS }


@OptIn(ExperimentalMaterial3Api::class, ExperimentalAnimationApi::class)
@Composable
fun AddTransactionBottomSheet(
    onDismiss: () -> Unit,
    onAddTransaction: () -> Unit
) {
    var currentStep by remember { mutableStateOf(AddTransactionStep.TYPE) }
    var selectedType by remember { mutableStateOf<TransactionType?>(TransactionType.EXPENSE) }


    val expenseCategories = listOf("Food", "Transport", "Shopping", "Bills", "Others")  //TODO: get from vm
    var selectedCategory by remember { mutableStateOf("Others") }
    var selectedDate by remember { mutableStateOf<String?>(null) }

    ModalBottomSheet(onDismissRequest = { onDismiss() }) {
        AnimatedContent(targetState = currentStep) { step ->
            when (step) {
                AddTransactionStep.TYPE -> {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(24.dp)
                    ) {
                        Row(Modifier.fillMaxWidth().height(45.dp), horizontalArrangement = Arrangement.Center) {
                            Row(
                                modifier = Modifier.fillMaxWidth(.7f)
                                    .height(45.dp),
                                horizontalArrangement = Arrangement.SpaceEvenly,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                TransactionType.entries.forEach { type ->
                                    Button(
                                        onClick = { selectedType = type },
                                        colors = ButtonDefaults.buttonColors(
                                            containerColor = if (selectedType == type)
                                                MaterialTheme.colorScheme.primary
                                            else
                                                MaterialTheme.colorScheme.surface,
                                            contentColor = if (selectedType == type)
                                                MaterialTheme.colorScheme.onPrimary
                                            else
                                                MaterialTheme.colorScheme.onSurface
                                        ),
                                        shape = RoundedCornerShape(14.dp),
                                        elevation = ButtonDefaults.buttonElevation(0.dp),
                                        modifier = Modifier
                                            .weight(.8f)
                                            .fillMaxHeight()
                                    ) {
                                        Text(
                                            text = type.name,
                                            style = MaterialTheme.typography.titleMedium
                                        )
                                    }
                                }
                            }
                        }

                        Button(
                            onClick = { },
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 16.dp)
                                .height(56.dp)
                                .border(
                                    width = 1.dp,
                                    color = MaterialTheme.colorScheme.outline,
                                    shape = RoundedCornerShape(16.dp)
                                ),
                            enabled = selectedType == TransactionType.EXPENSE,
                            shape = RoundedCornerShape(16.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = MaterialTheme.colorScheme.onPrimary,
                                contentColor = MaterialTheme.colorScheme.onSecondary
                            ),
                        ) {
                            Icon(
                                imageVector = Icons.Outlined.CameraAlt,
                                contentDescription = "Camera"
                            )
                        }

                        Button(
                            onClick = {
                                if (selectedType != null) currentStep = AddTransactionStep.DETAILS
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(56.dp),
                            shape = MaterialTheme.shapes.large,
                            colors = ButtonDefaults.buttonColors(
                                containerColor = MaterialTheme.colorScheme.primary,
                                contentColor = MaterialTheme.colorScheme.onPrimary
                            ),
                        ) {
                            Text(
                                text = "Next",
                                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Medium)
                            )
                        }
                    }
                }

                AddTransactionStep.DETAILS -> {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        Row(
                            Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = "${selectedType?.name} Details",
                                style = MaterialTheme.typography.titleMedium,
                            )


                            DatePickerButton(
                                selectedDate = selectedDate,
                                onDateSelected = { selectedDate = it }
                            )
                        }

                        OutlinedTextField(
                            value = "",
                            onValueChange = {},
                            label = { Text("Description") },
                            modifier = Modifier.fillMaxWidth()
                        )

                        if (selectedType == TransactionType.EXPENSE) {
                            CategoryDropdown(
                                selectedCategory = selectedCategory,
                                onCategorySelected = { selectedCategory = it },
                                categories = expenseCategories
                            )
                        }

                        OutlinedTextField(
                            value = "",
                            onValueChange = {},
                            label = { Text("Amount") },
                            keyboardOptions = KeyboardOptions(
                                keyboardType = KeyboardType.Number
                            ),
                            modifier = Modifier.fillMaxWidth()
                        )


                        Button(
                            onClick = { onAddTransaction },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(56.dp),
                            shape = MaterialTheme.shapes.large,
                            colors = ButtonDefaults.buttonColors(
                                containerColor = MaterialTheme.colorScheme.primary,
                                contentColor = MaterialTheme.colorScheme.onPrimary
                            ),
                        ) {
                            Text(
                                text = "Add Transaction",
                                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Medium)
                            )
                        }
                    }
                }
            }
        }
    }
}
