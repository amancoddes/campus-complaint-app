package com.example.demo.complaintApp

import android.util.Log
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import coil.compose.rememberAsyncImagePainter
import kotlinx.coroutines.flow.combine

@Composable
fun AllListReports(viewmodel: ReportListViewmodel,navHostController: NavHostController){

    val statesState by viewmodel.uiState.collectAsState()

    when(statesState){
        is CombineState.Loading -> {
            Log.e("success34", "ui loading ")
            ScaffoldMethod(content = { padding->
                LoadingShimmer(padding)
                           },navHostController)
        }
        is CombineState.Error -> {
            Log.e("success34", "error ui  ")

            ScaffoldMethod(content = { padding ->
                ErrorMethod(error = (statesState as CombineState.Error).message,padding,navHostController, onClick = { viewmodel.fetchUserDataAfterLoginAndSignUp()})
            },navHostController
            )

        }
        is CombineState.Success -> {
            Log.e("success34", "success ui ")

            val lists= (statesState as CombineState.Success).data
            ScaffoldMethod(content = {padding ->
                SuccessState(list =lists, navHostController = navHostController, padding = padding)
            },navHostController)
        }
        is CombineState.Empty -> {
            Log.e("success34", " empty ")

            ScaffoldMethod(content = { padding ->
                AddMethod(error =  "you not report any complaint",padding)
            },navHostController
            )
        }

        CombineState.Login ->{
            ScaffoldMethod(content = { padding ->
                LoginMethod(error = "user not login ",padding)
            },navHostController
            )
        }
    }

//    LazyColumn(Modifier.padding(padding)) {
//        items(list) { list ->
//
//            ComplaintCard(item, onClick = {
//                navHostController.navigate("complaint_detail/${item.id}")
//            })
//        }
//    }
//}




}
@Composable
fun ComplaintCard(
    item: ComplaintDataRoom.ComplaintEntity,
    onClick: () -> Unit,
    message:String,
    padding: PaddingValues
) {
    Column(
        modifier = Modifier
            .padding(padding).padding(10.dp)
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.primaryContainer, RoundedCornerShape(8.dp))
            //.clickable { onClick() }
    ) {

        Image(
            painter = rememberAsyncImagePainter(model = R.drawable.imagedefault),
            contentDescription = null,
            modifier = Modifier
                .padding(horizontal =6.dp, vertical = 9.dp)
                .fillMaxWidth()
                .height(300.dp),
            contentScale = ContentScale.Crop
        )

        Column(modifier = Modifier.fillMaxWidth()) {
            Spacer(Modifier.height(20.dp))

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        MaterialTheme.colorScheme.primaryContainer,
                        RoundedCornerShape(20.dp)
                    )
                    .border(
                        1.dp,
                        MaterialTheme.colorScheme.outline,
                        RoundedCornerShape(20.dp)
                    )
                    .padding(16.dp),
            ) {
                Text(
                    text = "complaint:",
                    fontWeight = FontWeight.Bold,
                    fontSize = 19.sp
                )
                Spacer(Modifier.width(8.dp))
                Text(
                    text = item.complain,
                    modifier = Modifier.weight(1f),
                    fontSize = 18.sp

                )
            }


            if(item.description.isNotBlank()){
                Box(
                    modifier = Modifier
                        .border(1.dp, Color.Gray, RoundedCornerShape(8.dp))
                        .padding(8.dp)
                ) {
                    Text(
                        text = item.description,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }
            Spacer(Modifier.height(8.dp))


            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        MaterialTheme.colorScheme.primaryContainer,
                        RoundedCornerShape(20.dp)
                    )
                    .border(
                        1.dp,
                        MaterialTheme.colorScheme.outline,
                        RoundedCornerShape(20.dp)
                    )
                    .padding(16.dp)
            ) {
                Text(
                    text = "address:",
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp
                )
                Spacer(Modifier.width(8.dp))
                Text(
                    text = item.address,
                    modifier = Modifier.weight(1f),
                    fontSize = 17.sp
                )
            }

            Spacer(Modifier.height(8.dp))

            Row {

                Column(Modifier.padding(10.dp)) {
                    Text("Time")
                    Text(formatTimestamp(item.timestamp.toLong()))

                    if(item.status=="solve"){
                        Text(item.status)
                    }
                }


                Spacer(Modifier.width(10.dp))

                Button(onClick=onClick) {
                    Text(message)
                }

            }



        }

    }


}


@Composable
fun ScaffoldMethod(content : @Composable (PaddingValues) -> Unit,navHostController: NavHostController,textSource:String=""){
    Scaffold(topBar = {
        CommonTopAppBar(
            title = "complaints list",
            onBackClick = { navHostController.popBackStack() }
        )
    }) { padding:PaddingValues->
        content(padding)

    }
}

@Composable
fun ErrorMethod(error:String,padding:PaddingValues,navHostController: NavHostController,onClick: () -> Unit){
    Box(modifier = Modifier.fillMaxSize().padding(padding), contentAlignment = Alignment.Center){

        Text(error)
        Spacer(modifier = Modifier.height(40.dp))
        Button(onClick = onClick){
            Text("try again")
        }

    }
}


@Composable
fun LoginMethod(error:String,padding:PaddingValues){
    Box(modifier = Modifier.fillMaxSize().padding(padding), contentAlignment = Alignment.Center){

        Text(error)
        Spacer(modifier = Modifier.height(40.dp))

    }
}


@Composable
fun AddMethod(error:String,padding:PaddingValues){
    Box(modifier = Modifier.fillMaxSize().padding(padding), contentAlignment = Alignment.Center){

        Text(error)
        Spacer(modifier = Modifier.height(40.dp))

    }
}

@Composable
fun SuccessState(list: List<ComplaintDataRoom.ComplaintEntity>, padding: PaddingValues,navHostController: NavHostController){
    LazyColumn(modifier = Modifier.padding(padding)) {// lazy items
        items(list) { item ->
            ComplaintCard(item, onClick = {
                navHostController.navigate("complaint_detail/${item.id}")
            }, message = "detail screen", padding = padding)
        }
    }
}