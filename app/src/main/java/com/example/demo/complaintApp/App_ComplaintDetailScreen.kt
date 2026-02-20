package com.example.demo.complaintApp

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import coil.compose.rememberAsyncImagePainter

@Composable
fun ComplaintDetailScreen(
    id: String,
    viewModel: ComplaintDetailViewModel,
    navHostController: NavHostController
) {
    val complaint by viewModel.complaint.collectAsState()
    val loading by viewModel.loading.collectAsState()

    LaunchedEffect(id) {
        viewModel.load(id)
    }
    Scaffold(topBar = {
        CommonTopAppBar(
            title = "complaint",
            onBackClick = {navHostController.popBackStack() }
        )
    }) { padding ->
        when {
            loading -> CustomLoadingScreen(padding = padding)

            complaint != null -> ComplaintCard(item = complaint!!, onClick = { navHostController.popBackStack()}, message = "back", padding = padding)
//DetailBody(complaint!!, padding)
            else -> Text("Complaint not found")
        }
    }

}

@Composable
fun DetailBody(item: ComplaintDataRoom.ComplaintEntity,padding: PaddingValues) {

    Column(
        modifier = Modifier
            .padding(padding)
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.primaryContainer, RoundedCornerShape(8.dp))
            .padding(12.dp)
    ) {

        Text("Complaint Details", style = MaterialTheme.typography.titleLarge)

        Spacer(Modifier.height(10.dp))

        Image(
            painter = rememberAsyncImagePainter(R.drawable.imagedefault),
            contentDescription = null,
            modifier = Modifier
                .fillMaxWidth()
                .height(250.dp),
            contentScale = ContentScale.Crop
        )

        Spacer(Modifier.height(12.dp))

        Text("Type: ${item.complain}")
        Text("Description: ${item.description}")
        Text("Address: ${item.address}")
        Text("Status: ${item.status}")
        Text("Date: ${formatTimestamp(item.timestamp.toLong())}")
    }
}


@Composable
fun CustomLoadingScreen(message: String = "Please wait…",padding: PaddingValues) {

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black.copy(alpha = 0.35f)),
        contentAlignment = Alignment.Center
    ) {

        Column(
            modifier = Modifier
                .background(MaterialTheme.colorScheme.surface, RoundedCornerShape(20.dp))
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            CircularProgressIndicator()

            Spacer(Modifier.height(16.dp))

            Text(
                text = message,
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurface
            )
        }
    }
}