package com.phantom.smartspend.ui.screens.home

import android.os.Build
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.phantom.smartspend.nav.Screen
import com.phantom.smartspend.ui.components.BalanceCard
import com.phantom.smartspend.ui.components.LastTransactions
import com.phantom.smartspend.viewmodels.AuthViewModel
import org.koin.compose.viewmodel.koinViewModel

@RequiresApi(Build.VERSION_CODES.UPSIDE_DOWN_CAKE)
@Composable
fun HomeScreen(
    modifier: Modifier,
    navController: NavController
) {

    val authViewModel: AuthViewModel = koinViewModel()


    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {
        Column() {
            BalanceCard()

            LastTransactions()

//                    navController.navigate(Screen.Savings.route)
//                    navController.navigate(Screen.Transactions.route)

            GoogleSignInButton(authViewModel)
        }
    }
}

@RequiresApi(Build.VERSION_CODES.UPSIDE_DOWN_CAKE)
@Composable
fun GoogleSignInButton(viewModel: AuthViewModel) {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    var isLoading by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    Button(
        onClick = {
            isLoading = true
            errorMessage = null
            viewModel.signInWithGoogleNative { result ->
                isLoading = false
                if (result.isFailure) {
                    errorMessage = result.exceptionOrNull()?.localizedMessage ?: "Unknown error"
                } else {
                    Toast.makeText(context, "Signed in..", Toast.LENGTH_LONG).show()
                }
            }
        },
        modifier = Modifier.padding(top = 16.dp),
        enabled = !isLoading
    ) {
        if (isLoading) {
            CircularProgressIndicator(modifier = Modifier.size(20.dp))
        } else {
            Text("Sign in with Google")
        }
    }

    errorMessage?.let {
        Text(text = it, color = Color.Red)
    }
}