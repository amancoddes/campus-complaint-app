package com.example.demo.complaintApp
import android.Manifest
import android.content.ContentValues
import android.content.Context
import android.content.pm.PackageManager
import android.net.Uri
import android.provider.MediaStore
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FabPosition
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.demo.complaintApp.AllRoute
import com.example.demo.complaintApp.NavGraphSetup


// first run
@Composable
fun RootScaffold(navController: NavHostController) {

    Scaffold(

        bottomBar = {
            if (shouldShowBottomBar(navController)) {
                BottomAppBar {
                    BottomBar(navController)
                }
            }
        },

        floatingActionButton = {
            if (shouldShowFAP(navController)) {
                MyFab(navController,modifier = Modifier.offset(y = 10.dp))
            }
        },

        floatingActionButtonPosition = FabPosition.Center   // ✅ allowed
    ) { innerPadding ->

        NavGraphSetup(// all screen open in it
            navControllerGraph = navController,
            modifier = Modifier.padding(innerPadding)
        )
    }
}

// function decide which screen bottom bar show
@Composable
fun shouldShowBottomBar(navController: NavHostController): Boolean {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route ?: return false

    val isAddReportFlow = Regex("capture|preview|location_confirm|submit_success|type|type_inside|priorityIncreaseScreen")
        .containsMatchIn(currentRoute)

    val isAuthFlow = currentRoute.startsWith("auth_")// see that '_'
    val isComplaintDetail = currentRoute.startsWith("complaint_detail")

    return !isAuthFlow && !isAddReportFlow && !isComplaintDetail
}
@Composable
fun shouldShowFAP(navController: NavHostController): Boolean {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route ?: return false
    Log.e("FAB_ROUTE", "Current route = $currentRoute")
    return currentRoute == "Home_Screens"
}






@Composable
fun MyFab(navController: NavHostController,modifier: Modifier = Modifier) {
    val context = LocalContext.current
    var hasPermission by remember { mutableStateOf(false) }
    // create and remember photo URI
    val imageUri = remember {
        createImageUri(context)
    }


// CAMERA LAUNCHER
    val cameraLauncher = rememberLauncherForActivityResult(// its open the camera
        contract = ActivityResultContracts.TakePicture()  ) { success ->
        if (success) {
            val encodedUri = Uri.encode(imageUri.toString())
            navController.navigate("type?imageUri=$encodedUri")
        } else {
            Toast.makeText(context, "cancelled ", Toast.LENGTH_SHORT).show()

        }
    }
// CHECK PERMISSION
    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { granted ->
        hasPermission = granted// igonre it its use when you want ux increase
        if (granted) {

            if (imageUri != null) {
                cameraLauncher.launch(imageUri)
            }
            else{
                Toast.makeText(context, "storage not available", Toast.LENGTH_SHORT).show()
            }
        } else {
            Toast.makeText(context, "Camera permission is required!", Toast.LENGTH_SHORT).show()
        }
    }




    // FAB
    FloatingActionButton(
        onClick = {

 val granted = ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.CAMERA
            ) == PackageManager.PERMISSION_GRANTED// PERMISSION_GRANTED <- its a variable

            if (granted) {
                Log.e("hey","it run granted ")
                if (imageUri != null) {// first image uri
                    cameraLauncher.launch(imageUri)
                }else{
                    Toast.makeText(context, "storage not available", Toast.LENGTH_SHORT).show()
                }

            } else {
               permissionLauncher.launch(input = Manifest.permission.CAMERA)
            }
        },
        modifier = modifier,
        containerColor = MaterialTheme.colorScheme.primaryContainer,
        contentColor = MaterialTheme.colorScheme.onPrimaryContainer,
        shape = CircleShape,
        elevation = FloatingActionButtonDefaults.elevation(defaultElevation = 6.dp)
    ) {


        Icon(
            imageVector = Icons.Default.Add,
            contentDescription = "Add Report",
            modifier = Modifier.size(28.dp)
        )

    }
}



fun createImageUri(context: Context): Uri? {
    val contentValues = ContentValues().apply {
       // put(MediaStore.Images.Media.DATE_TAKEN, System.currentTimeMillis()) without display name  its show unsupported image error
        put(MediaStore.Images.Media.DISPLAY_NAME, "IMG_${System.currentTimeMillis()}.jpg")
        put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg")

        put(MediaStore.Images.Media.RELATIVE_PATH, "Pictures/MyApp")
    }

    return context.contentResolver.insert(
        MediaStore.Images.Media.EXTERNAL_CONTENT_URI,// its a address of image table where all meta data store
        contentValues  )
}



@Composable
fun BottomBar(navController: NavHostController) {
    val items = listOf(
        AllRoute.Home,
        AllRoute.ReportList,
        AllRoute.Profile
    )

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    NavigationBar {
        items.forEach { screen ->

            val selected = currentDestination?.hierarchy?.any {
                it.route == screen.route
            } == true

            NavigationBarItem(
                selected = selected,
                onClick = {
                    val currentGraph = navController.currentBackStackEntry
                        ?.destination?.parent?.route
                    val targetGraph = screen.route

                    //if (currentGraph != targetGraph) {
                        // find main graph and home graph start destination safely
                        val mainGraphNode = navController.graph.findNode("main_Graph") as? NavGraph
                        val homeGraphNode = mainGraphNode?.findNode(AllRoute.Home.route) as? NavGraph
                        val homeStartId = homeGraphNode?.startDestinationId

                        navController.navigate(targetGraph) {
                            // only pop inside main_Graph, back to home’s start
                            if (homeStartId != null) {
                                popUpTo(homeStartId) {
                                    inclusive = false
                                    saveState = true
                                }
                            }
                            launchSingleTop = true
                            restoreState = true
                        }
                  //  }
                },
                icon = {
                    screen.icon?.let {
                        Icon(it, contentDescription = screen.label ?: "")
                    }
                },
                label = { Text(screen.label ?: "") },
                alwaysShowLabel = true
            )
        }
    }
}