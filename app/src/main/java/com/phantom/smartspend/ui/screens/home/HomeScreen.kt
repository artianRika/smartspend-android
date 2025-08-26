package com.phantom.smartspend.ui.screens.home

import android.os.Build
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.phantom.smartspend.nav.Screen
import com.phantom.smartspend.ui.components.BalanceCard
import com.phantom.smartspend.ui.components.LastTransactions
import com.phantom.smartspend.ui.components.SavingsCard
import com.phantom.smartspend.viewmodels.AuthViewModel
import com.phantom.smartspend.viewmodels.TransactionViewModel
import org.koin.compose.viewmodel.koinViewModel

@RequiresApi(Build.VERSION_CODES.UPSIDE_DOWN_CAKE)
@Composable
fun HomeScreen(
    modifier: Modifier,
    navController: NavController,
    authViewModel: AuthViewModel,
    transactionViewModel: TransactionViewModel
) {

    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(scrollState),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {

        BalanceCard()
        LastTransactions(navController, transactionViewModel)
        Spacer(modifier = Modifier.height(16.dp))
        SavingsCard(
            true,
            onShowViewMoreClick = { navController.navigate(Screen.Savings.route) }
        )
        Column(modifier = Modifier.fillMaxWidth()) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("Balance Over Time")
                TextButton(
                    onClick = { navController.navigate(Screen.Stats.route) },
                ) {
                    Text("View More", color = MaterialTheme.colorScheme.primary, fontSize = 12.sp)
                }
            }
        }


        GoogleSignInButton(authViewModel)

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