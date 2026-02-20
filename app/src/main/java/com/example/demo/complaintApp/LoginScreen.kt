package com.example.demo.complaintApp

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController


@Composable
fun LoginScreen(
    nav: NavHostController,
    viewModel: LoginViewModel
) {




    val colors = MaterialTheme.colorScheme   // 👈 YEH MISSING THA

    val gradientColors = if (isSystemInDarkTheme()) {
        listOf(
            colors.primary.copy(alpha = 0.15f),
            colors.background
        )
    } else {
        listOf(
            colors.primary.copy(alpha = 0.08f),
            colors.background
        )
    }



    val state by viewModel.uiState.collectAsState()
    Log.e("splashView", " see the login state $state")
    // listen ONLY for events
    LaunchedEffect(Unit) {
        viewModel.events.collect { event ->
            when (event) {// navigation ke liye sharedflow sahi hai
                LoginEvent.GoHome -> {
                    nav.navigate("main_Graph") {
                        popUpTo(AuthScreens.Login_Screen.route) {
                            inclusive = true
                        }
                        launchSingleTop = true
                    }
                }
            }
        }
    }

    Column(

        modifier = Modifier
            .fillMaxSize()
            //.padding(20.dp)
            .background(
                Brush.verticalGradient(gradientColors)
            ),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {


        when (state) {

            LoginState.Loading -> CircularProgressIndicator()

            is LoginState.Error -> {
                Text((state as LoginState.Error).msg, color = Color.Red)
                Spacer(Modifier.height(16.dp))
                LoginForm(viewModel, nav)
            }

            LoginState.Idle -> {
                Log.e("splashView2","login screen Idle")
                LoginForm(viewModel, nav)}
        }
    }
}


@Composable
fun LoginForm(viewModel: LoginViewModel,nav: NavHostController) {

    OutlinedTextField(
        value = viewModel.email,
        onValueChange = viewModel::onEmailChange,
        label = { Text("Email") }
    )

    Spacer(Modifier.height(12.dp))

    OutlinedTextField(
        value = viewModel.password,
        onValueChange = viewModel::onPasswordChange,
        label = { Text("Password") }
    )

    Spacer(Modifier.height(12.dp))

    Button(onClick = { viewModel.login() }) {
        Text("Login")
    }



    Button(
        onClick = {
            nav.navigate(AuthScreens.SignUp_Screen.route) {
                Log.e("splashView","go to signup  ")
                popUpTo(AuthScreens.Login_Screen.route) { inclusive = true }
            }
        }
    ) {
        Text("Go to Signup")
    }
}