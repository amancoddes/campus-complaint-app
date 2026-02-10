package com.example.soul

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.sp

@Composable
fun MainScreen67() {
    var showCamera by remember { mutableStateOf(true) }

    Column {
        Button(onClick = { showCamera = !showCamera }) {
            Text(if (showCamera) "Close Camera" else "Open Camera")
        }

        if (showCamera) {
            Disposable()
        }
    }
}

@Composable
fun Disposable() {
    Box(
        modifier = Modifier.fillMaxSize().background(Color.Black),
        contentAlignment = Alignment.Center
    ) {
        Text("hey", color = Color.White, fontSize = 40.sp)
    }

    DisposableEffect(Unit) {
        Log.d("observe", "📷 Camera start")

        onDispose {
            Log.d("observe", "❌ Camera closed")
        }
    }
}