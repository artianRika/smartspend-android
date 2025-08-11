package com.phantom.smartspend.nav

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.compose.NavHost
import com.phantom.smartspend.ui.screens.home.HomeScreen
import com.phantom.smartspend.ui.screens.stats.StatsScreen
import com.phantom.smartspend.ui.screens.pocket.PocketScreen
import com.phantom.smartspend.ui.screens.profile.ProfileScreen

@Composable
fun NavGraph(navController: NavHostController, modifier: Modifier = Modifier) {
    NavHost(
        navController = navController,
        startDestination = Screen.Home.route,
        modifier = modifier
    ) {
        composable(Screen.Home.route, ) { HomeScreen(modifier) }
        composable(Screen.Pocket.route) { PocketScreen(modifier) }
        composable(Screen.Stats.route) { StatsScreen(modifier) }
        composable(Screen.Profile.route) { ProfileScreen(modifier) }
    }
}
