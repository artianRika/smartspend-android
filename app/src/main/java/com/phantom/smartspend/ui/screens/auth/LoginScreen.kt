package com.phantom.smartspend.ui.screens.auth

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import com.google.android.gms.common.SignInButton

@Composable
fun LoginScreen(
    onGoogleClick: () -> Unit,
    modifier: Modifier = Modifier,
    //Plug the theme colors
    titleColor: Color = MaterialTheme.colorScheme.primary,
    bodyColor: Color = MaterialTheme.colorScheme.onBackground
) {
    Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background){
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 24.dp)
                .padding(top = 48.dp, bottom = 24.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ){
            Column(
                modifier = Modifier.padding(top = 24.dp),
                horizontalAlignment = Alignment.Start
            ) {
                Text(
                    "Welcome to",
                    color = titleColor,
                    fontSize = 34.sp,
                    fontWeight = FontWeight.Bold,
                    lineHeight = 38.sp
                )
                Text(
                    "Smart Spend",
                    color = titleColor,
                    fontSize = 34.sp,
                    fontWeight = FontWeight.Bold,
                    lineHeight = 38.sp
                )
                Spacer(Modifier.height(16.dp))
                Text(
                    "Enjoy all the features that make it easy for you to manage your finances",
                    color = bodyColor.copy(alpha = 0.8f),
                    style = MaterialTheme.typography.bodyMedium,
                    lineHeight = 18.sp,
                )
            }

            //Actions
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                GoogleSignInButton(
                    onClick = onGoogleClick
                )

            }
        }
    }
}
@Composable
fun GoogleSignInButton(onClick: () -> Unit) {
    AndroidView(
        factory = { context ->
            SignInButton(context).apply {
                setSize(SignInButton.SIZE_WIDE)   // wide format with text
                setColorScheme(SignInButton.COLOR_LIGHT)
                setOnClickListener { onClick() }
            }
        },
        modifier = Modifier
            .fillMaxWidth()
            .height(48.dp)
    )
}
