package com.phantom.smartspend.ui.onBoarding

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChevronLeft
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.phantom.smartspend.R

@Composable
fun OnboardingStep(
    title: String,
    onSkip: () -> Unit,
    onPrevious: () -> Unit,
    onNext: () -> Unit,
    centered: Boolean = true,
    content: @Composable ColumnScope.() -> Unit
) {
    // Use theme colors that adapt to light/dark mode
    val screenBg = MaterialTheme.colorScheme.background
    val cardBg = MaterialTheme.colorScheme.surface
    val dividerColor = MaterialTheme.colorScheme.outline.copy(alpha = 0.3f)
    val navGray = MaterialTheme.colorScheme.onSurfaceVariant
    val titleColor = MaterialTheme.colorScheme.onSurface
    val skipButtonColor = MaterialTheme.colorScheme.primary

    val focusManager = LocalFocusManager.current

    Box(
        modifier = Modifier
        .fillMaxSize()
        .background(screenBg)
        .imePadding()
        .clickable(
            indication = null,
            interactionSource = remember { MutableInteractionSource() }
        ) {
            focusManager.clearFocus()
        }
    ) {
        val cardMod = if (centered) {
            Modifier
                .fillMaxWidth(0.92f)
                .height(420.dp)
                .align(Alignment.Center)
                .padding(24.dp)
        } else {
            Modifier
                .padding(horizontal = 16.dp)
                .padding(top = 72.dp)
                .fillMaxWidth()
                .height(420.dp)
        }

        Surface(
            modifier = cardMod,
            color = cardBg,
            shape = RoundedCornerShape(28.dp),
            shadowElevation = 6.dp,
            tonalElevation = 2.dp // Use tonal elevation for better dark mode support
        ) {
            Column(Modifier.fillMaxSize()) {
                // Header
                Row(
                    Modifier
                        .fillMaxWidth()
                        .padding(start = 16.dp, end = 12.dp, top = 12.dp, bottom = 8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(28.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.smart_spend_logo),
                            contentDescription = "Mini logo",
                            modifier = Modifier.size(25.dp)
                        )
                    }
                    Spacer(Modifier.weight(1f))
                    TextButton(onClick = onSkip) {
                        Text(
                            "Skip for now",
                            color = skipButtonColor,
                            style = MaterialTheme.typography.labelLarge,
                            textDecoration = TextDecoration.Underline
                        )
                    }
                }

                HorizontalDivider(color = dividerColor)

                // Title
                Text(
                    text = title,
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp),
                    style = MaterialTheme.typography.titleLarge,
                    color = titleColor
                )

                // Body slot
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .padding(horizontal = 16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    content = content
                )

                Spacer(Modifier.fillMaxWidth())

                // Footer nav

                Row(
                    Modifier
                        .fillMaxWidth()
                        .padding(start = 8.dp, end = 8.dp, top = 12.dp, bottom = 8.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    // Prev Button
                    TextButton(
                        onClick = onPrevious,
                        colors = ButtonDefaults.textButtonColors(
                            contentColor = navGray,
                            disabledContentColor = navGray.copy(alpha = 0.4f)
                        )
                    ) {
                        Icon(
                            Icons.Filled.ChevronLeft,
                            contentDescription = "Prev"
                        )
                        Text(
                            text = "Prev",
                            style = MaterialTheme.typography.labelLarge
                        )
                    }

                    // Next Button
                    TextButton(
                        onClick = onNext,
                        colors = ButtonDefaults.textButtonColors(
                            contentColor = navGray,
                            disabledContentColor = navGray.copy(alpha = 0.4f)
                        )
                    ) {
                        Text(
                            text = if(!title.contains("balance") ) "Next" else "Finish",
                            style = MaterialTheme.typography.labelLarge
                        )
                        Icon(
                            Icons.Filled.ChevronRight,
                            contentDescription = "Next"
                        )
                    }
                }
            }
        }
    }
}

@Composable
@Preview(showBackground = true)
fun OnboardingStepPreview(){
    OnboardingStep(
    "title", {}, {}, {}, true, {})
}

