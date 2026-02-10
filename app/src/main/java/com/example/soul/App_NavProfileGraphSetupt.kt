package com.example.soul

import android.util.Log
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navDeepLink
import androidx.navigation.navigation


sealed class ProfileGraphSealedClass (val route:String, val icon: ImageVector, val label:String){
    data object XX:ProfileGraphSealedClass(route = "HomeSetting", icon = Icons.Default.Settings, label = "setting")
}



fun NavGraphBuilder.profileGraph(navProfile:NavHostController){

    navigation(startDestination = AllGraphScreeens1.Profile.route, route = AllRoute.Profile.route){
        Log.e("splashView2", "its nav run starting se ☘️")

        composable(route = AllGraphScreeens1.Profile.route, deepLinks = listOf(
            navDeepLink { uriPattern = "souls://complaint.com/profile" }
        )){

            val view: UserProfileViewModel = hiltViewModel()

            Log.e("splashView2", "its nav run ")

    profileScreen(view,navProfile)

        }
    }
}

