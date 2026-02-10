//package com.example.soul
//
//import androidx.compose.runtime.Composable
//import androidx.compose.runtime.LaunchedEffect
//import androidx.compose.runtime.collectAsState
//import androidx.compose.runtime.getValue
//import androidx.fragment.app.FragmentManager.BackStackEntry
//import androidx.hilt.navigation.compose.hiltViewModel
//import androidx.navigation.NavController
//import androidx.navigation.NavHost
//import androidx.navigation.NavHostController
//import androidx.navigation.NavType
//import androidx.navigation.compose.NavHost
//import androidx.navigation.compose.composable
//import androidx.navigation.compose.currentBackStackEntryAsState
//import androidx.navigation.navArgument
//import com.google.common.graph.Graph
//
//sealed class Graph1(val route:String){
//
//    data object Home:Graph1("home")
//    data object Msg:Graph1("msg/{id}"){
//        fun fun1(id:Int): String= "msg/$id"
//    }
//}
//
//
//@Composable
//fun Graph(navGraph2: NavHostController) {
//    val viewModel: VieworStateHandel = hiltViewModel()
//
//
//
//    val navBackStackEntry by navGraph2.currentBackStackEntryAsState()//currentBackStackEntry sirf last element (topmost screen) ko return karta hai —
//   // yaani jo screen abhi user ke saamne visible hai, usi ka NavBackStackEntry object.
//// its return NacBackStatckEntry of last screen
//    val currentRoute = navBackStackEntry?.destination?.route
//
//    LaunchedEffect(currentRoute) {
//        currentRoute?.let { route ->
//            println("hey 🥳😚 -> $route")
//            viewModel.storeScreen(route)
//        }
//    }
//
//
//    val lastRoute = viewModel.stateScreen.collectAsState()
//
//    NavHost(navController = navGraph2, startDestination = lastRoute.value.route) {
//        composable(Graph1.Home.route) {
//
//            HomeScreen(navGraph2, viewModel)
//
//        }
//
//        composable(
//            route = Graph1.Msg.route,
//            arguments = listOf(navArgument("id") { type = NavType.IntType })
//        ) {//navArgument("id"){}  ye func bas rule hai safety ke liye ki kya parameter ka tuype sahe hai ya nhi , esko bina bhi coede run karega
//                backStackEntry ->
//            val id = backStackEntry.arguments?.getInt("id")
//            if (id != null) {
//                Msg(navGraph2, id)
//            }
//        }
//    }
//}
//
