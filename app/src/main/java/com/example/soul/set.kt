//package com.example.soul
//
//import androidx.compose.foundation.background
//import androidx.compose.foundation.layout.Box
//import androidx.compose.foundation.layout.Column
//import androidx.compose.foundation.layout.fillMaxSize
//import androidx.compose.material3.Button
//import androidx.compose.material3.OutlinedTextField
//import androidx.compose.material3.Text
//import androidx.compose.material3.TextField
//import androidx.compose.runtime.Composable
//import androidx.compose.runtime.collectAsState
//import androidx.compose.runtime.getValue
//import androidx.navigation.NavHostController
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.graphics.Color
//import androidx.hilt.navigation.compose.hiltViewModel
//import androidx.lifecycle.viewmodel.compose.viewModel
//
//@Composable
//fun Msg(nav:NavHostController,id:Int){
//    val view:VieworStateHandel= hiltViewModel()
//    val value2 by view.stateScreen.collectAsState()
//    Column(modifier = Modifier.fillMaxSize().background(Color.Black.copy(alpha = 0.4f))){
//        Text(" : $id")
//
//        OutlinedTextField(value = value2.value, onValueChange = {view.valueSet(it)} , label = { Text("enter") })
//
//        Button(onClick = {nav.navigate(Graph1.Home.route)
//        view.storeScreen(Graph1.Home.route)}) {
//            Text("Home")
//        }
//    }
//}
