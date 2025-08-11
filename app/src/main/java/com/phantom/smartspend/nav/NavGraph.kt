package com.phantom.smartspend.nav

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.compose.NavHost
import com.phantom.smartspend.ui.screens.HomeScreen
import com.phantom.smartspend.ui.screens.StatsScreen
import com.phantom.smartspend.ui.screens.PocketScreen
import com.phantom.smartspend.ui.screens.ProfileScreen

@Composable
fun NavGraph(navController: NavHostController, modifier: Modifier = Modifier) {
    NavHost(
        navController = navController,
        startDestination = Screen.Home.route,
        modifier = modifier
    ) {
        composable(Screen.Home.route) { HomeScreen() }
        composable(Screen.Pocket.route) { PocketScreen() }
        composable(Screen.Stats.route) { StatsScreen() }
        composable(Screen.Profile.route) { ProfileScreen() }
    }
}
