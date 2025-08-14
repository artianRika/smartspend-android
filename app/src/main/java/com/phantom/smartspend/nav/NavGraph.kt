package com.phantom.smartspend.nav

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.phantom.smartspend.ui.screens.home.HomeScreen
import com.phantom.smartspend.ui.screens.profile.ProfileScreen
import com.phantom.smartspend.ui.screens.auth.LoginScreen

@RequiresApi(Build.VERSION_CODES.UPSIDE_DOWN_CAKE)
@Composable
fun NavGraph(navController: NavHostController, modifier: Modifier = Modifier) {
    NavHost(
        navController = navController,
        startDestination = Screen.Home.route,
        modifier = modifier
    ) {
        composable(Screen.Home.route, ) { HomeScreen(modifier) }
        composable(Screen.Profile.route) { ProfileScreen(modifier) }
    }
}
