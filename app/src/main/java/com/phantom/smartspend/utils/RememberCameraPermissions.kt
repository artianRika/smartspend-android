package com.phantom.smartspend.utils

import android.content.pm.PackageManager
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.ContextCompat

@Composable
fun rememberCameraPermission(
    onGranted: () -> Unit
): MutableState<Boolean> {
    val context = LocalContext.current
    var canOpenCamera by remember {
        mutableStateOf(
            ContextCompat.checkSelfPermission(
                context,
                android.Manifest.permission.CAMERA
            ) == PackageManager.PERMISSION_GRANTED
        )
    }

    val launcher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { granted ->
        canOpenCamera = granted
        if (granted) onGranted()
    }

    LaunchedEffect(Unit) {
        if (!canOpenCamera) {
            launcher.launch(android.Manifest.permission.CAMERA)
        } else {
            onGranted()
        }
    }

    return remember { mutableStateOf(canOpenCamera) }
}
