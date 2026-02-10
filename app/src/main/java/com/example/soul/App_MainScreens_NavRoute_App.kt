package com.example.soul


sealed class AllGraphScreeens1(val route:String) {


    data object Splash : AllGraphScreeens1(route = "auth_Splash_Screen")
    data object Home : AllGraphScreeens1(route = "Home_Screens")
    data object Report : AllGraphScreeens1(route = "Reports_ListScreen")
    data object Profile : AllGraphScreeens1(route = "Profile_Screens")
    //data object AddReport : AllGraphScreeens1(route = "Add_Report")
}

