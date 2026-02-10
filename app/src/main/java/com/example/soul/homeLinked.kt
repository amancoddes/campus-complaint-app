package com.example.soul

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavHostController
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun Home(nav:NavHostController){

    Column(modifier = Modifier.fillMaxSize().background(color = Color.Blue.copy(0.6f)), verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally) {

        Text("  Hilt +_ SavedStateHandle + Room + Navigation + MVVM", fontSize = 15.sp)
        Spacer(modifier = Modifier.height(20.dp))
        Button(onClick = {nav.navigate(route = Screens.TextPage.route)}) {
            Icon(imageVector = Icons.AutoMirrored.Filled.ArrowForward, contentDescription = "next")
        }
    }
}