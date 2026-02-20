package com.example.demo.complaintApp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.navigation.compose.rememberNavController
import com.example.demo.complaintApp.RootScaffold
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
      //  lifecycle.addObserver(MyObserver()) // for lifecycle observe


        setContent {


          val navController= rememberNavController()// main app
            CityCareTheme(content = { RootScaffold(navController = navController) })


        }

    }
}



















// Yaha tum UI set karte ho (Compose ya XML)
// setContent { MyApp() }