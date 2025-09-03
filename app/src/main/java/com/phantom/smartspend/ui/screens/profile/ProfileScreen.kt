package com.phantom.smartspend.ui.screens.profile

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.CurrencyExchange
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import coil.compose.rememberAsyncImagePainter
import com.phantom.smartspend.ui.components.SettingItem
import com.phantom.smartspend.viewmodels.AuthViewModel
import com.phantom.smartspend.viewmodels.UserViewModel

@Composable
fun ProfileScreen(navController: NavHostController, authViewModel: AuthViewModel, userViewModel: UserViewModel) {

    val navigateToWelcome by authViewModel.navigateToWelcome.collectAsState()
    val userData by userViewModel.userData.collectAsState()

    LaunchedEffect(navigateToWelcome) {
        if (navigateToWelcome) {
            navController.navigate("login") {
                popUpTo("login") { inclusive = true }
            }
        }
        authViewModel.resetNavigateToWelcome()
    }

    var showLogoutDialog by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.SpaceBetween
    ) {

        Column(
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp, bottom = 48.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Start
            ) {
                Image(
                    painter = rememberAsyncImagePainter(userData?.avatarUrl),
                    contentDescription = "Profile Picture",
                    contentScale = ContentScale.Fit,
                    modifier = Modifier
                        .size(70.dp)
                        .clip(CircleShape)
                )

                Text("${userData?.firstName} ${userData?.lastName}", Modifier.padding(start = 16.dp))
            }

            SettingItem("Currency", Icons.Outlined.CurrencyExchange)
        }

        Button(
            onClick = { showLogoutDialog = true },
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp, vertical = 12.dp)
                .height(56.dp)
                .border(
                    width = 1.dp,
                    color = Color.Red,
                    shape = RoundedCornerShape(16.dp)
                ),
            shape = MaterialTheme.shapes.large,
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.Transparent,
                contentColor = Color.Red
            ),
        ) {
            Text(
                text = "Logout",
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Medium),
                textAlign = TextAlign.Center
            )
        }
    }

    if (showLogoutDialog) {
        AlertDialog(
            onDismissRequest = { showLogoutDialog = false },
            title = { Text("Are you sure you want to logout?") },
            confirmButton = {
                TextButton(onClick = {
                    showLogoutDialog = false
                    authViewModel.logout()
                }) { Text("Logout", color = Color.Red) }
            },
            dismissButton = {
                TextButton(
                    onClick = {
                        showLogoutDialog = false
                    }
                ) { Text("Cancel", color = Color.Gray) }
            }
        )
    }
}