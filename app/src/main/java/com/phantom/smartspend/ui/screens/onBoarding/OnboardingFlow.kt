package com.phantom.smartspend.ui.screens.onBoarding


import androidx.compose.runtime.*
import com.phantom.smartspend.ui.screens.onBoarding.steps.*
import com.phantom.smartspend.ui.screens.onboarding.steps.CurrencyStepUI

@Composable
fun OnboardingFlow(
    // TODO: later: onFinish: (answers) -> Unit
) {
    var step by remember { mutableStateOf(0) }

    // Local, UI-only state (replace with ViewModel later)
    var currency by remember { mutableStateOf<String?>(null) }
    var goal by remember { mutableStateOf("") }
    var balance by remember { mutableStateOf("") }
    var cats by remember { mutableStateOf(setOf<String>()) }

    val total = 4
    val prev = { if (step > 0) step-- }
    val next = { if (step < total - 1) step++ }
    val skip = { step = total - 1 } // or navigate out when you add nav

    when (step) {
        0 -> CurrencyStepUI(currency, onSelect = { currency = it }, onPrev = prev, onNext = next, onSkip = skip)
        1 -> MonthlyGoalStepUI(goal, onValueChange = { goal = it }, onPrev = prev, onNext = next, onSkip = skip)
        2 -> CurrentBalanceStepUI(balance, onValueChange = { balance = it }, onPrev = prev, onNext = next, onSkip = skip)
        3 -> CategoriesStepUI(
            selected = cats,
            onToggle = { l -> cats = if (l in cats) cats - l else cats + l },
            onPrev = prev,
            onNext = { /* TODO: finish & navigate */ },
            onSkip = skip
        )
    }
}
