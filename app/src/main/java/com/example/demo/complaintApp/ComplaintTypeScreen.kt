package com.example.demo.complaintApp

import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import coil.compose.rememberAsyncImagePainter


@Composable
fun chooseType(
    nav: NavHostController,
    imageUri: Uri?
) {

    ChooseTypeContent(
        imageUri = imageUri,
        onBack = { nav.popBackStack() },
        onOutdoorClick = {
            nav.navigate("preview?imageUri=$imageUri")
        },
        onIndoorClick = {
            nav.navigate("type_inside?imageUri=$imageUri")
        }
    )
}




@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChooseTypeContent(
    imageUri: Uri?,
    onBack: () -> Unit,
    onOutdoorClick: () -> Unit,
    onIndoorClick: () -> Unit
) {

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Select Complaint Type") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, null)
                    }
                }
            )
        }
    ) { padding ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .background(
                    Brush.verticalGradient(
                        listOf(
                            Color(0xFF5C6BC0),
                            Color(0xFF3949AB),
                            Color(0xFF1A237E)
                        )
                    )
                )
                .padding(16.dp)
        ) {

            //  Image Preview
            Card(
                shape = RoundedCornerShape(20.dp),
                elevation = CardDefaults.cardElevation(8.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(220.dp)
            ) {
                Image(
                    painter = rememberAsyncImagePainter(imageUri),
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )
            }

            Spacer(Modifier.height(20.dp))

            Text(
                text = "Where is the issue?",
                color = Color.White,
                fontSize = 18.sp,
                fontWeight = FontWeight.SemiBold
            )

            Spacer(Modifier.height(16.dp))

            //  Outdoor Card
            TypeCard(
                title = "Outdoor Complaint",
                desc = "Road, street, public area issues",
                icon = Icons.Default.LocationOn,
                onClick = onOutdoorClick
            )

            Spacer(Modifier.height(12.dp))

            //  Indoor Card
            TypeCard(
                title = "Indoor Complaint",
                desc = "Room, building, internal issues",
                icon = Icons.Default.Home,
                onClick = onIndoorClick
            )
        }
    }
}

@Composable
fun TypeCard(
    title: String,
    desc: String,
    icon: ImageVector,
    onClick: () -> Unit
) {

    Card(
        onClick = onClick,
        shape = RoundedCornerShape(18.dp),
        elevation = CardDefaults.cardElevation(6.dp),
        modifier = Modifier.fillMaxWidth()
    ) {

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {

            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = Color(0xFF5C6BC0),
                modifier = Modifier.size(32.dp)
            )

            Spacer(Modifier.width(16.dp))

            Column {
                Text(title, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                Text(desc, fontSize = 13.sp, color = Color.Gray)
            }
        }
    }
}



@Preview(showBackground = true)
@Composable
fun ChooseTypePreview() {

    ChooseTypeContent(
        imageUri = null, //
        onBack = {},
        onOutdoorClick = {},
        onIndoorClick = {}
    )
}


























//
//@OptIn(ExperimentalMaterial3Api::class)
//@Composable
//fun chooseType(
//    navHostController: NavHostController,
//    imageUri: Uri?,
//) {
//
//    Box(modifier = Modifier.fillMaxSize().background(MaterialTheme.colorScheme.onPrimary)) {
//        Scaffold(
//            topBar = {
//                TopAppBar(
//                    title = { Text("Complaint type ") },
//                    navigationIcon = {
//                        IconButton(onClick = { navHostController.popBackStack() }) {
//                            Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
//                        }
//                    },
//                    colors = TopAppBarDefaults.topAppBarColors(
//                        containerColor = MaterialTheme.colorScheme.surface,
//                        titleContentColor = MaterialTheme.colorScheme.onSurface,
//                        navigationIconContentColor = MaterialTheme.colorScheme.onSurface
//                    )
//                )
//            }
//        ) { padding ->
//            Surface(
//                color = MaterialTheme.colorScheme.background,
//                modifier = Modifier
//                    .fillMaxSize()
//                    .padding(padding)
//            ) {
//                Column(
//                    modifier = Modifier
//                        .fillMaxSize()
//                        .verticalScroll(rememberScrollState())
//                        .padding(horizontal = 16.dp, vertical = 12.dp)
//                ) {
//                    // Top image preview (placeholder — replace with actual rememberAsyncImagePainter)
//                    Surface(
//                        modifier = Modifier
//                            .fillMaxWidth()
//                            .height(300.dp),
//                        shape = MaterialTheme.shapes.medium,
//                        tonalElevation = 2.dp
//                    ) {
//                        Image(
//                            painter = rememberAsyncImagePainter(model = imageUri),
//                            contentDescription = null,
//                            modifier = Modifier
//                                .padding(horizontal = 16.dp)
//                                .fillMaxWidth()
//                                .height(300.dp),
//                            contentScale = ContentScale.Crop
//                        )
//                    }
//
//Spacer(modifier = Modifier.height(20.dp))
//
//
//                    Button(onClick = { navHostController.navigate("preview?imageUri=$imageUri") }) {
//                        Text("outside complaints ")
//                    }
//
//                    Spacer(modifier = Modifier.height(20.dp))
//
//
//                    Button(onClick = {navHostController.navigate("type_inside?imageUri=$imageUri") }) {
//                        Text("inside complaints ")
//                    }
//                    Spacer(modifier = Modifier.height(20.dp))
//
//                }
//
//
//            }
//        }
//
//    }
//}