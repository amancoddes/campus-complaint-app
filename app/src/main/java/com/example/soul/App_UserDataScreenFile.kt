package com.example.soul

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController

@Composable
fun userDataTaken(view: UserDataView,navHostController: NavHostController){

val screenState=view.state.collectAsState()

    LaunchedEffect(screenState.value) {
        if(screenState.value is SaveState.Success){
           navHostController.navigate("main_Graph"){
               println("black its run and open  te main graph")
               popUpTo(route = AuthScreens.Auth_UserData.route){
                   inclusive=true
               }
           }
        }
    }
    Column (modifier = Modifier.fillMaxSize().background(color = MaterialTheme.colorScheme.onPrimary).padding(horizontal = 10.dp), horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center){


        when (val s = screenState.value) {
            is SaveState.Loading -> CircularProgressIndicator()

            is SaveState.Error -> Text(s.msg, color = Color.Red)

            is SaveState.Success -> Unit   // navigate LaunchedEffect me ho chuka hoga

            is SaveState.Idle -> UserDataMain(view)
        }


    }

}





@Composable
fun UserDataMain(view: UserDataView){
    val state by view.uiData

    inputText(value = state.name, label = "enter name") { x -> view.update(set = SetEnum.Name,x)}

    Spacer(modifier = Modifier.height(10.dp))
    inputText(value = state.phone, label = "enter phone number") { x->  view.update(SetEnum.Phone,x) }

    Spacer(modifier = Modifier.height(10.dp))

    inputText(value = state.rollNo, label = "enter roll number") {x->  view.update(SetEnum.RollNo,x) }
    Spacer(modifier = Modifier.height(10.dp))
    val x2= listOf(
        "CSIT",
        "ECE",
        "ME",
        "EI",
        "EE",
        "MBA",
        "HU",
        "PHARMACY",
        "Law dept"
    )

    val branch by view.uiData
// not send callback for complainTitle jab viewmodel ke state mei change hoga to wasa bhi re compose kar dega screen ko
    TitleDropdownSelector(boxShow = "department", modifier = Modifier.fillMaxWidth(), listTitles =x2, complainTitle = branch.branch, onTitleSelected = {
        x->
        view.update(input = x, set = SetEnum.Branch)
    })
    Button(onClick = {view.sendData()}) {
        Text("submit")
    }
}




@Composable
fun inputText(
    value:String,
    label:String,
    onchange:(String) -> Unit
){

OutlinedTextField(
    value=value,
    onValueChange =onchange,
    label = {
        Text(label)
    },
    singleLine = true
)

}

