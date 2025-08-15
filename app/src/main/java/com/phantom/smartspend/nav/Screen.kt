package com.phantom.smartspend.nav

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Paid
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.QueryStats
import androidx.compose.material.icons.outlined.Receipt
import androidx.compose.ui.graphics.vector.ImageVector

sealed class Screen(val route: String, val title: String, val icon: ImageVector) {
    object Home : Screen("home", "Home", Icons.Outlined.Home)
    object Add : Screen("add", "Add", Icons.Filled.AddCircle)
    object Profile : Screen("profile", "Profile", Icons.Outlined.Person)

    object Stats : Screen("stats", "Stats", Icons.Outlined.QueryStats)
    object Savings : Screen("savings", "Savings", Icons.Outlined.Paid)
    object Transactions : Screen("transactions", "Transactions", Icons.Outlined.Receipt)

    companion object {
        val items = listOf(Home, Add, Profile)
    }
}
