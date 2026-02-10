package com.example.soul


import android.util.Log
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.FeaturedPlayList
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navigation




//subgraph routes
sealed class AllRoute(val route:String, val icon: ImageVector?=null, val label:String?=null){// subgraph links
    data object Home:AllRoute(route = "home_Screen_subGraph",icon= Icons.Default.Home, "Home")
    data object Profile:AllRoute(route = "profile_Screen_subGraph", Icons.Default.Person, "Profile")
    data object ReportList:AllRoute(route="ReportList_subGraph", Icons.AutoMirrored.Filled.FeaturedPlayList, label = "reports")
    data object AddReport:AllRoute(route = "AddReport_subGraph")
}

sealed class AuthScreens(val route: String){
    data object Login_Screen:AuthScreens(route = "auth_Login")
    data object SignUp_Screen:AuthScreens(route = "auth_Signup")
    data object Auth_Verify:AuthScreens(route = "auth_Verify")
    data object Auth_UserData:AuthScreens(route = "auth_UserData")

}


@Composable
fun NavGraphSetup(navControllerGraph: NavHostController,modifier: Modifier){
    NavHost(navController = navControllerGraph, startDestination ="login/signup",modifier = modifier ) {

        Log.e("splashView2", "its nav run starting se 🙏")


        navigation(route = "login/signup", startDestination = AllGraphScreeens1.Splash.route){

            Log.e("splashView2","run the first ")


                composable(route = AllGraphScreeens1.Splash.route) {
                    val viewSplash:SplashViewModel= hiltViewModel()
/*
 ViewModel must be created INSIDE composable { },
not outside,
because only composable has a valid NavBackStackEntry.
 */
                    splashDemo(navControllerGraph, viewSplash)
                }

                composable(route = AuthScreens.Login_Screen.route) {

                    Log.e("splashView2","run the login composable")
                    val viewLogin:LoginViewModel= hiltViewModel()
                    LoginScreen(navControllerGraph, viewLogin)
                }
                composable(route = AuthScreens.SignUp_Screen.route) {
                    val view: LoginDemoView = hiltViewModel()
                    SignDemo(navControllerGraph, view)


                }
                composable(route = AuthScreens.Auth_Verify.route) {
                    val view: LoginDemoView = hiltViewModel()// do jagah par val view chiye to hum dono jagah par hie bana kar de dege kyu hilt pahle se hie banae or chache mei store loginDemo ka instance dega elsiye dono ke pass hie instance gaya
                    VerifyCheck(view, navControllerGraph)
                }
            composable(route=AuthScreens.Auth_UserData.route){
                val viewUserData:UserDataView= hiltViewModel()
                userDataTaken(viewUserData,navControllerGraph)
            }
            }
        navigation(route = "main_Graph", startDestination = AllRoute.Home.route){
            homeGraph(navControllerGraph = navControllerGraph)
            profileGraph(navProfile = navControllerGraph)
            reportGraph(navHostController = navControllerGraph)
            addReportGraph(navHostController = navControllerGraph)

        }

    }
}