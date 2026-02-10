package com.example.soul

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import coil.compose.rememberAsyncImagePainter


@Composable
fun AllListReports(viewmodel: ReportListViewmodel,navHostController: NavHostController){
    val list by viewmodel.complaints.collectAsState()


    Scaffold(topBar = {
        CommonTopAppBar(
            title = "complaints list",
            onBackClick = { navHostController.popBackStack() }
        )
    }) { padding ->
        LazyColumn(Modifier.padding(padding)) {
            if (list.isEmpty()) {
                item {
                    LoadingShimmer(navHostController)
                }
            }
            items(list) { item ->

                ComplaintCard(item, onClick = {
                    Log.e("Laxy","run ${item.complain}")
                    navHostController.navigate("complaint_detail/${item.id}")
                })
            }
        }
    }
}
@Composable
fun ComplaintCard(
    item: ComplaintDataRoom.ComplaintEntity,
    onClick: () -> Unit
) {

    Column(
        modifier = Modifier
            .padding(12.dp)
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.primaryContainer, RoundedCornerShape(8.dp))
            .clickable { onClick() }
            .padding(12.dp)
    ) {

        Image(
            painter = rememberAsyncImagePainter(model = R.drawable.imagedefault),
            contentDescription = null,
            modifier = Modifier
                .padding(horizontal = 16.dp)
                .fillMaxWidth()
                .height(300.dp),
            contentScale = ContentScale.Crop
        )

        Spacer(Modifier.height(12.dp))

        Text("complaint title")
        Text(item.complain)
        Text("description")
        Text(item.description)
        Text("address")
        Text(item.address)
        if(item.status=="solve"){
            Text(item.status)
        }
        Text(formatTimestamp(item.timestamp.toLong()))
    }


}


