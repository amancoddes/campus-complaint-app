package com.example.demo.complaintApp

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.google.firebase.auth.FirebaseAuth
import javax.annotation.meta.When


@Composable
fun profileScreen(viewModel: UserProfileViewModel,navHostController: NavHostController){
// splash ka logic use karo future mei
    val auth = FirebaseAuth.getInstance()
    val profileState by viewModel.uiState.collectAsState()

    LaunchedEffect(Unit) {
        if (auth.currentUser == null) {
            navHostController.navigate(AuthScreens.Login_Screen.route){
                popUpTo(0){ inclusive = true }
                launchSingleTop=true
            }

        }
    }

    if (auth.currentUser != null) {
        Scaffold(topBar = {
            CommonTopAppBar(
                title = "Profile",
                onBackClick = { navHostController.popBackStack() }
            )
        }) {paddingValues ->

            when(profileState){
                is CombineProfileFetchState.Loading ->{
                    LoadingShimmer(paddingValues)
                }
                is CombineProfileFetchState.Login -> {
                    LoginMethod(error = "first login ", padding = paddingValues)
                }
                is CombineProfileFetchState.Error -> {
                    ErrorMethod(error = (profileState as CombineProfileFetchState.Error).errorMessage, padding = paddingValues, navHostController = navHostController, onClick = {viewModel.fetchProfileAfterLoginAndSignUp()} )
                }
                is CombineProfileFetchState.Success -> {
                    UserProfileCard((profileState as CombineProfileFetchState.Success).data,navHostController, viewModel = viewModel,paddingValues)
                }

                is CombineProfileFetchState.Empty -> {
                    AddMethod(error = "add profile",paddingValues)
                }
            }








//            Column(modifier = Modifier
//                .fillMaxSize()
//                .background(color = MaterialTheme.colorScheme.onPrimary).padding(paddingValues)){
//
//                val data by viewModel.user.collectAsState()
//
//                UserDetailUI(data,navHostController,viewModel,paddingValues)
//            }
        }
    }

}

//
//@Composable
//fun UserDetailUI(user: ProfileRoom.ProfileEntity?,navHostController: NavHostController,viewModel: UserProfileViewModel,padding: PaddingValues) {
//
//    when {
//        user == null -> {
//
//            LoadingShimmer(padding)
//        }
//
//        else -> {
//            UserProfileCard(user,navHostController, viewModel = viewModel)
//        }
//    }
//}

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
    this.background(Color.LightGray.copy(alpha = 0.3f))
}


@Composable
fun UserProfileCard(user: ProfileRoom.ProfileEntity,navHostController: NavHostController,viewModel: UserProfileViewModel,padding: PaddingValues) {

    Column(
        modifier = Modifier.padding(padding)
            .padding(20.dp)
            .fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center
    ) {

        Text(
            text = "Hello, ${user.name}",
            style = MaterialTheme.typography.headlineSmall
        )

        Spacer(Modifier.height(10.dp))

        ProfileField(title = "Roll No", value = user.rollNo)
        ProfileField(title = "Phone", value = user.phone)
        ProfileField(title = "Branch", value = user.branch)
    }

    Button(
        onClick = {
            Log.e("VM", "Logout button clicked")
            Log.e("NAV2", "Now route = ${navHostController.currentDestination?.route}")
            FirebaseAuth.getInstance().signOut()
            Log.e("NAV2", "Now route = ${navHostController.currentDestination?.route}")
            Log.e("VM", "After signOut -> ${FirebaseAuth.getInstance().currentUser}")


            navHostController.navigate("login/signup"){
                viewModel.logoutDeleteRoom()
                    popUpTo("main_Graph") {
                        //Navigation ke liye ye sahi hai
                        //But VM destroy ke liye nahi

                        inclusive = true
                    }
                    launchSingleTop = true
                }



        }
    ) {
        Text("Log out")
    }
}

@Composable
fun ProfileField(title: String, value: String) {
    Column(modifier = Modifier.padding(vertical = 6.dp)) {
        Text(
            text = title,
            style = MaterialTheme.typography.labelMedium,
            color = Color.Gray
        )
        Text(
            text = value,
            style = MaterialTheme.typography.bodyLarge
        )
    }
}