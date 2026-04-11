package com.example.demo.complaintApp

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun HomeContent(
    recent: List<ComplaintDataRoom.ComplaintEntity>,
    pending: Int,
    resolved: Int,
    onViewAllClick: () -> Unit
) {

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.primary)
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {

        //  Stats
        item {
            Row(
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                StatCard("Pending", pending, Brush.linearGradient(
                    listOf(Color(0xFFFFA726), Color(0xFFFF7043))
                ))

                StatCard("Resolved", resolved, Brush.linearGradient(
                    listOf(Color(0xFF26A69A), Color(0xFF66BB6A))
                ))

                StatCard("Total", pending + resolved, Brush.linearGradient(
                    listOf(Color(0xFF90A4AE), Color(0xFFB0BEC5))
                ))
            }
        }

        //  Header
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    "Recent Complaints",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )

                Text(
                    "View All →",
                    color = MaterialTheme.colorScheme.onPrimary,
                    modifier = Modifier.clickable { onViewAllClick() }
                )
            }
        }

        //  List
        items(recent.take(5)) {
            ComplaintCard(it)
        }
    }
}
@Composable
fun RowScope.StatCard(
    title: String,
    count: Int,
    brush: Brush
) {

    Card(
        modifier = Modifier
            .weight(1f)
            .height(90.dp),
        shape = RoundedCornerShape(18.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(brush)
                .padding(12.dp)
        ) {
            Column {
                Text(title, color = Color.White)
                Spacer(Modifier.height(6.dp))
                Text(
                    count.toString(),
                    color = Color.White,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}
@Composable
fun ComplaintCard(item: ComplaintDataRoom.ComplaintEntity) {

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
       // elevation = CardDefaults.cardElevation(8.dp)
    ) {

        Box {

            Image(
                painter = painterResource(R.drawable.imagegreeting),
                contentDescription = null,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(140.dp),
                contentScale = ContentScale.Crop
            )

            // Gradient overlay


            Column(
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .padding(12.dp)
            ) {

                Text(
                    text = item.complain,
                    color = Color.White,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )

                Text(
                    text = "⏱ ${formatTime(item.timestamp)}",
                    fontSize = 12.sp,
                    color = Color.Gray
                )
            }
        }
    }
}