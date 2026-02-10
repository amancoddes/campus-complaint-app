//package com.example.soul
//
//import androidx.compose.runtime.Composable
//import androidx.hilt.navigation.compose.hiltViewModel
//import androidx.navigation.NavHostController
//import androidx.navigation.compose.NavHost
//import androidx.navigation.compose.composable
//import androidx.navigation.compose.rememberNavController
//import dagger.hilt.android.lifecycle.HiltViewModel
//
//
//@Composable
//fun AppNavHost(
//    navController: NavHostController
//) {
//
//    val view: LoginDemoView = hiltViewModel()
//val viewLogin:LoginViewModel= hiltViewModel()
//    val viewSplash:SplashViewModel= hiltViewModel()
//    NavHost(
//        navController = navController,
//        startDestination = "splash"
//    ) {
//
//       //////////////////////////////
//        composable(route = "splash"){
//
//            splashDemo(navController,viewSplash)
//        }
//
//        composable(route = "login"){
//            LoginScreen(navController, viewLogin)
//        }
//        composable(route="signup"){
//         SignDemo(navController,view)
//
//
//
//        }
//        composable(route="verify"){
//            VerifyCheck(view,navController)
//        }
//        composable(route="complete"){
//            completeverify()
//        }
//
//    }
//}