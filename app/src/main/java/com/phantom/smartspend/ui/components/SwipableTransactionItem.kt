package com.phantom.smartspend.ui.components

import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.AnchoredDraggableState
import androidx.compose.foundation.gestures.DraggableAnchors
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.anchoredDraggable
import androidx.compose.foundation.gestures.animateTo
import androidx.compose.foundation.gestures.snapTo
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import kotlinx.coroutines.launch
import kotlin.math.roundToInt

enum class DragAnchors {
    Start,
    End
}

@Composable
fun SwipeableTransactionItem(
    description: String,
    amount: Double,
    type: String,
    showBackground: Boolean,
    onEdit: () -> Unit,
    onDelete: () -> Unit
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
                    scope.launch {
                        swipeState.animateTo(
                            targetValue = DragAnchors.Start,
                            animationSpec = tween(durationMillis = 300)
                        )
                    }
                    onEdit()
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
                description = description,
                amount = amount,
                type = type,
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
                    onDelete()
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
}
