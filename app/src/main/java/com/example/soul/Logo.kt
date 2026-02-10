package com.example.soul

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import kotlinx.coroutines.delay

@Composable
fun Logo(navController: NavHostController) {

    // 🖼️ Centered logo or animation
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black),
        contentAlignment = Alignment.Center
    ) {
        Image(
            painter = painterResource(id = R.drawable.ic_launcher_background),
            contentDescription = "Splash Logo",
            contentScale = ContentScale.Crop,
            alignment = Alignment.Center, // 👈 keeps center aligned
            modifier = Modifier
            .fillMaxSize(0.5f) // same width & height (important!)
            .aspectRatio(1f) // 👈 ensures it's a 1:1 square before clipping
            .clip(CircleShape) // 👈 now it will be perfectly circular
            .border(3.dp, Color.White, CircleShape)
            .background(Color.Blue)
        )
    }

    // 🕓 After short delay → navigate to Home
    LaunchedEffect(Unit) {
        delay(1000) // 1.5 sec splash duration
        navController.navigate(Screens.Home.route) {
            popUpTo(Screens.Logo.route) { inclusive = true } // remove Splash from backstack
        }
    }
}