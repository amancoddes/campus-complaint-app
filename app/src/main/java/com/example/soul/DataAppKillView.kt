//package com.example.soul
//
//import android.util.Log
//import androidx.lifecycle.SavedStateHandle
//import androidx.lifecycle.ViewModel
//import kotlinx.coroutines.flow.MutableStateFlow
//import kotlinx.coroutines.flow.StateFlow
//import java.io.Serializable
//
//
//data class Login(
//
//
//    val userName:String="",
//    val password:String=""
//        ): Serializable
//
//class DataAppKillView(private val savedStateHandle: SavedStateHandle):ViewModel(){
//
//    companion object{
//        const val KEY12="Login_Key"
//    }
//    val uiState:StateFlow<Login> = savedStateHandle.getStateFlow(KEY12,Login())
//
//    fun nameUpdate(input:String){
//        Log.d("MyApp", "Username updated: $input")
//        print("hey")
//        savedStateHandle[KEY12]= uiState.value.copy(userName = input)
//        print("after")
//        Log.d("MyApp", "Current UiState: ${uiState.value}")
//    }
//
//    fun passwordUpdate(input: String){
//        savedStateHandle[KEY12]=uiState.value.copy(password = input)
//    }
//}