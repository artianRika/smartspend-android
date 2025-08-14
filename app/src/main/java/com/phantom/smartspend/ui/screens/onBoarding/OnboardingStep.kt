package com.phantom.smartspend.ui.screens.onBoarding

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChevronLeft
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import com.phantom.smartspend.R

@Composable
fun OnboardingStep(
    title: String,
    onSkip: () -> Unit,           // TODO navigate away later
    onPrevious: () -> Unit,       // TODO previous step
    onNext: () -> Unit,           // TODO next step
    nextEnabled: Boolean = true,
    centered: Boolean = true,
    content: @Composable ColumnScope.() -> Unit
) {
    val screenBg = Color.White
    val cardBg = Color(0xFFF0F0F0)
    val divider = Color(0xFFE3E3E3)
    val navGray = Color(0xFF8C8C8C)

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(screenBg)
    ) {
        val cardMod = if (centered) {
            Modifier
                .fillMaxWidth(0.92f)
                .height(420.dp)
                .align(Alignment.Center)
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
            tonalElevation = 0.dp
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
                            .size(28.dp)
                            .background(
                                color = MaterialTheme.colorScheme.primary.copy(alpha = .16f),
                                shape = RoundedCornerShape(10.dp)
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.smart_spend),
                            contentDescription = "Mini logo",
                            modifier = Modifier.size(18.dp)
                        )
                    }
                    Spacer(Modifier.weight(1f))
                    TextButton(onClick = onSkip) {
                        Text(
                            "Skip for now",
                            color = Color.Black,
                            style = MaterialTheme.typography.labelLarge,
                            textDecoration = TextDecoration.Underline
                        )
                    }
                }

                Divider(color = divider)

                // Title
                Text(
                    text = title,
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 18.dp),
                    style = MaterialTheme.typography.titleLarge,
                    color = Color.Black
                )

                // Body slot
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .padding(horizontal = 16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                    content = content
                )

                // Footer nav
                Row(
                    Modifier
                        .fillMaxWidth()
                        .padding(start = 8.dp, end = 8.dp, bottom = 10.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(onClick = onPrevious) {
                        Icon(Icons.Filled.ChevronLeft, contentDescription = "Previous", tint = navGray)
                    }
                    Text("Previous", color = navGray, modifier = Modifier.weight(1f))
                    Text("Next", color = if (nextEnabled) navGray else navGray.copy(alpha = .4f))
                    IconButton(onClick = onNext, enabled = nextEnabled) {
                        Icon(Icons.Filled.ChevronRight, contentDescription = "Next", tint = navGray)
                    }
                }
            }
        }
    }
}
