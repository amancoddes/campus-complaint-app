package com.example.demo.complaintApp

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Inbox
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.map
import coil.compose.AsyncImage

@Composable
fun ShowList(items: LazyPagingItems<ComplaintDataRoom.ComplaintEntity>,clickItem:(ComplaintDataRoom.ComplaintEntity)->Unit) {

    val isEmpty =
        items.loadState.refresh is LoadState.NotLoading &&
                items.itemCount == 0

    if (isEmpty) {
        EmptySection()
        return
    }


    LazyColumn(
        modifier = Modifier.fillMaxSize()
    ) {



        items(items.itemCount) { index ->
            val item = items[index]

            item?.let {

                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(12.dp).clickable {
                            clickItem(it)
                        } ,
                    shape = RoundedCornerShape(16.dp),
                    elevation = CardDefaults.cardElevation(6.dp)
                ) {

                    Column {

                        //  IMAGE + TITLE + STATUS
                        Box {

                            AsyncImage(
                                model = item.url,
                                contentDescription = null,
                                placeholder = painterResource(R.drawable.ic_launcher_foreground),
                                error = painterResource(R.drawable.imagedefault),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(180.dp),

                                contentScale = ContentScale.Crop
                            )

                            //  Title overlay

                            Text(
                                text = it.complain,
                                color = Color.White,
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier
                                    .align(Alignment.BottomStart)
                                    .padding(12.dp)
                            )

                            // Status badge
                            val isResolved = it.status == "RESOLVED"

                            Box(
                                modifier = Modifier
                                    .align(Alignment.TopEnd)
                                    .padding(10.dp)
                                    .background(
                                        color = if (isResolved) Color(0xFF2ECC71) else Color(0xFFE74C3C),
                                        shape = RoundedCornerShape(20.dp)
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

                        // DETAILS SECTION
                        Column(
                            modifier = Modifier.padding(12.dp)
                        ) {

                            Text(
                                text = "Category: ${it.complain}",
                                fontSize = 14.sp,
                                color = Color.Gray
                            )
                            Spacer(modifier = Modifier.height(2.dp))

                            if(it.address.isNotBlank()){
                                Text(
                                    text = it.address,
                                    color = Color.White,
                                    fontSize = 18.sp,
                                    fontWeight = FontWeight.Bold,
                                )
                            }

                            Spacer(modifier = Modifier.height(2.dp))

                            Text(
                                text = "⏱ ${formatTime(it.timestamp)}",
                                fontSize = 12.sp,
                                color = Color.Gray
                            )
                        }
                    }
                }
            }
        }

        // Load states
        items.apply {
            when (loadState.append) {

                is LoadState.Loading -> {
                    Log.e("loading check "," there 1")
                    item {
                        Box(
                            modifier = Modifier.fillMaxWidth(),
                            contentAlignment = Alignment.Center
                        ) {
                            CircularProgressIndicator(modifier = Modifier.padding(16.dp))
                        }
                    }
                }

                is LoadState.Error -> {
                    item {
                        Text(
                            "Error loading more",
                            modifier = Modifier.padding(16.dp),
                            color = Color.Red
                        )
                    }
                }

                else -> Unit
            }
        }
    }
}

fun formatTime(time: Long): String {
    val diff = System.currentTimeMillis() - time
    val minutes = diff / (1000 * 60)

    return when {
        minutes < 60 -> "$minutes min ago"
        minutes < 1440 -> "${minutes / 60} hr ago"
        else -> "${minutes / 1440} days ago"
    }
}

@Composable
fun EmptySection(
    title: String = "No Complaints",
    message: String = "You're all caught up! 🎉",
    buttonText: String? = null,
    onActionClick: (() -> Unit)? = null
) {

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        contentAlignment = Alignment.Center
    ) {

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {

            // icon
            Icon(
                imageVector = Icons.Default.Inbox,
                contentDescription = null,
                tint = Color.Gray,
                modifier = Modifier.size(80.dp)
            )

            Spacer(modifier = Modifier.height(16.dp))

            //Title
            Text(
                text = title,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Message
            Text(
                text = message,
                fontSize = 14.sp,
                color = Color.Gray,
                textAlign = TextAlign.Center
            )


            if (buttonText != null && onActionClick != null) {

                Spacer(modifier = Modifier.height(20.dp))

                Button(
                    onClick = onActionClick,
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text(buttonText)
                }
            }
        }
    }
}