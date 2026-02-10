//package com.example.soul
//
//import android.os.Bundle
//import androidx.lifecycle.AbstractSavedStateViewModelFactory
//import androidx.lifecycle.SavedStateHandle
//import androidx.lifecycle.ViewModel
//import androidx.lifecycle.ViewModelStoreOwner
//import androidx.lifecycle.viewModelScope
//import androidx.room.Dao
//import androidx.savedstate.SavedStateRegistryOwner
//import dagger.hilt.android.lifecycle.HiltViewModel
//import kotlinx.coroutines.Dispatchers
//import kotlinx.coroutines.flow.StateFlow
//import kotlinx.coroutines.launch
//import okhttp3.Dispatcher
//import java.io.Serializable
//import javax.inject.Inject
//
//data class Login2(
//    val userName:String="",
//):Serializable
//
//@HiltViewModel
//class SavedStateHandelRoomView @Inject constructor(val repo: RepositorySavedState, private val handle:SavedStateHandle):ViewModel(){
//
//    companion object
//    {
//        const val KEY45="username"
//    }
//    val loginState:StateFlow<Login2> = handle.getStateFlow(KEY45, Login2( userName = ""))
//
//    fun updateUserName(input:String){
//        handle[KEY45]=loginState.value.copy(userName = input)
//    }
//
//    fun saveDataINRoom(){
//        viewModelScope.launch(Dispatchers.IO){
//            repo.inserted(loginState.value)
//            handle[KEY45]= Login2("")
//        }
//    }
//
//    fun getAll(){
//        viewModelScope.launch(Dispatchers.IO){
//           val x= repo.display()?.userName
//            handle[KEY45]= x?.let { Login2(it) }
//        }
//    }
//
//
//}
////
////class Handle(val repo: RepositorySavedState, owner: SavedStateRegistryOwner, default:Bundle?=null):AbstractSavedStateViewModelFactory(owner,default){
////    override fun <T : ViewModel> create(
////        key: String,
////        modelClass: Class<T>,
////        handle: SavedStateHandle
////    ): T {
////        return SavedStateHandelRoomView(repo,handle) as T
////    }
////}
