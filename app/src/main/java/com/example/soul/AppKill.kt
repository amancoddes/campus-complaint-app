package com.example.soul

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.lifecycle.viewmodel.compose.viewModel
@Composable
fun AppKill (){
    val view:AppView= viewModel()

    val message by view.drift.collectAsState()
    val password by view.password.collectAsState()

    Column {
        OutlinedTextField(
            value = message,
            onValueChange = {view.update(it)},
            label = {
                Text("message")
            }
        )

        OutlinedTextField(
            value = password,
            onValueChange = {view.updatePassword(it)},
            label = {
                Text("message")
            }
        )
    }
}