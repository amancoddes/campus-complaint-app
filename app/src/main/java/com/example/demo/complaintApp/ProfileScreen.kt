package com.example.demo.complaintApp

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import com.google.firebase.auth.FirebaseAuth


@Composable
fun ProfileScreen(viewModel: ProfileScreenViewModel,navHostController: NavHostController,paddingValues: PaddingValues){
// splash ka logic use karo future mei
    val auth = FirebaseAuth.getInstance()
    val profileState by viewModel.uiState.collectAsStateWithLifecycle(
        minActiveState = Lifecycle.State.RESUMED
    )

    LaunchedEffect(Unit) {
        if (auth.currentUser == null) {
            navHostController.navigate(AuthScreens.Login_Screen.route){
                popUpTo(0){ inclusive = true }
                launchSingleTop=true
            }

        }
    }

    if (auth.currentUser != null) {
     Box(modifier = Modifier.fillMaxSize())
         {

            when(profileState){
                is CombineProfileFetchState.Loading ->{
                    LoadingShimmer(paddingValues)
                }
                is CombineProfileFetchState.Login -> {
                    LoginMethod(error = "first login ", padding = paddingValues)
                }
                is CombineProfileFetchState.Error -> {
                    ErrorMethod(error = (profileState as CombineProfileFetchState.Error).errorMessage, padding = paddingValues, onClick = {viewModel.fetchProfileAfterLoginAndSignUp()} )
                }
                is CombineProfileFetchState.Success -> {
                    UserProfileCard((profileState as CombineProfileFetchState.Success).data,navHostController, viewModel = viewModel,paddingValues)
                }

                is CombineProfileFetchState.Empty -> {
                    AddMethod(error = "add profile",paddingValues, onLogout = {
                        FirebaseAuth.getInstance().signOut()
                        viewModel.logoutDeleteRoom()

                        navHostController.navigate("login/signup") {
                            popUpTo("main_Graph") { inclusive = true }
                        }
                    })
                }
            }






        }
    }

}


@Composable
fun LoadingShimmer(padding:PaddingValues) {
    Column(
        modifier = Modifier.padding(20.dp)
    ) {
        Box(
            modifier = Modifier
                .height(80.dp)
                .fillMaxWidth().padding(padding)
                .shimmerEffect() // Custom shimmer modifier
        )
        Spacer(Modifier.height(12.dp))
        Box(
            modifier = Modifier
                .height(20.dp)
                .fillMaxWidth(0.5f)
                .shimmerEffect()// extension funcition for increase readeabilty
        )
       // Spacer(modifier =Modifier.height(50.dp))
//        Button(onClick = {
//            navHostController.popBackStack()
//        }) {
//            Text("Home")
//        }
    }
}


fun Modifier.shimmerEffect(): Modifier = composed {
    this.background(Color.Black.copy(alpha = 0.3f))
}


@Composable
fun ErrorMethod(error:String,padding:PaddingValues,onClick: () -> Unit){
    Box(modifier = Modifier.fillMaxSize().padding(padding), contentAlignment = Alignment.Center){

        Text(error)
        Spacer(modifier = Modifier.height(40.dp))
        Button(onClick = onClick){
            Text("try again")
        }

    }
}


@Composable
fun LoginMethod(error:String,padding:PaddingValues){
    Box(modifier = Modifier.fillMaxSize().padding(padding), contentAlignment = Alignment.Center){

        Text(error)
        Spacer(modifier = Modifier.height(40.dp))

    }
}


@Composable
fun AddMethod(error:String,padding:PaddingValues,onLogout:()-> Unit){
    Box(modifier = Modifier.fillMaxSize().padding(padding), contentAlignment = Alignment.Center){

        Text(error)
        Spacer(modifier = Modifier.height(40.dp))
        Button(
            onClick = onLogout,
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFFE53935)
            ),
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp)
        ) {
            Icon(Icons.AutoMirrored.Filled.ExitToApp, null)
            Spacer(modifier = Modifier.width(8.dp))
            Text("Logout")
        }

    }
}
