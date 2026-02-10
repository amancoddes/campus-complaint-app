//package com.example.soul
//
//import androidx.compose.animation.animateContentSize
//import androidx.compose.foundation.background
//import androidx.compose.foundation.layout.Arrangement
//import androidx.compose.foundation.layout.Box
//import androidx.compose.foundation.layout.fillMaxSize
//import androidx.compose.foundation.layout.imePadding
//import androidx.compose.foundation.layout.padding
//import androidx.compose.foundation.layout.size
//import androidx.compose.foundation.lazy.LazyColumn
//import androidx.compose.foundation.lazy.rememberLazyListState
//import androidx.compose.foundation.shape.RoundedCornerShape
//import androidx.compose.material.icons.Icons
//import androidx.compose.material.icons.automirrored.filled.ArrowBack
//import androidx.compose.material.icons.filled.AccountCircle
//import androidx.compose.material.icons.filled.Delete
//import androidx.compose.material.icons.filled.Edit
//import androidx.compose.material3.BottomAppBar
//import androidx.compose.material3.Card
//import androidx.compose.material3.CardDefaults
//import androidx.compose.material3.ExperimentalMaterial3Api
//import androidx.compose.material3.Icon
//import androidx.compose.material3.IconButton
//import androidx.compose.material3.Scaffold
//import androidx.compose.material3.Text
//import androidx.compose.material3.TopAppBar
//import androidx.compose.material3.TopAppBarDefaults
//import androidx.compose.runtime.Composable
//import androidx.compose.runtime.LaunchedEffect
//import androidx.compose.runtime.collectAsState
//import androidx.compose.runtime.getValue
//import androidx.compose.ui.Alignment
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.graphics.Brush
//import androidx.compose.ui.graphics.Color
//import androidx.compose.ui.platform.LocalFocusManager
//import androidx.compose.ui.platform.LocalSoftwareKeyboardController
//import androidx.compose.ui.unit.dp
//import androidx.compose.ui.unit.sp
//import androidx.lifecycle.viewmodel.compose.viewModel
//import java.text.SimpleDateFormat
//import java.util.Date
//import java.util.Locale
//
//@OptIn(ExperimentalMaterial3Api::class)
//@Composable
//fun ChatApp(){
//    val focus= LocalFocusManager.current
//    val keyboard= LocalSoftwareKeyboardController.current
//    val view:ChatAppViewModel= viewModel(factory = ChatAppFactory())
//    val change by view.carpClick.collectAsState()
//
//    val message by view.message.collectAsState()// typing
//
//    // load all message
//    val messageLoad by view.messageDataFromDataBase.collectAsState()
//
//    Scaffold (
//        topBar = {
//            TopAppBar(colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.Transparent),
//                navigationIcon = {
//                    if(change!=null)
//                    {
//                        IconButton(onClick = {view.topbarChangeCancel()}) {
//                            Icon(imageVector = Icons.AutoMirrored.Filled.ArrowBack, contentDescription = null, tint = Color.White, modifier = Modifier.size(30.dp))
//                        }
//                    }
//
//                },
//                title = {
//                    if (change==null){
//                        Text("social Buddy ", color = Color.White)
//                    }
//                    else{
//
//                        Text("1 selected")
//
//                    }
//                },
//                actions = {
//                    if (change!=null){
//                        IconButton(onClick = { view.deleteMessage(change.toString()) }) {
//                            Icon(
//                                imageVector = Icons.Default.Delete,
//                                contentDescription = "Delete", tint = Color.White
//                            )
//                        }
//
//                        IconButton(onClick = {view.editMessage45(change.toString(),"hey its editied messge")}) {
//                            Icon(imageVector = Icons.Default.Edit, contentDescription = null, tint = Color.White)
//                        }
//
//                    }
//                    else{
//                        Icon(imageVector = Icons.Default.AccountCircle, contentDescription = null, modifier = Modifier.size(50.dp), tint = Color.White)
//
//                    }
//                }
//            )
//        },
//        bottomBar = {
//            BottomAppBar(containerColor = Color.Transparent, modifier = Modifier.imePadding()) {
//                OutlinedFeed(view,message)// outlinedFeed is another file where outlinedTextfield()
//            }
//        }
//    ){
//            paddingValues ->
//        Box( modifier = Modifier.fillMaxSize().background(brush = Brush.verticalGradient(colors = listOf(
//            Color.Black, Color.White), startY = 600f)).padding(paddingValues).clearSabFocus(focus,keyboard)){
//
//            messageLoad?.let { result ->
//                when {
//                    result.isSuccess -> {
//
//                        val listMessages = result.getOrNull() ?: emptyList()
//                        println("🎃 ${listMessages.size}")// debugging
//
//                        //
//                        val listState = rememberLazyListState(
//                            initialFirstVisibleItemIndex = if (listMessages.isNotEmpty()) listMessages.size - 1 else 0,
//                            initialFirstVisibleItemScrollOffset = 0
//                        )
//                        LazyColumn(
//                            state = listState,
//                            modifier = Modifier.fillMaxSize().background(
//
//                            Color.Transparent), verticalArrangement = Arrangement.spacedBy(4.dp), horizontalAlignment = Alignment.CenterHorizontally) {
//                            items(listMessages.size) { index ->
//                                val currentMessageElement = listMessages[index]
//                                val currentMessage = currentMessageElement["message"]
//                                val currentDocId=currentMessageElement["id"]//
//
//
//                                val timeMillis = currentMessageElement["time"]?.toString()?.toLongOrNull() ?: 0L
//                                val formattedTime = SimpleDateFormat("hh:mm a", Locale.getDefault())
//                                    .format(Date(timeMillis))
//                                Card(modifier = Modifier.animateContentSize(), colors = CardDefaults.cardColors(Color.Gray), shape = RoundedCornerShape(5.dp), onClick = {view.topbarChange(currentDocId.toString())}) {
//                                    Text(" $currentMessage", fontSize = 16.sp, color = Color.Black, modifier = Modifier.padding(vertical = 2.dp, horizontal = 20.dp))
//                                    Text("$formattedTime ", fontSize = 8.sp, modifier = Modifier.align(alignment = Alignment.CenterHorizontally))
//                                }
//                            }
//                        }
//                        LaunchedEffect(listMessages.size) {
//                            if (listMessages.isNotEmpty()) {
//                                // Scroll to the last message
//                                listState.animateScrollToItem(listMessages.size - 1
//                                       , scrollOffset = 80 )// .ye aak fucniton hai to liststate ko leta hai or size -1 tak lzy column ko scrooll karta hai
//                            }
//                        }
//                    }
//                    result.isFailure->{
//                        Text("error ${result.exceptionOrNull()}")
//                    }
//                }
//            }
//
//
//        }
//
//    }
//
//}
