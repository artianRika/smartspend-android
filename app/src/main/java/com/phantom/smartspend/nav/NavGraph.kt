package com.phantom.smartspend.nav

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.phantom.smartspend.data.local.OnboardingPreferences
import com.phantom.smartspend.ui.onBoarding.OnboardingFlow
import com.phantom.smartspend.ui.onBoarding.WelcomeScreen
import com.phantom.smartspend.ui.screens.auth.LoginScreenGoogle
import com.phantom.smartspend.ui.screens.home.HomeScreen
import com.phantom.smartspend.ui.screens.home.SavingsScreen
import com.phantom.smartspend.ui.screens.profile.ProfileScreen
import com.phantom.smartspend.ui.screens.home.StatsScreen
import com.phantom.smartspend.ui.screens.home.TransactionsScreen
import com.phantom.smartspend.viewmodels.AuthViewModel
import com.phantom.smartspend.viewmodels.TransactionViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.koin.compose.viewmodel.koinViewModel

@RequiresApi(Build.VERSION_CODES.UPSIDE_DOWN_CAKE)
@Composable
fun NavGraph(navController: NavHostController, startDestination: String, modifier: Modifier = Modifier) {

    val authViewModel: AuthViewModel = koinViewModel()
    val transactionViewModel: TransactionViewModel = koinViewModel()

    NavHost(
        navController = navController,
        startDestination = startDestination,
        modifier = modifier
    ) {



        composable("login") {
            LoginScreenGoogle(onGoogleClick = {
                val fakeName = "nameeeee"
                navController.navigate("welcome/$fakeName") {
                    popUpTo("login") { inclusive = true }
                }
            })
        }

        composable("welcome/{userName}") { backStackEntry ->
            val userName = backStackEntry.arguments?.getString("userName") ?: "User"
            WelcomeScreen(userName = userName, onGetStarted = {
                navController.navigate("onboarding") {
                    popUpTo("login") { inclusive = true }
                }
            })
        }

        composable(
            "onboarding",
            exitTransition = {
                // Only add exit transition when going to home
                if (targetState.destination.route == Screen.Home.route) {
                    slideOutVertically(
                        targetOffsetY = { -it },
                        animationSpec = tween(500)
                    ) + fadeOut(animationSpec = tween(500))
                } else {
                    null
                }
            }
        ) {
            val scope = rememberCoroutineScope()

            OnboardingFlow(onFinish = {
                scope.launch {
                    try {
                        delay(200)
                        OnboardingPreferences.setOnboardingDone(navController.context, true)

                        navController.navigate(Screen.Home.route) {
                            popUpTo("login") { inclusive = true }
                            launchSingleTop = true
                        }
                    } catch (e: Exception) {
                        e.printStackTrace()
                        // Fallback navigation without delay
                        navController.navigate(Screen.Home.route) {
                            popUpTo("login") { inclusive = true }
                            launchSingleTop = true
                        }
                    }
                }
            })
        }

        composable(
            Screen.Home.route,
            enterTransition = {
                // Only add enter transition when coming from onboarding
                if (initialState.destination.route == "onboarding") {
                    slideInVertically(
                        initialOffsetY = { it },
                        animationSpec = tween(500)
                    ) + fadeIn(animationSpec = tween(500)) + scaleIn(
                        initialScale = 0.95f,
                        animationSpec = tween(500)
                    )
                } else {
                    null
                }
            }
        ) {
            HomeScreen(modifier, navController, authViewModel, transactionViewModel)
        }
        composable(Screen.Profile.route) { ProfileScreen() }
        composable(Screen.Savings.route) { SavingsScreen() }
        composable(Screen.Transactions.route) { TransactionsScreen(transactionViewModel) }
        composable(Screen.Stats.route) { StatsScreen() }
    }
}