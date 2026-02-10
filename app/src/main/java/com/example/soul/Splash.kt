//package com.example.soul
//
//import android.util.Log
//import androidx.compose.foundation.Image
//import androidx.compose.foundation.background
//import androidx.compose.foundation.border
//import androidx.compose.foundation.layout.Box
//import androidx.compose.foundation.layout.aspectRatio
//import androidx.compose.foundation.layout.fillMaxSize
//import androidx.compose.foundation.layout.fillMaxWidth
//import androidx.compose.foundation.shape.CircleShape
//import androidx.compose.material3.Icon
//import androidx.compose.material3.MaterialTheme
//import androidx.compose.material3.NavigationBar
//import androidx.compose.material3.NavigationBarItem
//import androidx.compose.material3.Text
//import androidx.compose.runtime.Composable
//import androidx.compose.runtime.LaunchedEffect
//import androidx.compose.runtime.getValue
//import androidx.compose.runtime.withFrameNanos
//import androidx.compose.ui.Alignment
//import androidx.navigation.NavController
//import androidx.navigation.NavHostController
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.draw.clip
//import androidx.compose.ui.draw.scale
//import androidx.compose.ui.graphics.Color
//import androidx.compose.ui.layout.ContentScale
//import androidx.compose.ui.res.painterResource
//import androidx.compose.ui.unit.dp
//import androidx.navigation.NavDestination.Companion.hierarchy
//import androidx.navigation.NavGraph.Companion.findStartDestination
//import androidx.navigation.compose.currentBackStackEntryAsState
//import kotlinx.coroutines.delay
//
//
//@Composable
//fun Splash_Screen(navController: NavHostController){
//
//
//    Box(modifier = Modifier.fillMaxSize().background(color = MaterialTheme.colorScheme.primary), contentAlignment = Alignment.Center){
//        Image(painter = painterResource(id = R.drawable.finallogoformhome), contentDescription = null, contentScale = ContentScale.Fit, modifier = Modifier
//            .fillMaxSize(0.5f) // same width & height (important!)
//            .aspectRatio(1f) // 👈 ensures it's a 1:1 square before clipping
//            .clip(CircleShape) // 👈 now it will be perfectly circular
//            .border(3.dp, Color.White, CircleShape)
//            .scale(1.3f) )
//    }
//
//    LaunchedEffect(true) {
//        withFrameNanos { }
//        delay(2000)
//        Log.d("SplashNav", "Current = ${navController.currentDestination?.route}")
//        Log.d("SplashNav", "Going to = ${AllRoute.Home.route}")
//
//        if (navController.currentDestination?.route == AllGraphScreeens1.Splash.route) {
//            navController.navigate("main_Graph") {
//                popUpTo(AllGraphScreeens1.Splash.route) { inclusive = true }
//
//            }
//        }
//    }
//
//}
//
//
//
