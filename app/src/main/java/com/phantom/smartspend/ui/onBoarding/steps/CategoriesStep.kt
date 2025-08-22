//package com.phantom.smartspend.ui.onBoarding.steps
//
//import androidx.compose.foundation.layout.Arrangement
//import androidx.compose.foundation.layout.ExperimentalLayoutApi
//import androidx.compose.foundation.layout.FlowRow
//import androidx.compose.foundation.layout.fillMaxWidth
//import androidx.compose.runtime.Composable
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.unit.dp
//import com.phantom.smartspend.ui.onBoarding.GrayPillChip
//import com.phantom.smartspend.ui.onBoarding.OnboardingStep
//
//@OptIn(ExperimentalLayoutApi::class)
//@Composable
//fun CategoriesStepUI(
//    selected: Set<String>,
//    onToggle: (String) -> Unit,
//    onPrev: () -> Unit,
//    onNext: () -> Unit,
//    onSkip: () -> Unit
//) {
//    val options = listOf("Fashion", "Food", "School", "House bills", "Going out", "Health")
//    OnboardingStep(
//        title = "Select some of our pre-defined categories.",
//        onSkip = onSkip, onPrevious = onPrev, onNext = onNext,
//        nextEnabled = selected.isNotEmpty()
//    ) {
//        FlowRow(
//            modifier = Modifier.fillMaxWidth(),
//            horizontalArrangement = Arrangement.spacedBy(12.dp),
//            verticalArrangement = Arrangement.spacedBy(12.dp)
//        ) {
//            options.forEach { label ->
//                GrayPillChip(
//                    text = label,
//                    selected = label in selected,
//                    onClick = { onToggle(label) }
//                )
//            }
//        }
//    }
//}