package com.example.soul

import android.widget.Spinner
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun loading(){

    Box {
        Text("Loading")

        Spinner.ACCESSIBILITY_DATA_SENSITIVE_AUTO
    }
}
