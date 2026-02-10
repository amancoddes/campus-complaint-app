package com.example.soul

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController


@Composable
fun SettingScreen(navHostController: NavHostController){
    Box(modifier = Modifier.fillMaxSize()) {
        Text("setting")
    }
}
