package com.phantom.smartspend

import AppTopBar
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import com.phantom.smartspend.nav.BottomNavBar
import com.phantom.smartspend.nav.NavGraph
import com.phantom.smartspend.ui.components.AddTransactionBottomSheet
import com.phantom.smartspend.ui.theme.SmartSpendTheme

class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            SmartSpendTheme {

                val navController = rememberNavController()
                var showSheet by remember { mutableStateOf(false) }

                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    topBar = {
                        AppTopBar("TopBar")
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


//@Preview(showBackground = true)
//@Composable
//fun GreetingPreview() {
//    SmartSpendTheme {
//        Greeting("Android")
//    }
//}