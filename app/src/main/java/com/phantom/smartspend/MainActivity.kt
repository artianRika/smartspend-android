package com.phantom.smartspend

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.phantom.smartspend.nav.BottomNavBar
import com.phantom.smartspend.nav.NavGraph
import com.phantom.smartspend.nav.Screen
import com.phantom.smartspend.nav.TopBar
import com.phantom.smartspend.ui.components.AddTransactionBottomSheet
import com.phantom.smartspend.ui.theme.SmartSpendTheme
import com.phantom.smartspend.viewmodels.AuthViewModel
import com.phantom.smartspend.viewmodels.UserViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : ComponentActivity() {
    private val authViewModel: AuthViewModel by viewModel()
    private val userViewModel: UserViewModel by viewModel()

    @RequiresApi(Build.VERSION_CODES.UPSIDE_DOWN_CAKE)
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            SmartSpendTheme {
                val isAuth by authViewModel.isAuthenticated.collectAsState()

                LaunchedEffect(Unit) {
                    authViewModel.checkAuthStatus()
                }

                val navController = rememberNavController()
                var startDestination by remember { mutableStateOf<String?>(null) }
                var showSheet by remember { mutableStateOf(false) }

                val navBackStackEntry by navController.currentBackStackEntryAsState()
                val currentRoute = navBackStackEntry?.destination?.route

                val shouldHideBars = when {
//                    currentRoute == null -> true
                    currentRoute == "login" -> true
                    currentRoute == "onboarding" -> true
                    currentRoute?.startsWith("welcome/") == true -> true
                    else -> false
                }

                LaunchedEffect(isAuth) {
                    startDestination = if (isAuth) {
                        Screen.Home.route
                    } else {
                        "login"
                    }
                }

                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    topBar = {
                        if (!shouldHideBars) {
                            TopBar(navController)
                        }
                    },
                    bottomBar = {
                        if (!shouldHideBars) {
                            BottomNavBar(navController = navController, onAddClick = { showSheet = true })
                        }
                    }
                ) { innerPadding ->

                    AnimatedVisibility(
                        visible = startDestination == null,
                        exit = fadeOut(animationSpec = tween(300))
                    ) {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = androidx.compose.ui.Alignment.Center
                        ) {
                            androidx.compose.material3.CircularProgressIndicator()
                        }
                    }

                    AnimatedVisibility(
                        visible = startDestination != null,
                        enter = fadeIn(animationSpec = tween(300))
                    ) {
                        NavGraph(
                            navController = navController,
                            startDestination = startDestination ?: "login",
                            modifier = if (shouldHideBars) {
                                Modifier.fillMaxSize()
                            } else {
                                Modifier.padding(innerPadding)
                            },
                            authViewModel,
                            userViewModel
                        )
                    }

                    if (showSheet) {
                        AddTransactionBottomSheet(
                            onDismiss = { showSheet = false },
                            onAddTransaction = {   }
                        )
                    }
                }
            }
        }
    }
}