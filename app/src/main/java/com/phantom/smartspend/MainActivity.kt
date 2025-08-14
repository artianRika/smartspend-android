package com.phantom.smartspend

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import com.phantom.smartspend.data.local.OnboardingPreferences
import com.phantom.smartspend.nav.BottomNavBar
import com.phantom.smartspend.nav.NavGraph
import com.phantom.smartspend.nav.TopBar
import com.phantom.smartspend.ui.components.AddTransactionBottomSheet
import com.phantom.smartspend.ui.onboarding.OnBoardingScreen
import com.phantom.smartspend.ui.theme.SmartSpendTheme
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    @RequiresApi(Build.VERSION_CODES.UPSIDE_DOWN_CAKE)
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            SmartSpendTheme {

                val scope = rememberCoroutineScope()
                var showOnboarding: Boolean? by remember { mutableStateOf(null) }

                LaunchedEffect(Unit) {
                    showOnboarding = !OnboardingPreferences.isOnboardingDone(this@MainActivity)
                }

                if (showOnboarding == true) {
                    OnBoardingScreen(
                        onFinish = {
                            scope.launch {
                                OnboardingPreferences.setOnboardingDone(this@MainActivity, true)
                                showOnboarding = false
                            }
                        }
                    )
                } else {

                    val navController = rememberNavController()
                    var showSheet by remember { mutableStateOf(false) }



                    Scaffold(
                        modifier = Modifier.fillMaxSize(),
                        topBar = {
                            TopBar(navController)
                        },
                        bottomBar = {
                            BottomNavBar(
                                navController = navController,
                                onAddClick = { showSheet = true }
                            )
                        }
                    ) { innerPadding ->
                        NavGraph(
                            navController = navController,
                            modifier = Modifier.padding(innerPadding)
                        )

                        if (showSheet) {
                            AddTransactionBottomSheet(
                                onDismiss = { showSheet = false },
                                onAddClick = { showSheet = false },
                                onScanClick = { showSheet = false }
                            )
                        }

                    }
                }
            }
        }
    }
}


//@Preview(showBackground = true)
//@Composable
//fun GreetingPreview() {
//    SmartSpendTheme {
//        Greeting("Android")
//    }
//}