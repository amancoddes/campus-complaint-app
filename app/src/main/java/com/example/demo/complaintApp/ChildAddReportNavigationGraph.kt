package com.example.demo.complaintApp

import android.net.Uri
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import androidx.navigation.navigation

sealed class AddReportScreens(val route: String) {
// Type outside
    data object Type:AddReportScreens("type?imageUri={imageUri}")
    data object Preview : AddReportScreens("preview?imageUri={imageUri}")
    data object TypeInside:AddReportScreens("type_inside?imageUri={imageUri}")

   // data object Loading: AddReportScreens("loading")
    data object SubmitSuccess : AddReportScreens("submit_success")
   // data object ErrorScreen:AddReportScreens("error_Screen")

    data object PriorityIncrease: AddReportScreens("priorityIncreaseScreen")
}


fun NavGraphBuilder.addReportGraph(navHostController: NavHostController){
    navigation(route =AllRoute.AddReport.route, startDestination = AddReportScreens.Type.route ){





        composable(
            route=AddReportScreens.Type.route,
            arguments = listOf(
                navArgument("imageUri") {
                    type = NavType.StringType
                    nullable = false
                }
            )
        ){backStackEntry ->

            val uriStr = backStackEntry.arguments?.getString("imageUri")
            val uri = Uri.parse(uriStr)

            chooseType(navHostController, imageUri = uri)
        }



        // outside preview
composable(
    route=AddReportScreens.Preview.route,
    arguments = listOf(
        navArgument("imageUri") {
            type = NavType.StringType
            nullable = false
        }
    )
){backStackEntry ->

    val uriStr = backStackEntry.arguments?.getString("imageUri")
    val uri = Uri.parse(uriStr)
    val view: PreviewScreenViewModelClass= hiltViewModel(backStackEntry)
    PreviewPage(navHostController, imageUri = uri,view)
}


// inside
        composable(
            route=AddReportScreens.TypeInside.route,
            arguments = listOf(
                navArgument("imageUri") {
                    type = NavType.StringType
                    nullable = false
                }
            )
        ){backStackEntry ->

            val uriStr = backStackEntry.arguments?.getString("imageUri")
            val uri = Uri.parse(uriStr)
            val view: PreviewScreenViewModelClass = hiltViewModel(backStackEntry)
            complaintInside(navHostController, imageUri = uri,view)
        }

        composable(route=AddReportScreens.SubmitSuccess.route){
            val view:PreviewScreenViewModelClass= hiltViewModel()
            SubmitSuccess(navHostController,view)
        }


        composable(route=AddReportScreens.PriorityIncrease.route){
            priorityScreen(navigation = navHostController)
        }




}

}