//package com.example.soul
//
//import android.os.Parcelable
//import androidx.lifecycle.SavedStateHandle
//import androidx.lifecycle.ViewModel
//import androidx.versionedparcelable.ParcelField
//import com.example.soul.SavedStateHandelRoomView.Companion.KEY45
//import dagger.hilt.android.lifecycle.HiltViewModel
//import kotlinx.coroutines.flow.StateFlow
//import java.io.Serializable
//import javax.inject.Inject
//
//data class Value(val route:String,val value:String):Serializable
//
//@HiltViewModel
//class VieworStateHandel @Inject constructor(
//    private val handle: SavedStateHandle
//) : ViewModel(){
//
//    companion object{
//        const val key1="Screens"
//    }
//
//    val stateScreen:StateFlow<Value> = handle.getStateFlow(key1,Value(route = Graph1.Home.route,"..."))
//
//    fun storeScreen(input:String){
//        handle[key1]=stateScreen.value.copy(route = input)
//    }
//
//    fun valueSet(input: String){
//        handle[key1]=stateScreen.value.copy(value = input)
//    }
//}
//
//
//
