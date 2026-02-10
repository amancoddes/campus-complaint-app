//package com.example.soul
//
//import androidx.compose.foundation.layout.Column
//import androidx.compose.material3.OutlinedTextField
//import androidx.compose.material3.Text
//import androidx.compose.runtime.Composable
//import androidx.compose.runtime.collectAsState
//import androidx.compose.runtime.getValue
//import androidx.lifecycle.viewmodel.compose.viewModel
//
//
//@Composable
//fun DataAppKill() {
//    val view: DataAppKillView = viewModel()
//
//    val state by view.uiState.collectAsState(initial = Login())
//    Column {
//        OutlinedTextField(
//            value = state.userName,
//            onValueChange = { view.nameUpdate(it) },
//            label = {
//                Text("enter the name")
//            }
//        )
//
//        OutlinedTextField(
//            value = state.password,
//            onValueChange = { view.passwordUpdate(it) },
//            label = {
//                Text("enter password")
//            }
//        )
//    }
//
//}