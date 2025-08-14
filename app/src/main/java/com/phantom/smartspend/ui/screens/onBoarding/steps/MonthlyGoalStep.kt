package com.phantom.smartspend.ui.screens.onBoarding.steps

import androidx.compose.runtime.Composable
import com.phantom.smartspend.ui.screens.onBoarding.OnboardingStep
import com.phantom.smartspend.ui.screens.onBoarding.PillAmountField

@Composable
fun MonthlyGoalStepUI(
    value: String,
    onValueChange: (String) -> Unit,
    onPrev: () -> Unit,
    onNext: () -> Unit,
    onSkip: () -> Unit
) {
    OnboardingStep(
        title = "What is your saving monthly goal?",
        onSkip = onSkip, onPrevious = onPrev, onNext = onNext,
        nextEnabled = value.isNotBlank()
    ) {
        PillAmountField(value = value, onValueChange = onValueChange)
    }
}