package com.phantom.smartspend.ui.onBoarding

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun WelcomeScreen(
    userName: String,
    onGetStarted: () -> Unit,//TODO CHange this to onGetStarted = {Navigate to ...}
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 24.dp, vertical = 16.dp)
                .systemBarsPadding(), // handles notch/status
        ) {
            Spacer(Modifier.height(24.dp))

            // Greeting
            Text(
                text = "Hello!",
                color = MaterialTheme.colorScheme.primary,
                fontSize = 40.sp,
                fontWeight = FontWeight.Bold,
                lineHeight = 44.sp
            )
            Text(
                text = userName,
                color = MaterialTheme.colorScheme.primary,
                fontSize = 40.sp,
                fontWeight = FontWeight.Bold,
                lineHeight = 44.sp
            )

            Spacer(Modifier.height(38.dp))

            // Lead copy
            Text(
                text = "Your money, your rules.\nSmart Spend helps you track, save, and master your finances— effortlessly.",
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f),
                lineHeight = 22.sp
            )

            Spacer(modifier = Modifier.weight(1f))

            // Fine print paragraph
            Text(
                text = "Before you get started, we’ll ask for a few quick details to personalize your experience. " +
                        "Once that’s done, you’ll be ready to take control and master your spending.",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f),
                lineHeight = 18.sp
            )

            Spacer(Modifier.height(26.dp))

            // CTA pinned to bottom
            Button(
                onClick = onGetStarted,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
                    .navigationBarsPadding(), // keeps above gesture bar
                shape = MaterialTheme.shapes.large,
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = MaterialTheme.colorScheme.onPrimary
                ),
                contentPadding = PaddingValues(horizontal = 24.dp)
            ) {
                Text(
                    text = "Get Started with us",
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Medium),
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}