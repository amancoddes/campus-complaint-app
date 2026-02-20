package com.example.demo.complaintApp

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.demo.complaintApp.AllGraphScreeens1
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(nav: NavHostController, viewModel: SplashScreenViewModel) {




    Box(modifier = Modifier.fillMaxSize().background(color = MaterialTheme.colorScheme.primary), contentAlignment = Alignment.Center){
        Image(painter = painterResource(id = R.drawable.finallogoformhome), contentDescription = null, contentScale = ContentScale.Fit, modifier = Modifier
            .fillMaxSize(0.5f) // same width & height (important!)
            .aspectRatio(1f) //  ensures it's a 1:1 square before clipping
            .clip(CircleShape) //  now it will be perfectly circular
            .border(3.dp, Color.White, CircleShape)
            .scale(1.3f) )
    }
    Log.e("splashView","see the before collect state")
    val dest = viewModel.startDestination.collectAsState()
    Log.e("splashView","after the collectstate ${dest.value}")
    LaunchedEffect(dest.value) {
        Log.e("splashView","Launched effect")
        delay(3000)
        if (dest.value != null) {
            nav.navigate(dest.value!!) {
                popUpTo(AllGraphScreeens1.Splash.route) { inclusive = true }
            }

        }
    }

    // splash UI

}