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
import androidx.navigation.NavHostController
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import coil.compose.rememberAsyncImagePainter

//
@Composable
fun SubmitSuccess(navigation:NavHostController){
Box(modifier = Modifier.fillMaxSize().background(color = MaterialTheme.colorScheme.background)) {
    Image(
        painter = rememberAsyncImagePainter(model = R.drawable.image780),
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
        Text("    Home   ")
    }


}
}