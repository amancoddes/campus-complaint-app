package com.example.soul

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController

@Composable
fun VerifyCheck(view: LoginDemoView, nav: NavHostController) {

    val state by view.uiState.collectAsState()
    val snackbar = remember { SnackbarHostState() }

//    LaunchedEffect(Unit) {
//        view.snackbar.collect { msg ->
//            snackbar.showSnackbar(msg)
//        }
//    }

    // NAVIGATION
    LaunchedEffect(state) {
        if (state is DataClassLogin.VerifyComplete) {
            nav.navigate(AuthScreens.Auth_UserData.route) {
                popUpTo(AuthScreens.Auth_Verify.route) { inclusive = true }
            }
        }
    }

    Scaffold(snackbarHost = { SnackbarHost(snackbar) }) { padding ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {

            Text("A verification link was sent to your email.")
            Spacer(Modifier.height(20.dp))

            Button(onClick = { view.checkEmailVerified() }) {
                Text("I verified")
            }

            if (state is DataClassLogin.Loading) {
                Spacer(Modifier.height(20.dp))
                CircularProgressIndicator()
            }
        }
    }
}