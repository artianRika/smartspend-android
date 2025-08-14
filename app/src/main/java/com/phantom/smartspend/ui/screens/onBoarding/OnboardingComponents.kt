package com.phantom.smartspend.ui.screens.onBoarding

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun PillAmountField(
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String = "Amount",
    modifier: Modifier = Modifier
) {
    val pillBg = Color(0xFFDBDBDB)
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange, // TODO restrict to numbers later
        placeholder = { Text(placeholder, color = Color.Black) },
        singleLine = true,
        shape = RoundedCornerShape(22.dp),
        colors = TextFieldDefaults.colors(
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent,
            disabledIndicatorColor = Color.Transparent,
            focusedContainerColor = pillBg,
            unfocusedContainerColor = pillBg,
            cursorColor = MaterialTheme.colorScheme.onSurface
        ),
        modifier = modifier
            .height(44.dp)
            .fillMaxWidth(0.6f)
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GrayPillChip(
    text: String,
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val bg = if (selected) Color(0xFFD0D0D0) else Color(0xFFDBDBDB)
    FilterChip(
        selected = selected,
        onClick = onClick,
        label = { Text(text, color = Color.Black, fontSize = 16.sp) },
        shape = RoundedCornerShape(20.dp),
        colors = FilterChipDefaults.filterChipColors(
            containerColor = bg,
            selectedContainerColor = bg,
            labelColor = Color(0xFF6F6F6F)
        ),
        border = null,
        modifier = modifier.height(36.dp)
    )
}
