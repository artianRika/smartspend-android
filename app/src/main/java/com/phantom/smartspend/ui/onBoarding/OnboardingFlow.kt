package com.phantom.smartspend.ui.onBoarding


import androidx.compose.runtime.*
import com.phantom.smartspend.ui.onBoarding.steps.*
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.tween
import androidx.compose.animation.togetherWith
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut


@Composable
fun OnboardingFlow(
    onFinish: () -> Unit
) {
    var step by remember { mutableIntStateOf(0) }

    // UI-only state (replace with ViewModel later)
    var currency by remember { mutableStateOf<String?>("MKD") }
    var goal by remember { mutableStateOf("") }
    var balance by remember { mutableStateOf("") }
    var cats by remember { mutableStateOf(setOf<String>()) }

    val total = 3
    val prev = { if (step > 0) step-- }
    val next = { if (step < total - 1) step++ }

    // Track direction (forward/back) for animation
    var lastStep by remember { mutableIntStateOf(0) }
    val forward = step >= lastStep
    LaunchedEffect(step) { lastStep = step }

    AnimatedContent(
        targetState = step,
        transitionSpec = {
            val dir = if (forward) 1 else -1
            (slideInHorizontally(animationSpec = tween(250)) { it * dir } + fadeIn()) togetherWith
                    (slideOutHorizontally(animationSpec = tween(250)) { -it * dir } + fadeOut())
        },
        label = "onboardingStepAnim"
    ) { s ->
        when (s) {
            0 -> CurrencyStepUI(
                selected = currency,
                onSelect = { currency = it },
                onPrev = prev,
                onNext = next,
                onSkip = { onFinish() }
            )
            1 -> MonthlyGoalStepUI(
                value = goal,
                onValueChange = { goal = it },
                onPrev = prev,
                onNext = next,
                onSkip = { onFinish() }
            )
            2 -> CurrentBalanceStepUI(
                value = balance,
                onValueChange = { balance = it },
                onPrev = prev,
                onNext = { onFinish() },
                onSkip = { onFinish() }
            )
//            3 -> CategoriesStepUI(
//                selected = cats,
//                onToggle = { l -> cats = if (l in cats) cats - l else cats + l },
//                onPrev = prev,
//                onNext = { onFinish() },
//                onSkip = { onFinish() }
//            )
        }
    }
}
