package com.example.demo.complaintApp
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavHostController

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Home_Screen(navHostController: NavHostController,viewModel:HomeScreenViewModel) {



    Scaffold(
        topBar = {

            TopAppBar(
                title = {
                    Text("complain app")
                },
                actions = {
                    IconButton(onClick = { }) {
                        Icon(imageVector = Icons.Default.Settings, contentDescription = "setting", tint = Color.White)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary, // Background color
                    titleContentColor = MaterialTheme.colorScheme.onPrimary // Text color
                )
            )

        },



        containerColor = MaterialTheme.colorScheme.background
    ) { innerPadding ->

        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            contentAlignment = Alignment.Center
        ) {




        }
    }
}



// pagging code remove






