package com.phantom.smartspend.ui.onBoarding.steps

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.phantom.smartspend.ui.onBoarding.GrayPillChip
import com.phantom.smartspend.ui.onBoarding.OnboardingStep

@Composable
fun CurrencyStepUI(
    selected: String?,
    options: List<String> = listOf("MKD", "EUR", "USD"),
    onSelect: (String) -> Unit,
    onPrev: () -> Unit,
    onNext: () -> Unit,
    onSkip: () -> Unit
) {
    OnboardingStep(
        title = "What is your preferred currency?",
        onSkip = onSkip, onPrevious = onPrev, onNext = onNext,
    ) {
        Spacer(Modifier.height(6.dp))
        options.forEach { code ->
            GrayPillChip(
                text = code,
                selected = selected == code,
                onClick = { onSelect(code) },
                modifier = Modifier
            )
        }
    }
}
