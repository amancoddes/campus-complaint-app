package com.example.demo.complaintApp

import android.provider.Settings
import android.content.Intent
import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.LocationManager
import android.net.Uri
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.navigation.NavHostController
import coil.compose.rememberAsyncImagePainter


@Composable
fun ComplaintPreviewOutdoorScreen(
    navHostController: NavHostController,
    imageUri: Uri?,
    viewModel: ComplaintPreviewScreenViewModel
) {
    val state by viewModel.uiState.collectAsState()

    val snackbarHostState = remember { SnackbarHostState() }

    DisposableEffect(Unit) {
        onDispose {
            viewModel.cancel()
        }
    }
Log.e("preview","preview page")
    LaunchedEffect(Unit) {
        viewModel.snackbarEvent.collect{ msg->
            snackbarHostState.showSnackbar(msg)// its say show snackbar 1.
        }
    }

    LaunchedEffect(state) {
        if (state is ComplaintUiState.Success) {
            navHostController.navigate(AddReportScreens.SubmitSuccess.route) {
                popUpTo(AddReportScreens.Type.route) { inclusive = true }
            }
        }
    }

    Box(modifier = Modifier.fillMaxSize().background(MaterialTheme.colorScheme.onPrimary)) {

        PreviewPageContent(navHostController, imageUri, viewModel)

        if (state is ComplaintUiState.Loading) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color(0x66000000))
                    .clickable(enabled = false) { }
            )
            CircularProgressIndicator(

                modifier = Modifier.align(Alignment.Center)
            )
        }

        SnackbarHost(// its run the commant 2.
            hostState = snackbarHostState,
            modifier = Modifier.align(Alignment.BottomCenter)
        )
    }


}














@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PreviewContent(
    imageUri: Uri?,
    complainTitle: String,
    description: String,
    address: String,
    locationAvailable: Boolean,

    onBack: () -> Unit,
    onTitleChange: (String) -> Unit,
    onDescriptionChange: (String) -> Unit,
    onAddressChange: (String) -> Unit,
    onLocationClick: () -> Unit,
    onSubmit: () -> Unit
) {

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Preview Complaint") },
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
                .verticalScroll(rememberScrollState())
                .padding(16.dp)
        ) {

            //  Image Card
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

            //  Form Card
            Card(
                shape = RoundedCornerShape(20.dp),
                elevation = CardDefaults.cardElevation(6.dp)
            ) {

                Column(modifier = Modifier.padding(16.dp)) {

                    Text(
                        "Complaint Details",
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp
                    )

                    Spacer(Modifier.height(12.dp))

                    TitleDropdownSelector(
                        boxShow = "Choose complaint",
                        modifier = Modifier.fillMaxWidth(),
                        listTitles = listOf(
                            "Garbage not collected",
                            "Water leakage",
                            "Street light not working",
                            "Road pothole",
                            "Drain overflow",
                            "Tree fallen",
                            "Noise complaint"
                        ),
                        complainTitle = complainTitle,
                        onTitleSelected = onTitleChange
                    )

                    Spacer(Modifier.height(12.dp))

                    OutlinedTextField(
                        value = description,
                        onValueChange = onDescriptionChange,
                        label = { Text("Description") },
                        modifier = Modifier.fillMaxWidth(),
                        maxLines = 3
                    )

                    Spacer(Modifier.height(12.dp))

                    OutlinedTextField(
                        value = address,
                        onValueChange = onAddressChange,
                        label = { Text("Address") },
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }

            Spacer(Modifier.height(20.dp))

            // Location Card
            Card(
                shape = RoundedCornerShape(18.dp),
                elevation = CardDefaults.cardElevation(6.dp)
            ) {

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { onLocationClick() }
                        .padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {

                    Icon(
                        Icons.Default.LocationOn,
                        contentDescription = null,
                        tint = if (locationAvailable) Color(0xFF4CAF50) else Color.Red
                    )

                    Spacer(Modifier.width(12.dp))

                    Text(
                        if (locationAvailable) "Location fetched ✔"
                        else "Tap to fetch location",
                        fontWeight = FontWeight.Medium
                    )
                }
            }

            Spacer(Modifier.height(30.dp))

            //  Submit Button
            Button(
                onClick = onSubmit,
                enabled = locationAvailable,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                shape = RoundedCornerShape(14.dp)
            ) {
                Text(
                    if (locationAvailable) "Submit Complaint"
                    else "Complete Required Fields"
                )
            }
        }
    }
}



@Composable
fun PreviewPageContent(
    nav: NavHostController,
    imageUri: Uri?,
    viewModel: ComplaintPreviewScreenViewModel
) {

    val title by viewModel.complainView
    val desc by viewModel.descriptionView
    val address by viewModel.addressView
    val location by viewModel.location.collectAsState()

    DisposableEffect(Unit) {
        onDispose { viewModel.cancel() }
    }

    PreviewContent(
        imageUri = imageUri,
        complainTitle = title,
        description = desc,
        address = address,
        locationAvailable = location != null,

        onBack = { nav.popBackStack() },
        onTitleChange = { viewModel.updateComplain(it) },
        onDescriptionChange = { viewModel.updateDescription(it) },
        onAddressChange = { viewModel.updateAddress(it) },
        onLocationClick = { viewModel.fetchLocation() },
        onSubmit = { viewModel.sendComplain() }
    )
}

@Preview(showBackground = true)
@Composable
fun PreviewScreenPreview() {

    PreviewContent(
        imageUri = null,
        complainTitle = "Garbage not collected",
        description = "Garbage lying on road",
        address = "Meerut, UP",
        locationAvailable = true,

        onBack = {},
        onTitleChange = {},
        onDescriptionChange = {},
        onAddressChange = {},
        onLocationClick = {},
        onSubmit = {}
    )
}


//
//@OptIn(ExperimentalMaterial3Api::class)
//@Composable
//fun PreviewPageContent(
//    navHostController: NavHostController,
//    imageUri: Uri? = null,
//    // ye home ka viewmodel nhi hai preview outside complaint ka hai
//    viewModel:ComplaintPreviewScreenViewModel
//) {
//
//    DisposableEffect(Unit) {
//        onDispose {
//            viewModel.cancel()
//        }
//    }
//
//
//    Scaffold(
//        topBar = {
//            TopAppBar(
//                title = { Text("Preview") },
//                navigationIcon = {
//                    IconButton(onClick = {navHostController.popBackStack()}) {
//                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
//                    }
//                },
//                colors = TopAppBarDefaults.topAppBarColors(
//                    containerColor = MaterialTheme.colorScheme.surface,
//                    titleContentColor = MaterialTheme.colorScheme.onSurface,
//                    navigationIconContentColor = MaterialTheme.colorScheme.onSurface
//                )
//            )
//        }
//    ) { padding ->
//        Surface(
//            color = MaterialTheme.colorScheme.background,
//            modifier = Modifier
//                .fillMaxSize()
//                .padding(padding)
//        ) {
//            Column(
//                modifier = Modifier
//                    .fillMaxSize()
//                    .verticalScroll(rememberScrollState())
//                    .padding(horizontal = 16.dp, vertical = 12.dp)
//            ) {
//                // Top image preview (placeholder — replace with actual rememberAsyncImagePainter)
//                Surface(
//                    modifier = Modifier
//                        .fillMaxWidth()
//                        .height(300.dp),
//                    shape = MaterialTheme.shapes.medium,
//                    tonalElevation = 2.dp
//                ) {
//                    Image(
//                        painter = rememberAsyncImagePainter(model = imageUri),
//                        contentDescription = null,
//                        modifier = Modifier
//                            .padding(horizontal = 16.dp)
//                            .fillMaxWidth()
//                            .height(300.dp),
//                        contentScale = ContentScale.Crop
//                    )
//                }
//
//                Spacer(modifier = Modifier.height(20.dp))
/////
//
//
//                ///
//                //  \title dropdown selector
//
//
//              val x=  listOf(
//                    "Garbage not collected",
//                    "Water leakage",
//                    "Street light not working",
//                    "Road pothole",
//                    "Drain overflow",
//                    "Tree fallen\branch blocking road",
//                    "Noise complaint",
//                )
//                val complainTitle2 by viewModel.complainView
//// not send callback for complainTitle jab viewmodel ke state mei change hoga to wasa bhi re compose kar dega screen ko
//                TitleDropdownSelector(boxShow = "choose complaint",
//                    modifier = Modifier.fillMaxWidth(), listTitles = x, complainTitle = complainTitle2, onTitleSelected = {new ->
//                        println("value change title 26-12-25 code test🥸😃 , $new")
//                        viewModel.updateComplain(new)
//                    }
//                )
//
//
//
//
//                Spacer(modifier = Modifier.height(20.dp))
//
//
//                // Address (simple editable field)
//
//               val description by viewModel.descriptionView
//                OutlinedTextField(
//                    value = description,
//                    onValueChange = { viewModel.updateDescription(it) },
//                    label = {
//                        Text("description")
//                    },
//                    minLines = 1,
//                    maxLines = 2,
//                    modifier = Modifier.fillMaxWidth(),
//                    supportingText = {
//                        Text("opt/*")
//                    }
//                )
//
//
//
//                Spacer(modifier = Modifier.height(20.dp))
//
//
//                val address by viewModel.addressView
//                OutlinedTextField(
//                    value = address,
//                    onValueChange = { viewModel.updateAddress(it) },
//                    label = {
//                        Text("add address")
//                    },
//                    minLines = 1,
//                    maxLines = 2,
//                    modifier = Modifier.fillMaxWidth()
//                )
//
//                Spacer(modifier = Modifier.height(20.dp))
//
//
//
//
//
//
//
//
//
//
///// LOCATION FETCHING
//
//                // Result ApI for show dialog of location gps
//                val context2 = LocalContext.current
//// its notes 69, see down
//
//val context=context2.applicationContext
//                // check gps onn or off but its not check permission its simply check hardware onn or off
//                //	•	isProviderEnabled() = Binder IPC ke through system se poochta hai
//                fun isLocationEnabled(): Boolean {
//                    val lm = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
//                    return lm.isProviderEnabled(LocationManager.GPS_PROVIDER) ||// ye method indirectly 1 level ke baad Binder IPC automatically trigger hota hai
//                            lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
//                }
//
//                fun openLocationSettings() {
//                    context2.startActivity(
//                        Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)// Settings.ACTION_LOCATION_SOURCE_SETTINGS its a shorcut
//                    )
//                }
//                val permissionLauncher= rememberLauncherForActivityResult(// esme context dene ki jarat hie nhi khud esko activity ka context mil jata hai or dialog show karne ke liye activity ka hie context chahiye
//                    contract = ActivityResultContracts.RequestPermission()
//                ){granted->
//
//                    if(granted){
//
//                        if(!isLocationEnabled()){
//                            openLocationSettings()
//                            return@rememberLauncherForActivityResult
//                        }
//                        else{
//                            Log.e("location","from call Resutl api location check and run and call the viewmodel ")
//                            viewModel.fetchLocation()
//                        }
//                    }
//                    else{
//                        Toast.makeText(
//                            context,// ye os show karta hai esliye use the application context
//                            "Location permission is required!",
//                            Toast.LENGTH_SHORT
//                        ).show()
//                    }
//
//                }
//
//
//
//
//
//                Row(verticalAlignment = Alignment.CenterVertically) {
//                    Text(
//                        text = "fetch my Location",
//                        style = MaterialTheme.typography.titleLarge
//                    )
//                    Spacer(modifier = Modifier.width(20.dp))
//
//                    IconButton(onClick = {
//                        val granted = ContextCompat.checkSelfPermission(
//                            context,
//                            Manifest.permission.ACCESS_FINE_LOCATION//Runtime me FINE request karne se COARSE bhi milta hai
//                        ) == PackageManager.PERMISSION_GRANTED//Any Context  sirf read or dialog show karne ke liye chiye activity context lekin
//                        /*
//                        Status read karne me Application context bhi chalega.
//Dialog dikhane ke liye Activity zaroori hai — aur launcher already Activity se linked hai.
//                         */
//
//
//                                if(!granted) {
//                            permissionLauncher.launch(input = Manifest.permission.ACCESS_FINE_LOCATION)
//                            return@IconButton
//                        }
//
//                        if(!isLocationEnabled()){
//                           openLocationSettings()
//                            return@IconButton
//                        }
//                        viewModel.fetchLocation()
//                    }) {
//                        Icon(imageVector = Icons.Default.LocationOn, contentDescription = null, tint = Color.Red)
//                    }
//                }
//                // end of location
//
//
//
//
//
//
//
//
//
//
//                Spacer(modifier = Modifier.height(36.dp))
//val locationCheck by viewModel.location.collectAsState()
//                // Save button placeholder (no onSave logic as requested)
//                Button(enabled = locationCheck != null,
//                    onClick = {
//
//                            if (imageUri != null) {
//                                viewModel.sendComplain()
//                                //imageUri
//                        }
//                        else{
//                            println("image is null")
//                        }
//                    },
//                    modifier = Modifier
//                        .align(Alignment.CenterHorizontally)
//                        .widthIn(min = 160.dp)
//                ) {
//                    Text(if(locationCheck!=null) "submit" else "fill")
//                }
//            }
//        }
//    }
//}