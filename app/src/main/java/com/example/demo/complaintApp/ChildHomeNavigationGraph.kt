package com.example.demo.complaintApp

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navigation


sealed class HomeGraphSealedClass (val route:String,val icon:ImageVector,val label:String){
    data object HomeSetting:HomeGraphSealedClass(route = "HomeSetting", icon = Icons.Default.Settings, label = "setting")
}

fun NavGraphBuilder.homeGraph(navControllerGraph: NavHostController,innerPadding: PaddingValues) {

    navigation(
        startDestination = AllGraphScreeens1.Home.route,
        route = AllRoute.Home.route
    ) {
        composable(route = AllGraphScreeens1.Home.route) {
            val parentEntry = remember(navControllerGraph) {// soul reuse of nav.get() but there remember is opt because parent composable not recompose
                navControllerGraph.getBackStackEntry("main_Graph")
            }

            val view: HomeScreenViewModel = hiltViewModel(parentEntry)

            Home_Screen(navHostController = navControllerGraph, viewModel = view, innerPadding = innerPadding)
        }

    }


}


