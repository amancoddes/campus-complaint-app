//package com.example.soul
//
//
//import android.net.Uri
//import androidx.compose.foundation.Image
//import androidx.compose.foundation.background
//import androidx.compose.foundation.layout.Column
//import androidx.compose.foundation.layout.fillMaxSize
//import androidx.compose.foundation.layout.fillMaxWidth
//import androidx.compose.foundation.layout.height
//import androidx.compose.foundation.layout.padding
//import androidx.compose.foundation.rememberScrollState
//import androidx.compose.foundation.verticalScroll
//import androidx.compose.material3.MaterialTheme
//import androidx.compose.runtime.Composable
//import androidx.navigation.NavHostController
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.layout.ContentScale
//import androidx.compose.ui.unit.dp
//import coil.compose.rememberAsyncImagePainter
//import java.net.URI
//
//@Composable
//fun Preview(navController: NavHostController,imageURI: Uri) {
//
////    rememberAsyncImagePainter(model = imageURI) OS se ek read-only stream open karta hai —
////    exactly wohi stream jahan tumhari image MediaStore me save hui hoti hai.
//
//
//
//    Column(modifier = Modifier.fillMaxSize().background(color = MaterialTheme.colorScheme.background).verticalScroll(
//        rememberScrollState()
//    )) {
//
//        Image(
//            painter = rememberAsyncImagePainter(model = imageURI),
//            contentDescription = null,
//            modifier = Modifier
//                .padding(horizontal = 16.dp)
//                .fillMaxWidth()
//                .height(300.dp),
//            contentScale = ContentScale.Crop
//        )
//
//
//    }
//
//}
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
//
//
//
//
//
//
//
//
//
////    val context = LocalContext.current
////
////
////
////    val permissionLauncher = rememberLauncherForActivityResult(
////        contract = ActivityResultContracts.RequestPermission()
////    ) { granted ->
////        if (granted) {
////            if (imageUri != null) {
////                cameraLauncher.launch(imageUri)
////            } else {
////                Toast.makeText(context, "storage not avilable", Toast.LENGTH_SHORT)
////                    .show()// jab row hie na ban paye to uri hie nhi mil payegi , or bhi sayad ho sake null hone ke liye
////            }
////        } else {
////            Toast.makeText(context, "allow the camera", Toast.LENGTH_SHORT).show()
////
////        }
////    }
////
////    val lifecycleOwner = LocalLifecycleOwner.current
////
////    DisposableEffect(lifecycleOwner) {
////        val observer = LifecycleEventObserver { _, event ->
////
////            if (event == Lifecycle.Event.ON_RESUME) {
////                val granted = ContextCompat.checkSelfPermission(
////                    context,
////                    Manifest.permission.CAMERA
////                ) == PackageManager.PERMISSION_GRANTED// PERMISSION_GRANTED <- its a variable
////                if (granted) {
////                    if (imageUri != null) {
////                        cameraLauncher.launch(imageUri)
////                    } else {
////                        Toast.makeText(context, "storage not available", Toast.LENGTH_SHORT).show()
////                    }
////                } else {
////                    permissionLauncher.launch(input = Manifest.permission.CAMERA)// its a only a remote which signal to Registry take this intent or launcher id and check permission and than Registry run dipatch()
////
////                }
////
////            }
////
////        }
////
////
////        lifecycleOwner.lifecycle.addObserver(observer)
////
////        onDispose {
////            lifecycleOwner.lifecycle.removeObserver(observer)
////        }
////    }
////
//
//
//
//
//
//
//
