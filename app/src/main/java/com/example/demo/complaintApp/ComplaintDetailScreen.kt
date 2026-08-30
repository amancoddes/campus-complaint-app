package com.example.demo.complaintApp

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.navigation.NavHostController
import coil.compose.AsyncImage


@Composable
fun ComplaintDetailScreen(
    id: String,
    viewModel: ComplaintDetailViewModel,
    navHostController: NavHostController
) {

    val snackBarHost = remember {
        SnackbarHostState()
    }
    LaunchedEffect(Unit) {
        viewModel.snackBarEvent.collect{
            snackBarHost.showSnackbar(it)// its add message in queue give signal to UI and its change the state
        }
    }

    val complaint by viewModel.complaint.collectAsState()
    val loading by viewModel.loading.collectAsState()

    LaunchedEffect(id) {
        viewModel.roomLoad(id)
    }
    Scaffold(
        snackbarHost = { SnackbarHost(snackBarHost) },// its observe the state of snackBarHost state , its show the snack bar on Ui
        topBar = {
        CommonTopAppBar(
            title = "complaint",
            onBackClick = {navHostController.popBackStack() }
        )
    }) { padding ->
        when {
            loading -> CustomLoadingScreen(padding = padding)

           complaint != null -> ComplaintDetailScreen(item = complaint!!, onClick = {

               viewModel.load(id)},padding
           )
//DetailBody(complaint!!, padding)2
            else -> Text("Complaint not found")
        }
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

@Composable
fun ComplaintDetailContent(
    item: ComplaintDataRoom.ComplaintEntity,
    onActionClick: () -> Unit,
    buttonText: String,
    padding: PaddingValues
) {
    var selectedImage by remember {
        mutableStateOf<String?>(null)
    }
    val isResolved = item.status == "RESOLVED"

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .background(MaterialTheme.colorScheme.background)
            .padding(padding).padding(vertical = 10.dp, horizontal = 3.dp)
    ) {

        //  Image Card
        Card(
            shape = RoundedCornerShape(20.dp),
            elevation = CardDefaults.cardElevation(8.dp)
        ) {
            Box {

                AsyncImage(
                    model = item.url,
                    contentDescription = null,
                    placeholder = painterResource(R.drawable.ic_launcher_foreground),
                    error = painterResource(R.drawable.imagedefault),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(180.dp)
                        .clip(RoundedCornerShape(16.dp))
                        .clickable {
                            selectedImage = item.url
                        },
                    contentScale = ContentScale.Crop
                )

Spacer(Modifier.height(10.dp))



                // Status Badge
                Box(
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(10.dp)
                        .background(
                            if (isResolved) Color(0xFF4CAF50) else Color(0xFFE53935),
                            RoundedCornerShape(20.dp)
                        )
                ) {
                    Text(
                        text = if (isResolved) "Resolved" else "Pending",
                        color = Color.White,
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
                        fontSize = 12.sp
                    )
                }
            }
        }

        Spacer(Modifier.height(16.dp))


        AsyncImage(
            model = item.resolvedImageUrl,
            contentDescription = null,
            placeholder = painterResource(R.drawable.ic_launcher_foreground),
            error = painterResource(R.drawable.imagedefault),
            modifier = Modifier
                .fillMaxWidth()
                .height(180.dp)
                .clip(RoundedCornerShape(16.dp))
                .clickable {
                    selectedImage = item.resolvedImageUrl
                },
            contentScale = ContentScale.Crop
        )


        if (selectedImage != null) {

            Dialog(
                onDismissRequest = {
                    selectedImage = null
                }
            ) {

                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color.Black)
                ) {

                    AsyncImage(
                        model = selectedImage,
                        contentDescription = null,
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Fit
                    )

                    IconButton(
                        onClick = {
                            selectedImage = null
                        },
                        modifier = Modifier.align(
                            Alignment.TopEnd
                        )
                    ) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = null,
                            tint = Color.White
                        )
                    }
                }
            }
        }

        Spacer(Modifier.height(16.dp))

        //  Complaint Info
        Card(
            shape = RoundedCornerShape(18.dp),
            elevation = CardDefaults.cardElevation(6.dp)
        ) {
            Column(modifier = Modifier.padding(16.dp).fillMaxWidth()) {

                Text("Complaint", fontWeight = FontWeight.Bold, fontSize = 16.sp)
                Text(item.complain, fontSize = 15.sp)

                Spacer(Modifier.height(12.dp))

                if (item.description.isNotBlank()) {
                    Text("Description", fontWeight = FontWeight.Bold)
                    Text(item.description)
                    Spacer(Modifier.height(12.dp))
                }

                Text("Address", fontWeight = FontWeight.Bold)
                Text(item.address)

                Spacer(Modifier.height(12.dp))

                Text(
                    text = "⏱ ${formatTimestamp(item.timestamp.toLong())}",
                    fontSize = 12.sp,
                    color = Color.Gray
                )
            }
        }

        Spacer(Modifier.height(20.dp))


        Button(
            onClick = onActionClick,
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp),
            shape = RoundedCornerShape(14.dp)
        ) {
            Text(buttonText)
        }
    }
}


@Composable
fun ComplaintDetailScreen(
    item: ComplaintDataRoom.ComplaintEntity,
    onClick: () -> Unit,
    padding: PaddingValues
) {

    ComplaintDetailContent(
        item = item,
        onActionClick = onClick,
        buttonText = "fetch again",
        padding
    )
}


@Preview(showBackground = true)
@Composable
fun ComplaintDetailPreview() {

    val fakeItem = ComplaintDataRoom.ComplaintEntity(
        id = "1",
        complain = "Garbage not collected",
        description = "Garbage lying on the road for 3 days Garbage lying on the road for 3 days Garbage lying on the road for 3 days Garbage lying on the road for 3 days",
        address = "Meerut, UP",
        status = "PENDING",
        timestamp = System.currentTimeMillis(),
        url = "",
        resolvedImageUrl = ""
    )

    ComplaintDetailContent(
        item = fakeItem,
        onActionClick = {},
        buttonText = "fetch again",
        padding = PaddingValues(20.dp)
    )
}