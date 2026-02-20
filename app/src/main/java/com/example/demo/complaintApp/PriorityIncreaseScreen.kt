package com.example.demo.complaintApp

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.navigation.NavHostController
import coil.compose.rememberAsyncImagePainter

@Composable
fun PriorityIncreaseScreen(navigation: NavHostController){

    Box(modifier = Modifier.fillMaxSize().background(color = MaterialTheme.colorScheme.background)) {
        Image(
            painter = rememberAsyncImagePainter(model = R.drawable.screenshot_2026_02_04_at_11_15_06pm),
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.FillBounds
        )

        Button(onClick = {
            navigation.navigate(AllRoute.Home.route){
                popUpTo(AllRoute.AddReport.route){
                    inclusive=true
                }
                launchSingleTop=true
            }
        }, modifier = Modifier.align(Alignment.BottomCenter)) {
            Text(" Home ")
        }


    }
}