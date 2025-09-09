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
    onFinish: (balance: Float, monthlyGoal: Float, preferredCurrency: String) -> Unit
) {
    var step by remember { mutableIntStateOf(0) }

    var currency by remember { mutableStateOf("MKD") }
    var goal by remember { mutableStateOf("0") }
    var balance by remember { mutableStateOf("0") }

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
                onSkip = { onFinish(balance.toFloat(), goal.toFloat(), currency) }
            )
            1 -> MonthlyGoalStepUI(
                value = goal,
                onValueChange = { goal = it },
                onPrev = prev,
                onNext = next,
                onSkip = { onFinish(balance.toFloat(), goal.toFloat(), currency) }
            )
            2 -> CurrentBalanceStepUI(
                value = balance,
                onValueChange = { balance = it },
                onPrev = prev,
                onNext = { onFinish(balance.toFloat(), goal.toFloat(), currency) },
                onSkip = { onFinish(balance.toFloat(), goal.toFloat(), currency) }
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
