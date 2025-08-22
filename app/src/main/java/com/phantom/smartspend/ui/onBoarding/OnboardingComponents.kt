package com.phantom.smartspend.ui.onBoarding

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun PillAmountField(
    modifier: Modifier = Modifier,
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String = "0",
) {
    val pillBg = MaterialTheme.colorScheme.tertiary
    val textColor = MaterialTheme.colorScheme.onPrimaryContainer
    val placeholderColor = MaterialTheme.colorScheme.onPrimaryContainer

    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        placeholder = {
            Text(
                text = placeholder,
                color = placeholderColor
            )
        },
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Number
        ),
        textStyle = LocalTextStyle.current.copy(
            fontWeight = FontWeight.Bold
        ),
        singleLine = true,
        shape = RoundedCornerShape(22.dp),
        colors = TextFieldDefaults.colors(
            // container
            focusedContainerColor = pillBg,
            unfocusedContainerColor = pillBg,
            disabledContainerColor = pillBg,
            // indicators
            focusedIndicatorColor = androidx.compose.ui.graphics.Color.Transparent,
            unfocusedIndicatorColor = androidx.compose.ui.graphics.Color.Transparent,
            disabledIndicatorColor = androidx.compose.ui.graphics.Color.Transparent,
            // cursor
            cursorColor = MaterialTheme.colorScheme.primary,
            // text colors
            focusedTextColor = textColor,
            unfocusedTextColor = textColor,
            disabledTextColor = textColor.copy(alpha = 0.4f),
            // placeholder colors
            focusedPlaceholderColor = placeholderColor,
            unfocusedPlaceholderColor = placeholderColor
        ),
        modifier = modifier.fillMaxWidth(0.6f)
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
    val unselectedBg = MaterialTheme.colorScheme.tertiary
    val selectedBg = MaterialTheme.colorScheme.secondaryContainer
    val unselectedTextColor = MaterialTheme.colorScheme.onPrimary
    val selectedTextColor = MaterialTheme.colorScheme.onPrimary

    FilterChip(
        selected = selected,
        onClick = onClick,
        label = {
            Text(
                text = text,
                fontSize = 16.sp,
                color = if (selected) selectedTextColor else unselectedTextColor
            )
        },
        shape = RoundedCornerShape(20.dp),
        colors = FilterChipDefaults.filterChipColors(
            containerColor = unselectedBg,
            selectedContainerColor = selectedBg,
            labelColor = unselectedTextColor,
            selectedLabelColor = selectedTextColor
        ),
        border = null,
        modifier = modifier.height(36.dp)
    )
}