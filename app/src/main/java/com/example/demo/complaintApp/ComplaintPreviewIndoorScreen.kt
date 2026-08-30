package com.example.demo.complaintApp

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.LocationManager
import android.net.Uri
import android.os.Build
import android.provider.Settings
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
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

//ComplaintPreviewOutdoorScreen
@Composable
fun ComplaintPreviewIndoorScreen(
    navHostController: NavHostController,
    imageUri: Uri?,
    viewModel: ComplaintPreviewScreenViewModel) {
    val state by viewModel.uiState.collectAsState()

    val snackbarHostState = remember { SnackbarHostState() }

    DisposableEffect(Unit) {
        onDispose {
            viewModel.cancel()
        }
    }

    LaunchedEffect(Unit) {
        viewModel.snackbarEvent.collect { msg ->
            snackbarHostState.showSnackbar(msg)// its say show snackbar 1.
        }
    }

    LaunchedEffect(state) {
        if (state is ComplaintUiState.Success) {
            navHostController.navigate(AddReportScreens.SubmitSuccess.route) {
                popUpTo(AddReportScreens.Type.route) { inclusive = true }
            }
        }
        if (state is ComplaintUiState.PriorityIncrease) {
            navHostController.navigate(AddReportScreens.PriorityIncrease.route) {
                popUpTo(AddReportScreens.Type.route) { inclusive = true }
            }
        }
    }

    Box(modifier = Modifier.fillMaxSize().background(MaterialTheme.colorScheme.onPrimary)) {

        PreviewPageContent2(navHostController, imageUri, viewModel)

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


@Composable
fun PreviewPageContent2(
    nav: NavHostController,
    imageUri: Uri?,
    viewModel: ComplaintPreviewScreenViewModel
) {

    val building by viewModel.building
    val complaint by viewModel.complainView
    val floor by viewModel.floor
    val location by viewModel.location.collectAsState()

    DisposableEffect(Unit) {
        onDispose { viewModel.cancel() }
    }

    PreviewContent2(
        imageUri = imageUri,
        building = building,
        complaint = complaint,
        floor = floor,
        locationAvailable = location != null,

        onBack = { nav.popBackStack() },
        onBuildingChange = { viewModel.updateBuilding(it) },
        onComplaintChange = { viewModel.updateComplain(it) },
        onFloorChange = { viewModel.updateFloor(it) },
        onLocationClick = { viewModel.fetchLocation(inside = true) },
        onSubmit = {
            if (imageUri != null) {
                viewModel.setImageUri(imageUri)
            }
            viewModel.insideSendComplain()
        }
    )
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PreviewContent2(
    imageUri: Uri?,
    building: String,
    complaint: String,
    floor: String,
    locationAvailable: Boolean,

    onBack: () -> Unit,
    onBuildingChange: (String) -> Unit,
    onComplaintChange: (String) -> Unit,
    onFloorChange: (String) -> Unit,
    onLocationClick: () -> Unit,
    onSubmit: () -> Unit
) {

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Department Complaint") },
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
                    .height(200.dp)
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
                        "Location Details",
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp
                    )

                    Spacer(Modifier.height(12.dp))

                    TitleDropdownSelector(
                        boxShow = "Choose Building",
                        modifier = Modifier.fillMaxWidth(),
                        listTitles = listOf("CSIT", "ME", "EE"),
                        complainTitle = building,
                        onTitleSelected = onBuildingChange
                    )

                    Spacer(Modifier.height(12.dp))

                    TitleDropdownSelector(
                        boxShow = "Choose Complaint",
                        modifier = Modifier.fillMaxWidth(),
                        listTitles = listOf(
                            "Furniture damage",
                            "Electrical issue",
                            "Cleaning complaint"
                        ),
                        complainTitle = complaint,
                        onTitleSelected = onComplaintChange
                    )

                    Spacer(Modifier.height(12.dp))

                    TitleDropdownSelector(
                        boxShow = "Choose Floor",
                        modifier = Modifier.fillMaxWidth(),
                        listTitles = listOf("Ground", "First", "Second"),
                        complainTitle = floor,
                        onTitleSelected = onFloorChange
                    )
                }
            }

            Spacer(Modifier.height(20.dp))

            // Result ApI for show dialog of location gps
            val context2 = LocalContext.current
// its notes 69, see down

            val context = context2.applicationContext

            // check gps onn or off but its not check permission its simply check hardware onn or off
            //	•	isProviderEnabled() = Binder IPC ke through system se poochta hai
            @RequiresApi(Build.VERSION_CODES.P)
            fun isLocationEnabled(): Boolean {
                val lm =
                    context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
                return lm.isLocationEnabled }

            fun openLocationSettings() {
                context2.startActivity(
                    Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)// Settings.ACTION_LOCATION_SOURCE_SETTINGS its a shorcut
                )
            }

            val permissionLauncher =
                rememberLauncherForActivityResult(// esme context dene ki jarat hie nhi khud esko activity ka context mil jata hai or dialog show karne ke liye activity ka hie context chahiye
                    contract = ActivityResultContracts.RequestPermission()
                ) { granted ->

                    if (granted) {

                        if (!isLocationEnabled()) {
                            openLocationSettings()
                            return@rememberLauncherForActivityResult
                        } else {
                            Log.e(
                                "location",
                                "from call Resutl api location check and run and call the viewmodel "
                            )
                            onLocationClick()
                        }
                    } else {
                        Toast.makeText(
                            context,// ye os show karta hai esliye use the application context
                            "Location permission is required!",
                            Toast.LENGTH_SHORT
                        ).show()
                    }

                }

















            //  Location Card
            Card(
                shape = RoundedCornerShape(18.dp),
                elevation = CardDefaults.cardElevation(6.dp)
            ) {

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            val granted = ContextCompat.checkSelfPermission(
                                context,
                                Manifest.permission.ACCESS_FINE_LOCATION//Runtime me FINE request karne se COARSE bhi milta hai
                            ) == PackageManager.PERMISSION_GRANTED//Any Context  sirf read or dialog show karne ke liye chiye activity context lekin
                            /*
                        Status read karne me Application context bhi chalega.
Dialog dikhane ke liye Activity zaroori hai — aur launcher already Activity se linked hai.
                         */


                            if (!granted) {
                                permissionLauncher.launch(input = Manifest.permission.ACCESS_FINE_LOCATION)
                                return@clickable
                            }

                            if (!isLocationEnabled()) {
                                openLocationSettings()
                                return@clickable
                            }
                            onLocationClick()
                         }
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






@Preview(showBackground = true)
@Composable
fun PreviewIndoorScreenPreview() {

    PreviewContent2(
        imageUri = null,
        building = "CSIT",
        complaint = "Furniture damage",
        floor = "First",
        locationAvailable = true,

        onBack = {},
        onBuildingChange = {},
        onComplaintChange = {},
        onFloorChange = {},
        onLocationClick = {},
        onSubmit = {}
    )
}

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun PreviewPageContent34(
        navHostController: NavHostController,
        imageUri: Uri? = null,
        // ye home ka viewmodel nhi hai preview outside complaint ka hai
        viewModel: ComplaintPreviewScreenViewModel
    ) {

        DisposableEffect(Unit) {
            onDispose {
                viewModel.cancel()
            }
        }

        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text(" department complaints  ") },
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


                    val x = listOf(
                        "CSIT",
                        "ME",
                        "EE"
                    )
                    val complainTitle2 by viewModel.building
// not send callback for complainTitle jab viewmodel ke state mei change hoga to wasa bhi re compose kar dega screen ko
                    TitleDropdownSelector(boxShow = "choose building",
                        modifier = Modifier.fillMaxWidth(),
                        listTitles = x,
                        complainTitle = complainTitle2,
                        onTitleSelected = { new ->
                            println("value change title 26-12-25 code test🥸😃 , $new")
                            viewModel.updateBuilding(new)
                        }
                    )

                    Spacer(modifier = Modifier.height(20.dp))


                    val xx = listOf(
                        "furniture damage",
                        "electrical instrument damage",
                        "cleaning complaint",
                    )
                    val complainTitle2x by viewModel.complainView
// not send callback for complainTitle jab viewmodel ke state mei change hoga to wasa bhi re compose kar dega screen ko
                    TitleDropdownSelector(boxShow = "choose complaint",
                        modifier = Modifier.fillMaxWidth(),
                        listTitles = xx,
                        complainTitle = complainTitle2x,
                        onTitleSelected = { new ->
                            println("value change title 26-12-25 code test🥸😃 , $new")
                            viewModel.updateComplain(new)
                        }
                    )

                    Spacer(modifier = Modifier.height(20.dp))

                    val floors = listOf(
                        "ground",
                        "First",
                        "Second"
                    )
                    val floorsType by viewModel.floor
// not send callback for complainTitle jab viewmodel ke state mei change hoga to wasa bhi re compose kar dega screen ko
                    TitleDropdownSelector(boxShow = "choose floor",
                        modifier = Modifier.fillMaxWidth(),
                        listTitles = floors,
                        complainTitle = floorsType,
                        onTitleSelected = { new ->
                            println("value change title 26-12-25 code test🥸😃 , $new")
                            viewModel.updateFloor(new)
                        }
                    )

                    Spacer(modifier = Modifier.height(20.dp))

/// LOCATION FETCHING


                    // Result ApI for show dialog of location gps
                    val context2 = LocalContext.current
// its notes 69, see down

                    val context = context2.applicationContext

                    // check gps onn or off but its not check permission its simply check hardware onn or off
                    //	•	isProviderEnabled() = Binder IPC ke through system se poochta hai
                    fun isLocationEnabled(): Boolean {
                        val lm =
                            context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
                        return lm.isProviderEnabled(LocationManager.GPS_PROVIDER) ||// ye method indirectly 1 level ke baad Binder IPC automatically trigger hota hai
                                lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
                    }

                    fun openLocationSettings() {
                        context2.startActivity(
                            Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)// Settings.ACTION_LOCATION_SOURCE_SETTINGS its a shorcut
                        )
                    }

                    val permissionLauncher =
                        rememberLauncherForActivityResult(// esme context dene ki jarat hie nhi khud esko activity ka context mil jata hai or dialog show karne ke liye activity ka hie context chahiye
                            contract = ActivityResultContracts.RequestPermission()
                        ) { granted ->

                            if (granted) {

                                if (!isLocationEnabled()) {
                                    openLocationSettings()
                                    return@rememberLauncherForActivityResult
                                } else {
                                    Log.e(
                                        "location",
                                        "from call Resutl api location check and run and call the viewmodel "
                                    )
                                    viewModel.fetchLocation(inside = true)
                                }
                            } else {
                                Toast.makeText(
                                    context,// ye os show karta hai esliye use the application context
                                    "Location permission is required!",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }

                        }





                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = "fetch my Location",
                            style = MaterialTheme.typography.titleLarge
                        )
                        Spacer(modifier = Modifier.width(20.dp))

                        IconButton(onClick = {
                            val granted = ContextCompat.checkSelfPermission(
                                context,
                                Manifest.permission.ACCESS_FINE_LOCATION//Runtime me FINE request karne se COARSE bhi milta hai
                            ) == PackageManager.PERMISSION_GRANTED//Any Context  sirf read or dialog show karne ke liye chiye activity context lekin
                            /*
                        Status read karne me Application context bhi chalega.
Dialog dikhane ke liye Activity zaroori hai — aur launcher already Activity se linked hai.
                         */


                            if (!granted) {
                                permissionLauncher.launch(input = Manifest.permission.ACCESS_FINE_LOCATION)
                                return@IconButton
                            }

                            if (!isLocationEnabled()) {
                                openLocationSettings()
                                return@IconButton
                            }
                            viewModel.fetchLocation(inside = true)
                        }) {
                            Icon(
                                imageVector = Icons.Default.LocationOn,
                                contentDescription = null,
                                tint = Color.Red
                            )
                        }
                    }
                    // end of location


                    Spacer(modifier = Modifier.height(36.dp))
                    val locationCheck by viewModel.location.collectAsState()
                    // Save button placeholder (no onSave logic as requested)
                    Button(
                        enabled = locationCheck != null,
                        onClick = {

                            if (imageUri != null) {
                                viewModel.insideSendComplain()
                                //imageUri
                            } else {
                                println("image is null")
                            }
                        },
                        modifier = Modifier
                            .align(Alignment.CenterHorizontally)
                            .widthIn(min = 160.dp)
                    ) {
                        Text(if (locationCheck != null) "submit" else "fill")
                    }


                }


            }
        }


    }