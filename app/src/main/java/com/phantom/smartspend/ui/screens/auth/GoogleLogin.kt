package com.phantom.smartspend.ui.screens.auth

import android.os.Build
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.phantom.smartspend.R
import com.phantom.smartspend.R.drawable.google__g__logo
import com.phantom.smartspend.viewmodels.AuthViewModel

@RequiresApi(Build.VERSION_CODES.UPSIDE_DOWN_CAKE)
@Composable
fun LoginScreenGoogle(
    authViewModel: AuthViewModel,
    onGoogleClick: () -> Unit
) {

    val context = LocalContext.current
    val errorMessage by authViewModel.errorMessage.collectAsState()

    errorMessage?.let { msg ->
        LaunchedEffect(msg) {
            Toast.makeText(context, msg, Toast.LENGTH_LONG).show()
            authViewModel.clearError()
        }
    }

    Surface(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 16.dp, bottom = 42.dp),
        color = MaterialTheme.colorScheme.background
    ) {
        val context = LocalContext.current
        var errorMessage by remember { mutableStateOf<String?>(null) }


        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Column(
                modifier = Modifier
                    .systemBarsPadding()
                    .padding(horizontal = 24.dp),
                verticalArrangement = Arrangement.spacedBy(24.dp),
                horizontalAlignment = Alignment.Start
            ) {
                Spacer(Modifier.height(24.dp))

                Text(
                    text = "Welcome to\nSmartSpend",
                    color = MaterialTheme.colorScheme.primary,
                    fontSize = 40.sp,
                    fontWeight = FontWeight.SemiBold,
                    lineHeight = 44.sp
                )

                Text(
                    text = "Enjoy all the features that make it easy for your finances, and receive rich insights.",
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.75f),
                    lineHeight = 22.sp
                )
            }

            OutlinedButton(
                onClick = {
                    onGoogleClick()
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp, vertical = 24.dp)
                    .height(56.dp),
                shape = RoundedCornerShape(14.dp),
                border = ButtonDefaults.outlinedButtonBorder,
                colors = ButtonDefaults.outlinedButtonColors(
                    containerColor = MaterialTheme.colorScheme.surface,
                    contentColor = MaterialTheme.colorScheme.onSurface
                ),
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Image(
                        painter = painterResource(google__g__logo),
                        contentDescription = "Google",
                        modifier = Modifier.size(22.dp)
                    )
                    Spacer(Modifier.width(12.dp))
                    Text(
                        text = "Continue with Gmail",
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Medium)
                    )
                }
            }
        }
    }
}