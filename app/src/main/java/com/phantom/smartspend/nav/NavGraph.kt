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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.phantom.smartspend.data.local.OnboardingPreferences
import com.phantom.smartspend.ui.onBoarding.OnboardingFlow
import com.phantom.smartspend.ui.onBoarding.WelcomeScreen
import com.phantom.smartspend.ui.screens.auth.LoginScreenGoogle
import com.phantom.smartspend.ui.screens.home.HomeScreen
import com.phantom.smartspend.ui.screens.home.SavingsScreen
import com.phantom.smartspend.ui.screens.home.StatsScreen
import com.phantom.smartspend.ui.screens.home.TransactionsScreen
import com.phantom.smartspend.ui.screens.profile.ProfileScreen
import com.phantom.smartspend.viewmodels.AuthViewModel
import com.phantom.smartspend.viewmodels.TransactionViewModel
import com.phantom.smartspend.viewmodels.UserViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import org.koin.compose.viewmodel.koinViewModel

@RequiresApi(Build.VERSION_CODES.UPSIDE_DOWN_CAKE)
@Composable
fun NavGraph(
    navController: NavHostController,
    startDestination: String,
    modifier: Modifier = Modifier,
    authViewModel: AuthViewModel,
    userViewModel: UserViewModel
) {

    val transactionViewModel: TransactionViewModel = koinViewModel()

    val userData by userViewModel.userData.collectAsState()

    NavHost(
        navController = navController,
        startDestination = startDestination,
        modifier = modifier
    ) {


        composable("login") {
            val context = LocalContext.current
            val isAuthenticated by authViewModel.isAuthenticated.collectAsState()


//            LaunchedEffect(isAuthenticated, userData) {
//                if (isAuthenticated && userData != null) {
//                    navController.navigate("welcome/${userData?.firstName} ${userData?.lastName}") {
//                        popUpTo("login") { inclusive = true }
//                        launchSingleTop = true
//                    }
//                }
//            }

            LaunchedEffect(isAuthenticated, userData) {
                if (isAuthenticated) {
                    val data = userViewModel.getUserData()
                    if (data != null) {
                        navController.navigate("welcome/${data.firstName} ${data.lastName}") {
                            popUpTo("login") { inclusive = true }
                            launchSingleTop = true
                        }
                    }
                }
            }

            LoginScreenGoogle(
                authViewModel,
                onGoogleClick = {
                    authViewModel.signInWithGoogleNative(context)
                }
            )
        }


        composable("welcome/{userName}") { backStackEntry ->

            val context = LocalContext.current
            val boardingDone = runBlocking {
                OnboardingPreferences.isOnboardingDone(context)
            }

            val userName = backStackEntry.arguments?.getString("userName") ?: "User"
            if(userName != "null null") {
                WelcomeScreen(fullName = userName, onGetStarted = {
                    if (boardingDone) {
                        navController.navigate(Screen.Home.route) {
                            popUpTo("login") { inclusive = true }
                        }
                    } else {
                        navController.navigate("onboarding") {
                            popUpTo("login") { inclusive = true }
                        }
                    }
                })
            }
        }

        composable(
            "onboarding",
            exitTransition = {
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

            val context = LocalContext.current


            OnboardingFlow(
                onFinish = {
                    balance, monthlyGoal, preferredCurrency ->
                    scope.launch {
                        try {
                            delay(200)

                            userViewModel.updateUserData(balance, monthlyGoal, preferredCurrency)
                            navController.navigate(Screen.Home.route) {
                                popUpTo(0) { inclusive = true

                                    saveState = true}
                                launchSingleTop = true
                            }
                        } catch (e: Exception) {
                            e.printStackTrace()
                        }
                    }
                }
            )
        }

        composable(
            Screen.Home.route,
            enterTransition = {
                if (initialState.destination.route == "onboarding") {
                    slideInVertically(
                        initialOffsetY = { it },
                        animationSpec = tween(800)
                    ) + fadeIn(animationSpec = tween(1000)) + scaleIn(
                        initialScale = 0.65f,
                        animationSpec = tween(800)
                    )
                } else {
                    null
                }
            }
        ) {
            HomeScreen(modifier, navController, authViewModel, userViewModel, transactionViewModel)
        }
        composable(Screen.Profile.route) { ProfileScreen(navController, authViewModel, userViewModel) }
        composable(Screen.Savings.route) { SavingsScreen(userViewModel) }
        composable(Screen.Transactions.route) { TransactionsScreen(transactionViewModel) }
        composable(Screen.Stats.route) { StatsScreen() }
    }
}