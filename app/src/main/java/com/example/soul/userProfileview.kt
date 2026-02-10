//package com.example.soul
//
//import android.util.Log
//import androidx.lifecycle.ViewModel
//import androidx.lifecycle.viewModelScope
//import dagger.hilt.android.lifecycle.HiltViewModel
//import kotlinx.coroutines.Dispatchers
//import kotlinx.coroutines.flow.SharingStarted
//import kotlinx.coroutines.flow.stateIn
//import kotlinx.coroutines.launch
//import javax.inject.Inject
//
//
//@HiltViewModel
//class UserProfileViewModel @Inject constructor(private val profileRepo: UserProfileDataRepo):ViewModel(){
//
//    init {
//        Log.e("room", "ReportListViewModel CREATED")
//    }
//
//    // user state flow ka use karo kyuki flow to store hei nhi kar pata hai
//    val user = profileRepo.observeUser()
//        .stateIn(
//            viewModelScope,
//            SharingStarted.WhileSubscribed(5000),
//            null
//        )
//
//    fun logoutDeleteRoom(){
//        viewModelScope.launch(Dispatchers.IO){
//            profileRepo.logout()
//
//        }
//    }
//
//}
//
///* MANUALLY , kuch alag nhi hai bas stateIN() automaitcally sab kar deta hai
//private val _state = MutableStateFlow<ProfileEntity?>(null)
//val state: StateFlow<ProfileEntity?> = _state
//
//init{
//viewModelScope.launch {
//    profileRepo.observeUser().collect { value ->
//        _state.value = value
//    }
//    }
//}
// */
//
///*
//And that’s it.
//
//✔ कोई launch {} नहीं
//
//✔ कोई collect नहीं
//
//✔ कोई listener नहीं
//
//✔ कोई init{} नहीं
//
//stateIn() internally ये करता है:
// */