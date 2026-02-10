//package com.example.soul
//
//import androidx.compose.foundation.background
//import androidx.compose.foundation.clickable
//import androidx.compose.foundation.layout.Arrangement
//import androidx.compose.foundation.layout.Box
//import androidx.compose.foundation.layout.Column
//import androidx.compose.foundation.layout.Spacer
//import androidx.compose.foundation.layout.fillMaxSize
//import androidx.compose.foundation.layout.height
//import androidx.compose.foundation.layout.imePadding
//import androidx.compose.foundation.layout.padding
//import androidx.compose.foundation.layout.size
//import androidx.compose.foundation.lazy.LazyColumn
//import androidx.compose.foundation.lazy.items
//import androidx.compose.foundation.lazy.rememberLazyListState
//import androidx.compose.material.icons.Icons
//import androidx.compose.material.icons.automirrored.filled.ArrowBack
//import androidx.compose.material.icons.automirrored.filled.Send
//import androidx.compose.material.icons.filled.AccountCircle
//import androidx.compose.material.icons.filled.Delete
//import androidx.compose.material.icons.filled.Edit
//import androidx.compose.material3.BottomAppBar
//import androidx.compose.material3.Card
//import androidx.compose.material3.ExperimentalMaterial3Api
//import androidx.compose.material3.Icon
//import androidx.compose.material3.IconButton
//import androidx.compose.material3.OutlinedTextField
//import androidx.compose.material3.Scaffold
//import androidx.compose.material3.Text
//import androidx.compose.material3.TopAppBar
//import androidx.compose.material3.TopAppBarDefaults
//import androidx.compose.runtime.Composable
//import androidx.compose.runtime.collectAsState
//import androidx.compose.runtime.getValue
//import androidx.compose.runtime.mutableStateOf
//import androidx.compose.runtime.remember
//import androidx.compose.runtime.setValue
//import androidx.compose.ui.Alignment
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.graphics.Brush
//import androidx.compose.ui.graphics.Color
//import androidx.compose.ui.platform.LocalFocusManager
//import androidx.compose.ui.platform.LocalSoftwareKeyboardController
//import androidx.compose.ui.unit.dp
//import androidx.compose.ui.unit.sp
//import androidx.lifecycle.viewmodel.compose.viewModel
//var no=0
//@OptIn(ExperimentalMaterial3Api::class)
//@Composable
//fun ChatApp() {
//    val focus = LocalFocusManager.current
//    val keyboard = LocalSoftwareKeyboardController.current
//    val view: ChatAppViewModel = viewModel(factory = ChatAppFactory())
//
//
//    val message by view.message.collectAsState()// typing// frist textfield
//    // load all message
//    val messageLoad by view.messageDataFromDataBase.collectAsState()// data from backend
//    val change by view.carpClick.collectAsState()// showtop bar
//
//    val openEdit by view.startEdit.collectAsState()
//    Scaffold(
//        topBar = {
//            TopAppBar(colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.Transparent),
//                navigationIcon = {
//                    if (change != null) {
//                        IconButton(onClick = { view.topbarChangeCancel() }) {
//                            Icon(
//                                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
//                                contentDescription = null,
//                                tint = Color.White,
//                                modifier = Modifier.size(30.dp)
//                            )
//                        }
//                    }
//
//                },
//                title = {
//                    if (change == null) {
//                        Text("social Buddy ", color = Color.White)
//                    } else {
//
//                        Text("1 selected")
//
//                    }
//                },
//                actions = {
//                    if (change != null) {
//                        IconButton(onClick = { view.deleteMessage(change.toString()) }) {
//                            Icon(
//                                imageVector = Icons.Default.Delete,
//                                contentDescription = "Delete", tint = Color.White
//                            )
//                        }
//
//                        IconButton(onClick = {
//                            view.editStart(change.toString())
//                        }
//                        ) {
//                            Icon(
//                                imageVector = Icons.Default.Edit,
//                                contentDescription = null,
//                                tint = Color.White//           Edit Icon///////
//                            )
//                        }
//
//                    } else {
//                        Icon(
//                            imageVector = Icons.Default.AccountCircle,
//                            contentDescription = null,
//                            modifier = Modifier.size(50.dp),
//                            tint = Color.White
//                        )
//
//                    }
//                }
//            )
//        },
//        bottomBar = {
//            if(openEdit==null){
//                BottomAppBar(containerColor = Color.Transparent, modifier = Modifier.imePadding()) {
//                    OutlinedFeed(view, message)// outlinedFeed is another file where outlinedTextfield()
//                }
//            }
//
//        }
//    ) { paddingValues ->
//        Box(
//            modifier = Modifier.fillMaxSize().background(
//                brush = Brush.verticalGradient(
//                    colors = listOf(
//                        Color.Black, Color.White
//                    ), startY = 600f
//                )
//            ).padding(paddingValues).clearSabFocus(focus, keyboard)
//        ) {
//            messageLoad?.let { result ->
//                when{
//                    result.isSuccess->{
//
//
//                        if(openEdit==null){
//                            val list=result.getOrNull()
//                            if (list != null) {
//                                LazyList(list,
//                                    change = { id ->
//                                        view.topbarChange(id)
//                                    }
//                                )
//                            }
//                        }
//
//
//
//
//
//
//
//                        else{
//Column(modifier = Modifier.fillMaxSize().background(brush = Brush.verticalGradient(
//    colors = listOf(
//        Color.Black, Color.White
//    ), startY = 600f
//)), verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally
//){
//
//
//    val edit by view.messageEdit.collectAsState()
//
//
//    Card {
//        Text(" edit message ", modifier = Modifier.padding(5.dp))
//    }
//Spacer(modifier = Modifier.height(20.dp))
//    OutlinedTextField(
//        value = edit,
//        onValueChange = {view.messageInputEdit(it)},
//       trailingIcon = {
//           IconButton(onClick = {view.editMessage45(docId2 = change.toString(), editMessage = edit)}, modifier = Modifier.padding(2.dp)) {
//               Icon(imageVector = Icons.AutoMirrored.Filled.Send ,contentDescription = null,
//                   Modifier.size(30.dp), tint = Color.White)
//           }
//       }
//    )
//}
//                        }
//
//
//                    }
//                    result.isFailure->{
//                        println(result.exceptionOrNull())
//                    }
//                }
//            }
//            }
//        }
//
//    }
//
//
//
//
//
//
//
//@Composable
//fun LazyList(list:List<Map<String,Any>>, change:(String)->Unit){
//
//    LazyColumn(verticalArrangement = Arrangement.spacedBy(10.dp)){
//
//        items(list.size){
//            index -> val currentElement=list[index]
//            val message=currentElement["message"]
//            val id=currentElement["id"]
//            Card(onClick = {change(id.toString())}){
//                Text(message.toString(), fontSize = 19.sp, modifier = Modifier.padding(start = 10.dp, top = 3.dp, end = 15.dp, bottom = 6.dp))
//            }
//        }
//    }
//}