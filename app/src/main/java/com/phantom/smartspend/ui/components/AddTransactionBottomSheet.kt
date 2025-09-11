package com.phantom.smartspend.ui.components

import DatePickerButton
import android.content.pm.PackageManager
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
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
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import com.phantom.smartspend.utils.saveBitmapToCache
import com.phantom.smartspend.viewmodels.TransactionViewModel
import com.phantom.smartspend.viewmodels.UploadState
import java.time.LocalDate
import java.time.ZoneId
import java.time.format.DateTimeFormatter

enum class TransactionType { Income, Expense }
enum class AddTransactionStep { TYPE, DETAILS }


@OptIn(ExperimentalMaterial3Api::class, ExperimentalAnimationApi::class)
@Composable
fun AddTransactionBottomSheet(
    transactionViewModel: TransactionViewModel,
    onDismiss: () -> Unit,
    onAddTransaction: (title: String, amount: String, type: String, date: String, categoryId: Int?) -> Unit
) {
    var currentStep by remember { mutableStateOf(AddTransactionStep.TYPE) }
    var selectedType by remember { mutableStateOf<TransactionType?>(TransactionType.Expense) }


    val expenseCategories = listOf("Food", "Transport", "Shopping", "Bills", "Others")  //TODO: get from vm
    var selectedCategory by remember { mutableStateOf("Others") }
    var selectedDate by remember {
        mutableStateOf(LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")))
    }

    var title by remember { mutableStateOf("") }
    var amount by remember { mutableStateOf("") }


    fun dateIntoTimeStamp(date: String): String{
        val localDate = LocalDate.parse(date, DateTimeFormatter.ofPattern("dd/MM/yyyy"))
        val zonedDateTime = localDate.atStartOfDay(ZoneId.systemDefault())
        return zonedDateTime.format(DateTimeFormatter.ISO_OFFSET_DATE_TIME)
    }

    val context = LocalContext.current
    var selectedImageUri by remember { mutableStateOf<Uri?>(null) }




    val cameraLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.TakePicturePreview()
    ) { bitmap ->
        bitmap?.let {
            val uri = saveBitmapToCache(context, it)
            selectedImageUri = uri

            transactionViewModel.uploadReceipt(context, uri)
        }
    }



    val cameraPermissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { granted ->
        if (granted) {
            cameraLauncher.launch(null)
        }
    }

    val uploadState by transactionViewModel.uploadState.collectAsState()

    selectedImageUri?.let {
        Text("Image captured. Upload status: ${
            when (uploadState) {
                is UploadState.Idle -> "Idle"
                is UploadState.Loading -> "Uploading..."
                is UploadState.Success -> "Success"
                is UploadState.Error -> "Error: ${(uploadState as UploadState.Error).message}"
            }
        }")
    }



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
                        Row(Modifier
                            .fillMaxWidth()
                            .height(45.dp), horizontalArrangement = Arrangement.Center) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth(.7f)
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
                            onClick = {
                                if (ContextCompat.checkSelfPermission(
                                        context,
                                        android.Manifest.permission.CAMERA
                                    ) == PackageManager.PERMISSION_GRANTED
                                ) {
                                    cameraLauncher.launch(null)
                                } else {
                                    // Request permission
                                    cameraPermissionLauncher.launch(android.Manifest.permission.CAMERA)
                                }
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 16.dp)
                                .height(56.dp)
                                .border(
                                    width = 1.dp,
                                    color = MaterialTheme.colorScheme.outline,
                                    shape = RoundedCornerShape(16.dp)
                                ),
                            enabled = selectedType == TransactionType.Expense,
                            shape = RoundedCornerShape(16.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = MaterialTheme.colorScheme.onPrimary,
                                contentColor = MaterialTheme.colorScheme.onSecondary
                            ),
                        ) {
                            Icon(
                                imageVector = Icons.Outlined.CameraAlt,
                                contentDescription = "Camera",
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
                            value = title,
                            onValueChange = {title = it},
                            label = { Text("Title") },
                            modifier = Modifier.fillMaxWidth()
                        )

                        OutlinedTextField(
                            value = amount,
                            onValueChange = { amount = it },
                            label = { Text("Amount") },
                            keyboardOptions = KeyboardOptions(
                                keyboardType = KeyboardType.Number
                            ),
                            modifier = Modifier.fillMaxWidth()
                        )

                        if (selectedType == TransactionType.Expense) {
                            CategoryDropdown(
                                selectedCategory = selectedCategory,
                                onCategorySelected = { selectedCategory = it },
                                categories = expenseCategories
                            )
                        }

                        Button(
                            onClick = {
                                onAddTransaction(
                                    title,
                                    amount,
                                    selectedType.toString(),
                                    dateIntoTimeStamp(selectedDate),
                                    if(selectedType == TransactionType.Expense)
                                        2 //TODO: category id
                                    else null
                                )
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
