package com.example.soul

sealed class Screens(val route:String){
    data object Logo:Screens(route = "splash")
    data object Home:Screens(route = "home")
    data object TextPage:Screens(route = "textPage")
    data object ThirdPage:Screens(route = "thirdPage")
}