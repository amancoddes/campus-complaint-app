package com.example.soul
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




// first ye run hoga or sub phir esme
@OptIn(ExperimentalMaterial3Api::class)
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

        NavGraphSetup(// see it all things open from it
            navControllerGraph = navController,
            modifier = Modifier.padding(innerPadding)
        )
    }
}

// both
@Composable
fun shouldShowBottomBar(navController: NavHostController): Boolean {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route ?: return false// yaha esne fucniton hie return kar diya hai false nhi store kiya hai

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
    var hasPermission by remember { mutableStateOf(false) }// hasPermisson only use when we want to change the icon when want to change the FAB iocn accoriding it

    // ✅ Launcher to request camera permission
    //        contract = ActivityResultContracts.RequestPermission()// translator hai jo convert karata request ko intent mei ,jime ya specify karta hai ki request kis context mei hai permison ya take picture ya kuch or , kyu dono mei hie hm string hie pass karte hai to kon bataye ga ki eska context kya hai





    // create and remember photo URI
    val imageUri = remember {
        createImageUri(context)
    }


// bas yaha objet bana launcher ka launch() ko call on click par hoga
// CAMERA LAUNCHER
    val cameraLauncher = rememberLauncherForActivityResult(// its open the camera
        contract = ActivityResultContracts.TakePicture()// its specifiy the specific action or esme phale se createIntent() or parseResult() ka eska khud ka code hota hai
    ) { success ->
        if (success) {
           // Toast.makeText(context, "image save ", Toast.LENGTH_SHORT).show()
            val encodedUri = Uri.encode(imageUri.toString())//val uri = Uri.parse(encodedString)//Android automatically decode kar deta hai. //  So no extra step needed.
            navController.navigate("type?imageUri=$encodedUri")
        } else {
            Toast.makeText(context, "cancelled ", Toast.LENGTH_SHORT).show()

        }
    }
// same bas yaha launcher obejt bana hai .launch() baad mei run hoga jab check hoga ki permission allow nhi hai to ye phir dialog show karega
// CHECK PERMISSION
    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { granted ->
        hasPermission = granted// igonre it its use when you want ux increase
        if (granted) {
            Log.e("hey"," upper on on click FAB")
            // Once permission granted → go to Capture flow
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




    // fab ui
    FloatingActionButton(
        onClick = {

//            val dir = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
//            dir?.listFiles()?.forEach { file ->
//                file.delete()
//            }// delete all ,,,Ye saare old wrong images delete kar dega.
            //  Check if permission already granted
            val granted = ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.CAMERA
            ) == PackageManager.PERMISSION_GRANTED// PERMISSION_GRANTED <- its a variable

            if (granted) {// its run the camera launcher
                Log.e("hey","it run granted ")
                if (imageUri != null) {// image uri pahale le lete hai
                    cameraLauncher.launch(imageUri)// its not use camera api its simpley open phone default camera app and our app go to the background
                }else{
                    Toast.makeText(context, "storage not available", Toast.LENGTH_SHORT).show()
                }

            } else {// its run permission launcher
                // 2 Ask for permission
               permissionLauncher.launch(input = Manifest.permission.CAMERA)// its a only a remote which signal to Registry take this intent or launcher id and check permission and than Registry run dipatch()
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
       // put(MediaStore.Images.Media.DATE_TAKEN, System.currentTimeMillis())/display name jaruri hai nhi to unsupported show hogi image
        put(MediaStore.Images.Media.DISPLAY_NAME, "IMG_${System.currentTimeMillis()}.jpg") // esliye use this line
        put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg")

        put(MediaStore.Images.Media.RELATIVE_PATH, "Pictures/MyApp")
    }

    return context.contentResolver.insert(
        MediaStore.Images.Media.EXTERNAL_CONTENT_URI,// its a address of image table where all meta data store
        contentValues// all about to image and its give a folder name and os check its present or not and make it , but this time os not  make the file
    )
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
                        // ✅ Find main graph and home graph start destination safely
                        val mainGraphNode = navController.graph.findNode("main_Graph") as? NavGraph// its give "main_Graph" wala NavGarph object , so findNode() search and return ye wala route wala instance
                        val homeGraphNode = mainGraphNode?.findNode(AllRoute.Home.route) as? NavGraph
                        val homeStartId = homeGraphNode?.startDestinationId

                        navController.navigate(targetGraph) {
                            // ✅ Only pop inside main_Graph, back to home’s start
                            if (homeStartId != null) {
                                popUpTo(homeStartId) {
                                    inclusive = false
                                    saveState = true
                                }
                            }
                            launchSingleTop = true  // write insted of this                     //if (currentGraph != targetGraph) {
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













// bottom bar start when only splash close
//@Composable
//fun shouldShowBottomBar(navController: NavHostController): Boolean {
//    val navBackStackEntry by navController.currentBackStackEntryAsState()
//    val currentRoute = navBackStackEntry?.destination?.route ?: return false
//
//    // 🔥 Hide bottom bar until navController gets a real route
//    // 🔥 Hide bottom bar until navController gets a real route
//   // if (currentRoute == null) return false -> ?:return false
//
//    return currentRoute != AllGraphScreeens1.Splash.route
//}





















//
//@Composable
//fun BottomBar(navController: NavHostController) {
//    val items= listOf(AllGraphScreeens1.Home,AllGraphScreeens1.Report,AllGraphScreeens1.AddReport,AllGraphScreeens1.Profile)
//    val navBackStackEntry by navController.currentBackStackEntryAsState()
//    val currentDestination = navBackStackEntry?.destination
//
//
//
//
//    NavigationBar {
//
//
//        items.forEach { screen ->
//
//            /*
//            selected = currentDestination?.hierarchy?.any {
//    it.route == "home_graph"
//} == true
//............................................................
//
//“Agar mai Home graph ke andar kahin bhi hu (Home ya HomeDetail),
//to BottomBar me Home icon selected dikhao 🔥”
//             */
//            NavigationBarItem(
//                selected = currentDestination?.hierarchy?.any {
//                    it.route == screen.route
//                } ?:false,
//                onClick = {
//                    /*
//                    Jab Splash pop hoti hai (inclusive=true),
//tab NavController automatically
//next available graph (homeGraph) ko root active destination bana deta hai.
//
//Isliye baad me findStartDestination() Splash nahi dhoondhta,
//balki naye active root (homeGraph) tak hi clean karta hai 💫.”
//
//                     */
//                    navController.navigate(screen.route) {//route = "report_list_graph" bhi ek valid destination hai,
//                        // lekin uska startDestination automatically trigger hota hai. esliye open hota hai startdestination wala screen
//                        popUpTo(navController.graph.findStartDestination().id) {// ro hum cahahete hai ki  start se lekar curent tak ka bick screen delete ho jaye
//                            saveState = true
//                            /*
//                            Tu Home tab pe scroll kar raha tha 👇
//→ 50th complaint tak pahuch gaya 😅
//Phir tu Profile tab pe gaya.
//
//Normally jab tu back aata hai Home par —
//wo reset ho jaata hai (scroll top pe).
//😭 annoying, right?
//                             */
////
//////
//////                            /*
//////                            Aur saveState = true isliye,
//////taaki agar tu tab switch kare (Home → Profile → Home),
//////to purana Home ka scroll, search, aur filter state wapas mil jaye.
//////
//////                             */
//
//
//                        }
//                        launchSingleTop = true// phir se screen nhi banayega agar vahi open hai to
//                        restoreState = true//  load jo hune store hua tha savestate se
//                    }
//                },
//                icon = { Icon(screen.icon!!, contentDescription = screen.label) },
//                label = { Text(screen.label!!) },
//                alwaysShowLabel = true
//            )
//        }
//    }
//}







