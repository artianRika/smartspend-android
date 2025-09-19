package com.phantom.smartspend.ui.components

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.AnchoredDraggableState
import androidx.compose.foundation.gestures.DraggableAnchors
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.anchoredDraggable
import androidx.compose.foundation.gestures.animateTo
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import com.phantom.smartspend.data.model.Transaction
import com.phantom.smartspend.ui.screens.home.currentMonthRange
import com.phantom.smartspend.ui.screens.home.toRfc3339EndOfDay
import com.phantom.smartspend.ui.screens.home.toRfc3339StartOfDay
import com.phantom.smartspend.viewmodels.TransactionViewModel
import com.phantom.smartspend.viewmodels.UserViewModel
import kotlinx.coroutines.launch
import java.time.LocalDate
import kotlin.math.roundToInt

enum class DragAnchors {
    Start,
    End
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun SwipeableTransactionItem(
    transactionViewModel: TransactionViewModel,
    userViewModel: UserViewModel,
    transaction: Transaction,
    showBackground: Boolean,
    onEdit: (logId: Int) -> Unit,
    onDelete: (logId: Int) -> Unit
) {
    val density = LocalDensity.current
    val swipeWidthPx = with(density) { 120.dp.toPx() }

    val anchors = DraggableAnchors {
        DragAnchors.Start at 0f
        DragAnchors.End at -swipeWidthPx
    }

    val swipeState = remember {
        AnchoredDraggableState(
            initialValue = DragAnchors.Start,
            anchors = anchors
        )
    }

    val scope = rememberCoroutineScope()
    var showDeleteDialog by remember { mutableStateOf(false) }
    var showEditTransactionBottomSheet by remember { mutableStateOf(false) }

    var anchorDate by remember { mutableStateOf(LocalDate.now()) }
    val startOfMonth = remember(anchorDate) { currentMonthRange(anchorDate).start }
    val endOfMonth = remember(anchorDate) { currentMonthRange(anchorDate).end }


    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(55.dp)
    ) {
        Row(
            modifier = Modifier
                .matchParentSize()
                .background(color = Color.Transparent),
            horizontalArrangement = Arrangement.End,
            verticalAlignment = Alignment.CenterVertically
        ) {
            TextButton(
                contentPadding = PaddingValues(0.dp),
                onClick = {
                    showEditTransactionBottomSheet = true
                    onEdit(transaction.id)
                },
                modifier = Modifier
                    .width(50.dp)
                    .background(Color.Transparent, RoundedCornerShape(8.dp))
            ) {
                Icon(
                    imageVector = Icons.Filled.Edit,
                    contentDescription = "Edit",
                    tint = Color.Gray
                )
            }

            Spacer(modifier = Modifier.width(8.dp))
            TextButton(
                contentPadding = PaddingValues(0.dp),
                onClick = {
                    showDeleteDialog = true
                },
                modifier = Modifier
                    .width(50.dp)
                    .background(Color.Transparent, RoundedCornerShape(8.dp))
            ) {
                Icon(
                    imageVector = Icons.Filled.Delete,
                    contentDescription = "Delete",
                    tint = Color.Red
                )
            }
        }

        Box(
            modifier = Modifier
                .offset { IntOffset(swipeState.offset.roundToInt(), 0) }
                .zIndex(1f)
                .anchoredDraggable(
                    state = swipeState,
                    orientation = Orientation.Horizontal
                )
        ) {
            TransactionItem(
                title = transaction.title,
                amount = transaction.amount,
                type = transaction.type,
                showBackground = showBackground
            )
        }
    }

    if (showDeleteDialog) {
        AlertDialog(
            onDismissRequest = { showDeleteDialog = false },
            title = { Text("Delete Transaction") },
            text = { Text("Are you sure you want to delete this transaction?") },
            confirmButton = {
                TextButton(onClick = {
                    onDelete(transaction.id)
                    showDeleteDialog = false
                }) { Text("Delete") }
            },
            dismissButton = {
                TextButton(
                    onClick = {
                        scope.launch {
                            swipeState.animateTo(
                                targetValue = DragAnchors.Start,
                                animationSpec = tween(durationMillis = 300)
                            )
                        }
                        showDeleteDialog = false
                    }
                ) { Text("Cancel") }
            }
        )
    }

    if(showEditTransactionBottomSheet){
        EditTransactionBottomSheet(
            transactionViewModel,
            transaction,
            {
                showEditTransactionBottomSheet = false
            },
            { title, amount, date, categoryId ->
                transactionViewModel.editTransaction(
                    transaction.id,
                    title, amount, date, categoryId
                )
                onEdit(transaction.id)



                showEditTransactionBottomSheet = false
                scope.launch {
                    userViewModel.getUserData()
                    userViewModel.loadPieChart(
                        from = startOfMonth.toRfc3339StartOfDay(),
                        to = endOfMonth.toRfc3339EndOfDay()
                    )
                    swipeState.animateTo(
                        targetValue = DragAnchors.Start,
                        animationSpec = tween(durationMillis = 300)
                    )
                }



            }
        )
    }
}
