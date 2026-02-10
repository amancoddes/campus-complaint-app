//package com.example.soul
//import androidx.compose.foundation.Image
//import androidx.compose.foundation.background
//import androidx.compose.foundation.layout.Arrangement
//import androidx.compose.foundation.layout.Box
//import androidx.compose.foundation.layout.Column
//import androidx.compose.foundation.layout.Spacer
//import androidx.compose.foundation.layout.fillMaxSize
//import androidx.compose.foundation.layout.fillMaxWidth
//import androidx.compose.foundation.layout.height
//import androidx.compose.foundation.layout.padding
//import androidx.compose.foundation.lazy.LazyColumn
//import androidx.compose.foundation.rememberScrollState
//import androidx.compose.foundation.shape.RoundedCornerShape
//import androidx.compose.foundation.text.KeyboardActions
//import androidx.compose.foundation.text.KeyboardOptions
//import androidx.compose.foundation.verticalScroll
//import androidx.compose.material.icons.Icons
//import androidx.compose.material.icons.filled.Search
//import androidx.compose.material.icons.filled.Settings
//import androidx.compose.material3.Button
//import androidx.compose.material3.Card
//import androidx.compose.material3.CardDefaults
//import androidx.compose.material3.CircularProgressIndicator
//import androidx.compose.material3.ExperimentalMaterial3Api
//import androidx.compose.material3.Icon
//import androidx.compose.material3.IconButton
//import androidx.compose.material3.MaterialTheme
//import androidx.compose.material3.OutlinedTextField
//import androidx.compose.material3.OutlinedTextFieldDefaults
//import androidx.compose.material3.Scaffold
//import androidx.compose.material3.Surface
//import androidx.compose.material3.Text
//import androidx.compose.material3.TopAppBar
//import androidx.compose.material3.TopAppBarDefaults
//import androidx.compose.runtime.Composable
//import androidx.compose.ui.Alignment
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.graphics.Color
//import androidx.compose.ui.layout.ContentScale
//import androidx.compose.ui.res.painterResource
//import androidx.compose.ui.text.input.ImeAction
//import androidx.compose.ui.unit.dp
//import androidx.navigation.NavHostController
//import androidx.paging.LoadState
//import androidx.paging.compose.collectAsLazyPagingItems
//@OptIn(ExperimentalMaterial3Api::class)
//@Composable
//fun Home_Screen(navHostController: NavHostController,viewModel:Homeviewmodel) {
//
//    // 👇 Local state to hold search text
//    // val searchQuery by viewModel.searchToCompose.collectAsState()
//
//    Scaffold(
//        topBar = {
//
//            TopAppBar(
//                title = {
//                    Text("complain app")
//                },
//                actions = {
//                    IconButton(onClick = { }) {
//                        Icon(imageVector = Icons.Default.Settings, contentDescription = "setting", tint = Color.White)
//                    }
//                },
//                colors = TopAppBarDefaults.topAppBarColors(
//                    containerColor = MaterialTheme.colorScheme.primary, // 🔥 Background color
//                    titleContentColor = MaterialTheme.colorScheme.onPrimary // 🔥 Text color
//                )
//            )
////            HomeTopBar(
////                query = searchQuery,
////                onQueryChange = { viewModel.updateSerchValue(it) },
////                onSearch = {
////                    // 💡 You can perform your search logic here
////                    // Example: navigate to result page or filter a list
////                    println("User searched for: $searchQuery")
////                }
////            )
//        },
////        bottomBar = {
////            BottomBar(navController = navHostController)
////        },
//
//
//        // You can add bottomBar or FAB here too later
//        containerColor = MaterialTheme.colorScheme.background// niche ka bada area
//    ) { innerPadding ->
//
//        // 💬 Your main page content goes here
//        Box(
//            modifier = Modifier
//                .fillMaxSize()
//                .padding(innerPadding),
//            contentAlignment = Alignment.Center
//        ) {
////
////          val pagingData = viewModel.complaintPagingFlow.collectAsLazyPagingItems()
////
////
////            LazyColumn(verticalArrangement = Arrangement.spacedBy(5.dp)) {
////                items(pagingData.itemCount) { index ->// itemCount jo abhi hai items uka total   itemen(10){}
////                    val item = pagingData[index]// ye presenter ko or presenter hie Ram se data nikar kar dega
////                    if (item != null) {
////                        CardMethod(item)
////                   }
////
////                 }
////
////
////                pagingData.apply {
////
////                    // ⃣ Refresh Loading (start of list)
////                    if (loadState.refresh is LoadState.Loading) {
////                        item { CircularProgressIndicator() }
////                        return@apply
////                    }
////
////                    // 2️⃣ Refresh Error (like no internet)
////                    if (loadState.refresh is LoadState.Error) {
////                        val error = (loadState.refresh as LoadState.Error).error
////                        val cleanMessage =
////                            if (error.message?.contains("Failed to get documents") == true)
////                                "No Internet Connection"
////                            else
////                                error.message ?: "Unknown error"
////
////                        item {
////                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
////                                Text(cleanMessage)
////                                Button(onClick = { retry() }) {
////                                    Text("Retry")
////                                }
////                            }
////                        }
////                        return@apply
////                    }
////
////                    // 3️⃣ No data in the list AFTER refresh completed
////                    if (itemCount == 0) {
////                        item {
////                            Box(
////                                modifier = Modifier.fillMaxSize(),
////                                contentAlignment = Alignment.Center
////                            ) {
////                                Text("No data available")
////                            }
////                        }
////                        return@apply
////                    }
////
////                    // 4️⃣ Append (loading next pages)
////                    if (loadState.append is LoadState.Loading) {
////                        item { CircularProgressIndicator() }
////                        return@apply
////                    }
////
////                    // 5️⃣ Append Error
////                    if (loadState.append is LoadState.Error) {
////                        val error = (loadState.append as LoadState.Error).error
////                        val cleanMessage =
////                            if (error.message?.contains("Failed to get documents") == true)
////                                "No Internet Connection"
////                            else
////                                error.message ?: "Unknown error"
////
////                        item {
////                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
////                                Text(cleanMessage)
////                                Button(onClick = { retry() }) {
////                                    Text("Retry")
////                                }
////                            }
////                        }
////                    }
////                }
////            }
//
//
//
//
//
//        }
//    }
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
//@Composable
//fun HomeTopBar(
//    query: String,
//    onQueryChange: (String) -> Unit,
//    onSearch: () -> Unit,// where we use it
//) {
//    Surface(
//        color = MaterialTheme.colorScheme.primary,
//        shadowElevation = 14.dp,
//        shape = RoundedCornerShape(topStart = 0.dp, topEnd = 0.dp, bottomStart = 15.dp, bottomEnd = 15.dp ),
//        tonalElevation = 16.dp,
//    ) {
//        Column(
//            modifier = Modifier
//                .fillMaxWidth()
//                .padding(16.dp)
//        ) {
//            Text(
//                text = "MJPRU",
//                style = MaterialTheme.typography.headlineSmall,
//                color = MaterialTheme.colorScheme.onPrimary
//            )
//
//            Spacer(modifier = Modifier.height(12.dp))
//
//            OutlinedTextField(
//                value = query,
//                onValueChange = onQueryChange,
//                placeholder = { Text("Search area, issue, or report ID...") },
//                leadingIcon = {
//                    Icon(
//                        imageVector = Icons.Default.Search,
//                        contentDescription = "Search"
//                    )
//                },
//                singleLine = true,
//                shape = RoundedCornerShape(16.dp),
//                modifier = Modifier
//                    .fillMaxWidth()
//                    .height(56.dp),
//                colors = OutlinedTextFieldDefaults.colors(
//                    focusedContainerColor = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.05f),
//                    unfocusedContainerColor = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.05f),
//                    focusedBorderColor = MaterialTheme.colorScheme.onPrimary,
//                    unfocusedBorderColor = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.3f),
//                    focusedTextColor = MaterialTheme.colorScheme.onPrimary,
//                    unfocusedTextColor = MaterialTheme.colorScheme.onPrimary,
//                    cursorColor = MaterialTheme.colorScheme.onPrimary
//                ),
//                keyboardActions = KeyboardActions(onSearch = { onSearch() }),
//                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search)
//            )
//        }
//    }
//}
////
////Image(
////painter = rememberAsyncImagePainter(
////model = imageUrl, // API se aaye photo
////placeholder = painterResource(id = R.drawable.placeholder_card),
////error = painterResource(id = R.drawable.placeholder_card)
////),
////contentDescription = null,
////modifier = Modifier.fillMaxWidth(),
////contentScale = ContentScale.Crop
////)
//@Composable
//fun CardMethod(item:FirstAppFireStoreDataClass?){
//
//    Card( modifier = Modifier.fillMaxWidth(),
//        colors = CardDefaults.cardColors(
//            containerColor = MaterialTheme.colorScheme.surfaceVariant
//        ),
//        elevation = CardDefaults.cardElevation(6.dp)) {
//
//        Image(
//            painter = painterResource(id=R.drawable.imagedefault),
//            contentDescription = "pagging 3 image ",
//
//            contentScale = ContentScale.Crop,
//            modifier = Modifier.padding(10.dp).fillMaxWidth().height(300.dp)
//        )
//
//        Spacer(modifier = Modifier.height(10.dp))
//
//        Column(modifier = Modifier.fillMaxWidth().height(100.dp).verticalScroll(rememberScrollState())) {
//            item?.let { x->
//                Text(x.complain)
//                Text(x.description)
//                Text(x.address)
//            }?:"not available "
//        }
//
//    }
//
//}
//
