package com.example.demo.complaintApp

import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import coil.compose.rememberAsyncImagePainter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun chooseType(
    navHostController: NavHostController,
    imageUri: Uri?,
) {

    Box(modifier = Modifier.fillMaxSize().background(MaterialTheme.colorScheme.onPrimary)) {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text("Complaint type ") },
                    navigationIcon = {
                        IconButton(onClick = { navHostController.popBackStack() }) {
                            Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = MaterialTheme.colorScheme.surface,
                        titleContentColor = MaterialTheme.colorScheme.onSurface,
                        navigationIconContentColor = MaterialTheme.colorScheme.onSurface
                    )
                )
            }
        ) { padding ->
            Surface(
                color = MaterialTheme.colorScheme.background,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .verticalScroll(rememberScrollState())
                        .padding(horizontal = 16.dp, vertical = 12.dp)
                ) {
                    // Top image preview (placeholder — replace with actual rememberAsyncImagePainter)
                    Surface(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(300.dp),
                        shape = MaterialTheme.shapes.medium,
                        tonalElevation = 2.dp
                    ) {
                        Image(
                            painter = rememberAsyncImagePainter(model = imageUri),
                            contentDescription = null,
                            modifier = Modifier
                                .padding(horizontal = 16.dp)
                                .fillMaxWidth()
                                .height(300.dp),
                            contentScale = ContentScale.Crop
                        )
                    }

Spacer(modifier = Modifier.height(20.dp))


                    Button(onClick = { navHostController.navigate("preview?imageUri=$imageUri") }) {
                        Text("outside complaints ")
                    }

                    Spacer(modifier = Modifier.height(20.dp))


                    Button(onClick = {navHostController.navigate("type_inside?imageUri=$imageUri") }) {
                        Text("inside complaints ")
                    }
                    Spacer(modifier = Modifier.height(20.dp))

                }


            }
        }

    }
}