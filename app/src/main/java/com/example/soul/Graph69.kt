//package com.example.soul
//
//import androidx.compose.runtime.Composable
//import androidx.compose.runtime.LaunchedEffect
//import androidx.compose.runtime.collectAsState
//import androidx.compose.runtime.getValue
//import androidx.hilt.navigation.compose.hiltViewModel
//import androidx.navigation.NavHostController
//import androidx.navigation.compose.NavHost
//import androidx.navigation.compose.composable
//import androidx.navigation.compose.currentBackStackEntryAsState
//
//
//@Composable
//fun Graph69(navNew:NavHostController){
//
//   // val view2: SavedStateHandelRoomView = hiltViewModel()
//
//
////    val navBackStackEntry = navNew.currentBackStackEntryAsState()// use make it reactive by use of AsState is make interally aa extension functionwhich give us data when currentBackStateEntry get new NavBackStackEntry
////    val lastRoute= navBackStackEntry.value?.destination?.route
////
////    LaunchedEffect(lastRoute){
////        lastRoute?.let {
////            view2.saveLastScreen(lastRoute)
////        }
////    }// ye kaam ka nhi hai kyuki navcontroller khud hie os ke backgorund kill ke baad savedStateHandle ki tarah apna data store kar leta hai bundle mei
//
//    NavHost(navController = navNew, startDestination =Screens.Logo.route) {
//
//
//        composable(route = Screens.Logo.route){
//            Logo(navController = navNew)
//        }
//
//        composable(route = Screens.Home.route){
//            Home(nav = navNew)
//        }
//
//
//        composable(route = Screens.TextPage.route){
//            val view: SavedStateHandelRoomView = hiltViewModel()
//            SavedStateHandle(navHostController = navNew,view)
//
//        }
//
//        composable(route = Screens.ThirdPage.route){
//            val view:SavedStateHandelRoomView= hiltViewModel()
//            ThirdPageScreen(navHostController = navNew, view = view)
//        }
//    }
//}