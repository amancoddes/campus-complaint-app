package com.example.soul

import android.util.Log
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navigation


fun NavGraphBuilder.reportGraph(navHostController: NavHostController){

    navigation(route = AllRoute.ReportList.route, startDestination = AllGraphScreeens1.Report.route){
println(" run report graph ")



        composable(route=AllGraphScreeens1.Report.route){entry ->
            val view:ReportListViewmodel= hiltViewModel(entry)
            Log.e("NAV", "VM attached to -> ${entry.destination.route}")
            AllListReports(view,navHostController)
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