package com.example.demo.complaintApp

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.remember
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navigation


fun NavGraphBuilder.userComplaintsGraph(navHostController: NavHostController,innerPaddingValues: PaddingValues){

    navigation(route = AllRoute.ReportList.route, startDestination = AllGraphScreeens1.Report.route){



        composable(route=AllGraphScreeens1.Report.route){
            val view: UserAllComplaintsScreenViewModel = hiltViewModel()
           UserAllComplaintsScreen(navHostController = navHostController,view, innerPadding = innerPaddingValues)
        }

        composable(
            route = "complaint_detail/{id}"
        ) { backStackEntry ->
            val id = backStackEntry.arguments?.getString("id")!!
            val view:ComplaintDetailViewModel= hiltViewModel()
            ComplaintDetailScreen(id = id,view,navHostController)
        }
    }
}