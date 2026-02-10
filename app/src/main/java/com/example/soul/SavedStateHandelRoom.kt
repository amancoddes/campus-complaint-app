//package com.example.soul
//
//import androidx.compose.foundation.background
//import androidx.compose.foundation.layout.Arrangement
//import androidx.compose.foundation.layout.Column
//import androidx.compose.foundation.layout.Row
//import androidx.compose.foundation.layout.Spacer
//import androidx.compose.foundation.layout.fillMaxSize
//import androidx.compose.foundation.layout.padding
//import androidx.compose.foundation.layout.width
//import androidx.compose.material.icons.Icons
//import androidx.compose.material.icons.automirrored.filled.ArrowForward
//import androidx.compose.material.icons.filled.ArrowForward
//import androidx.compose.material.icons.filled.Home
//import androidx.compose.material3.Button
//import androidx.compose.material3.Icon
//import androidx.compose.material3.IconButton
//import androidx.compose.material3.OutlinedTextField
//import androidx.compose.material3.Text
//import androidx.compose.runtime.Composable
//import androidx.compose.runtime.collectAsState
//import androidx.compose.runtime.getValue
//import androidx.compose.ui.Alignment
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.graphics.Color
//import androidx.compose.ui.platform.LocalContext
//import androidx.compose.ui.unit.dp
//import androidx.lifecycle.viewmodel.compose.LocalViewModelStoreOwner
//import androidx.lifecycle.viewmodel.compose.viewModel
//import androidx.savedstate.SavedStateRegistryOwner
//import dagger.hilt.android.lifecycle.HiltViewModel
//import androidx.hilt.navigation.compose.hiltViewModel
//import androidx.navigation.NavHostController
//
//@Composable
//fun SavedStateHandle(navHostController: NavHostController,view: SavedStateHandelRoomView){
//
//
//
//
////
////   val context= LocalContext.current.applicationContext as MyApplication2
////    val owner = LocalViewModelStoreOwner.current!! as SavedStateRegistryOwner
////    val view:SavedStateHandelRoomView= viewModel(factory = Handle(repo = context.repo, owner = owner))
////, we now use Hilt than we have to only write one line insted of three and a manualy factroy class
//
//    val vari by view.loginState.collectAsState()
//
//
//    Column(modifier = Modifier.fillMaxSize().background(Color.White).padding(20.dp), verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally) {
//
//        OutlinedTextField(
//            value = vari.userName,
//            onValueChange = {
//                view.updateUserName(it)
//            },
//            label = {
//                Text("username")
//            }
//        )
//
//        Row {
//            Button(onClick = {
//                view.saveDataINRoom()
//            }) {
//                Text("save")
//            }
//
//            Button(onClick = {view.getAll()}) {
//                Text("get")
//            }
//
//            Spacer(modifier = Modifier.width(10.dp))
//            IconButton(onClick = {navHostController.navigate(Screens.ThirdPage.route)}) {
//                Icon(imageVector = Icons.AutoMirrored.Filled.ArrowForward, contentDescription = "last screen ")
//            }
//
//
//            Spacer(modifier = Modifier.width(10.dp))
//            IconButton(onClick = {navHostController.navigate(Screens.Home.route)}) {
//                Icon(imageVector = Icons.Default.Home, contentDescription = "last screen ")
//            }
//        }
//
//
//
//
//    }
//
//
//}
