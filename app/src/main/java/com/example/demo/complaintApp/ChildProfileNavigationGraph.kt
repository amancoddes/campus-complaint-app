//@file:JvmName("ChildUserComplaintsGraphKt")

package com.example.demo.complaintApp

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.runtime.remember
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



fun NavGraphBuilder.profileGraph(navProfile:NavHostController,innerPaddingValues: PaddingValues){

    navigation(startDestination = AllGraphScreeens1.Profile.route, route = AllRoute.Profile.route){

        composable(route = AllGraphScreeens1.Profile.route, deepLinks = listOf(
            navDeepLink { uriPattern = "souls://complaint.com/profile" }
        )){
            val parentEntry = remember(navProfile) {
                navProfile.getBackStackEntry("main_Graph")
            }

            val view: ProfileScreenViewModel= hiltViewModel(parentEntry)


    ProfileScreen(view,navProfile, paddingValues = innerPaddingValues)

        }
    }
}

