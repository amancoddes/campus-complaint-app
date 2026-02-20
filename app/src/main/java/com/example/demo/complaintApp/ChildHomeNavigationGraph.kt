package com.example.demo.complaintApp

import android.util.Log
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navigation


sealed class HomeGraphSealedClass (val route:String,val icon:ImageVector,val label:String){
    data object HomeSetting:HomeGraphSealedClass(route = "HomeSetting", icon = Icons.Default.Settings, label = "setting")
}

fun NavGraphBuilder.homeGraph(navControllerGraph: NavHostController) {

    navigation(
        startDestination = AllGraphScreeens1.Home.route,
        route = AllRoute.Home.route
    ) {
        composable(route = AllGraphScreeens1.Home.route) {
            val view: Homeviewmodel = hiltViewModel()
            Home_Screen(navHostController = navControllerGraph, viewModel = view)
        }

    }


}


