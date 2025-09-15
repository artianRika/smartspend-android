package com.phantom.smartspend.ui.screens.profile

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForwardIos
import androidx.compose.material.icons.filled.ArrowForwardIos
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.outlined.CurrencyExchange
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SegmentedButtonDefaults.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import coil.compose.rememberAsyncImagePainter
import com.patrykandpatrick.vico.core.cartesian.marker.ColumnCartesianLayerMarkerTarget
import com.phantom.smartspend.ui.components.SettingItemDropdown
import com.phantom.smartspend.viewmodels.AuthViewModel
import com.phantom.smartspend.viewmodels.UserViewModel
import kotlinx.coroutines.launch

@Composable
fun ProfileScreen(navController: NavHostController, authViewModel: AuthViewModel, userViewModel: UserViewModel) {

    val navigateToLogin by authViewModel.navigateToLogin.collectAsState()
    val userData by userViewModel.userData.collectAsState()
    val scope = rememberCoroutineScope()
    val context = LocalContext.current
    LaunchedEffect(navigateToLogin) {
        if (navigateToLogin) {
            navController.navigate("login") {
                popUpTo(0) {
                    inclusive = true
                    saveState = true
                }
//                launchSingleTop = true
            }
        }
        authViewModel.resetNavigateToLogin()
    }

    var showLogoutDialog by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState()),
    verticalArrangement = Arrangement.Top
    ) {
        // Profile section
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 8.dp, bottom = 16.dp),
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

            Text(
                "${userData?.firstName} ${userData?.lastName}",
                Modifier.padding(start = 16.dp),
                style = MaterialTheme.typography.titleMedium
            )
        }

        SettingItemDropdown(
            item = "Currency",
            icon = Icons.Default.Settings,
            value = userData?.preferredCurrency ?: "MKD",
            options = listOf("MKD", "EUR", "USD"),
            onSelect = { code ->
                        scope.launch{
                            userViewModel.updatePreferredCurrency(code)
                        }

                //TODO: vm::updatePreferredCurrency(code)
            }
        )
        Spacer(modifier = Modifier.height(16.dp))
        SettingItemAction(
            item = "Support",
            icon = Icons.Filled.Email,
            onClick = {
                val intent = Intent(Intent.ACTION_SENDTO).apply {
                    data = Uri.parse("mailto:metiseni27@gmail.com")
                    putExtra(Intent.EXTRA_SUBJECT, "SmartSpend Support")
                    putExtra(Intent.EXTRA_TEXT, "Hello SmartSpend Team,\n\n")
                }
                context.startActivity(intent)
            }
        )
        FAQSection()

        Spacer(Modifier.weight(1f))
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
@Composable
fun FAQSection(){
    val faqs = listOf(
        "What is SmartSpend?" to "SmartSpend helps you track expenses and gain financial insights.",
        "How do I change my currency?" to "Go to Profile -> Currency and select your preferred currency.",
        "Is my data secure?" to "Yes. Tokens are encrypted with Android Keystore, and no sensitive info is stored in plain text.",
        "How do I contact support?" to "Go to Profile -> Support and email us directly.",
        "What platforms are supported?" to "SmartSpend is available on Android, iOS.",
        "How often are exchange rates updated?" to "Rates are refreshed multiple times a day to stay accurate.",
        "Does SmartSpend charge fees?" to "No fees SmartSpend is an internship project from the interns at Shortcut Balkans."
    )
    Column(Modifier.fillMaxWidth().padding(top = 24.dp)) {
        Text("FAQ", style = MaterialTheme.typography.titleMedium)
        Spacer(Modifier.height(8.dp))
        faqs.forEach {(question, answers) ->
            var expanded by remember {mutableStateOf(false)}
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp)
                    .clickable {expanded = !expanded}
            ) {
                Column(Modifier.padding(12.dp)){
                    Text(question, style = MaterialTheme.typography.bodyLarge, fontWeight = FontWeight.Bold)
                    if(expanded) {
                        Spacer(Modifier.height(4.dp))
                        Text(answers, style = MaterialTheme.typography.bodyMedium)
                    }
                }

            }
        }
    }
}

@Composable
fun SettingItemAction(
    item: String,
    icon: ImageVector,
    onClick: () -> Unit,
    trailing: @Composable (() -> Unit)? = {
        Icon(
            imageVector = Icons.Filled.ArrowForwardIos,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.primary
        )
    }
) {
    Column {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { onClick() }
                .padding(vertical = 8.dp, horizontal = 6.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(imageVector = icon, contentDescription = null)
                Spacer(Modifier.width(16.dp))
                Text(text = item, style = MaterialTheme.typography.bodyLarge)
            }
            if (trailing != null) {
                Row(verticalAlignment = Alignment.CenterVertically) { trailing() }
            }
        }
        // Divider — same look as your dropdown item
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 8.dp)
                .height(1.dp)
                .background(Color.Gray)
        )
    }
}