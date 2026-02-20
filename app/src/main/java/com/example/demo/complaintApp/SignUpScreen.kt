package com.example.demo.complaintApp

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController


// LISTEN SNACKBAR EVENTS
//    LaunchedEffect(Unit) {
//        viewModel.snackbar.collect { msg ->
//            snackbarHost.showSnackbar(msg)
//        }
//    }
@Composable
fun SignUpScreen(nav: NavHostController, viewModel: SignUpScreenViewModel) {

    val state by viewModel.uiState.collectAsState()
    val snackbarHost = remember { SnackbarHostState() }


    val colors = MaterialTheme.colorScheme

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


    // NAVIGATION ON STATE CHANGE
    LaunchedEffect(state) {
        when (state) {
            is DataClassLogin.VerifyEmailSent -> {
                nav.navigate(AuthScreens.Auth_Verify.route) {
                    popUpTo(AuthScreens.SignUp_Screen.route) { inclusive = true }
                }
            }
            else -> Unit
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHost) }
    ) { padding ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .background(
                    Brush.verticalGradient(gradientColors)
                ),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            when (state) {
                is DataClassLogin.Loading ->
                    CircularProgressIndicator()

                is DataClassLogin.Error -> {
                    Text((state as DataClassLogin.Error).e, color = Color.Red)
                    Spacer(Modifier.height(20.dp))
                    SignupForm(viewModel,nav)
                }

                is DataClassLogin.Idle ->
                    SignupForm(viewModel,nav)

                else -> Unit
            }
        }
    }
}

@Composable
fun SignupForm(viewModel: SignUpScreenViewModel,nav: NavHostController) {

    Column(horizontalAlignment = Alignment.CenterHorizontally) {

        OutlinedTextField(
            value = viewModel.email.value,
            onValueChange = { viewModel.loginInput(it) },
            label = { Text("Email") }
        )

        Spacer(Modifier.height(16.dp))

        OutlinedTextField(
            value = viewModel.password.value,
            onValueChange = { viewModel.passwordInput(it) },
            label = { Text("Password") }
        )

        Spacer(Modifier.height(16.dp))

        Button(onClick = { viewModel.signUp() }) {
            Text("Sign Up")
        }

        Button(onClick = {
            nav.navigate(AuthScreens.Login_Screen.route) {
                Log.e("splashView","go to login  ")

                popUpTo(AuthScreens.SignUp_Screen.route) { inclusive = true }
            }
        }) {
            Text("login")
        }
    }
}