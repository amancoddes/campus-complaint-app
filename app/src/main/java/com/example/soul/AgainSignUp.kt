//package com.example.soul
//
//import android.widget.Toast
//import androidx.compose.foundation.background
//import androidx.compose.foundation.layout.Arrangement
//import androidx.compose.foundation.layout.Box
//import androidx.compose.foundation.layout.Column
//import androidx.compose.foundation.layout.Row
//import androidx.compose.foundation.layout.fillMaxSize
//import androidx.compose.material3.Button
//import androidx.compose.material3.Card
//import androidx.compose.material3.OutlinedTextField
//import androidx.compose.material3.Text
//import androidx.compose.runtime.Composable
//import androidx.compose.runtime.collectAsState
//import androidx.compose.runtime.getValue
//import androidx.compose.ui.Alignment
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.graphics.Brush
//import androidx.compose.ui.graphics.Color
//import androidx.compose.ui.platform.LocalContext
//import androidx.compose.ui.platform.LocalFocusManager
//import androidx.compose.ui.platform.LocalSoftwareKeyboardController
//import androidx.lifecycle.viewmodel.compose.viewModel
//
//@Composable
//fun AgainSignUp(){
//    val view:AgainViewModel= viewModel(factory = AgainFactory())
//    val email by view.email.collectAsState()
//    val password by view.password.collectAsState()
//    val userDetail by view.user.collectAsState()
//    val userLoginDetail by view.userLogin.collectAsState()
//    val authState by view.authState.collectAsState()
//    val focus= LocalFocusManager.current
//    val keyboard= LocalSoftwareKeyboardController.current
//if(authState==null){
//    Column(modifier = Modifier.fillMaxSize().clearSabFocus(x = focus, y = keyboard).background(brush = Brush.verticalGradient(colors = listOf(
//        Color.Blue.copy(0.4f), Color.White))), verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally
//    ){
//
//
//        OutlinedTextField(
//            value = email,
//            onValueChange = {view.emailSet(it)},
//            singleLine = true,
//            label = { Text("enter email") }
//        )
//
//        OutlinedTextField(
//            value = password,
//            onValueChange = {view.passwordSet(it)},
//            singleLine = true,
//            label = { Text("enter password") }
//        )
//
//        Row {
//            val context= LocalContext.current
//            Button(onClick = {view.signUp()}) {  Text("SignUp") }
//            userDetail?.let { result ->
//                result.onSuccess {
//                        its->
//                    if (its != null) {
//                        Toast.makeText(context,its.email,Toast.LENGTH_SHORT).show()
//                    }
//                }.onFailure { e->
//                    Toast.makeText(context,e.message,Toast.LENGTH_SHORT).show()
//                }
//            }
//            Button(onClick = {view.login()}) { Text("Login") }
//            userLoginDetail?.let { result ->
//                result.onSuccess {
//                        its->
//                    if (its != null) {
//                        Toast.makeText(context,"singup complete ${ its.email}", Toast.LENGTH_SHORT).show()
//                    }
//                }.onFailure { e->
//                    Toast.makeText(context,e.message,Toast.LENGTH_SHORT).show()
//                }
//            }
//        }
//    }
//}
//    else{
//        Box(modifier = Modifier.fillMaxSize().background(Color.Blue.copy(0.4f)), contentAlignment = Alignment.Center){
//            Card {
//                Text(" user is Login with ${authState?.email?:"login successful but due to high trafffic email not sotre in data base "}")
//                Button(onClick = {view.logout()}) { Text("Logout") }
//            }
//        }
//    }
//}