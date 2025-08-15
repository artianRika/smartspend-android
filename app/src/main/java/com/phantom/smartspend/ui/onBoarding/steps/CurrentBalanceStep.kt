package com.phantom.smartspend.ui.onBoarding.steps


import androidx.compose.runtime.Composable
import com.phantom.smartspend.ui.onBoarding.OnboardingStep
import com.phantom.smartspend.ui.onBoarding.PillAmountField

@Composable
fun CurrentBalanceStepUI(
    value: String,
    onValueChange: (String) -> Unit,
    onPrev: () -> Unit,
    onNext: () -> Unit,
    onSkip: () -> Unit
) {
    OnboardingStep(
        title = "What is your current balance?",
        onSkip = onSkip, onPrevious = onPrev, onNext = onNext,
        nextEnabled = true
    ) {
        PillAmountField(value = value, onValueChange = onValueChange)
    }
}