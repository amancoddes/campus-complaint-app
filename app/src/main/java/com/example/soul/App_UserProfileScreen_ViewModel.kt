package com.example.soul

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class UserProfileViewModel @Inject constructor(private val profileRepo: UserProfileDataRepo):ViewModel(){

    init {
        Log.e("room", "ReportListViewModel CREATED")
    }

    val user = profileRepo.observeUser()
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            null
        )

    fun logoutDeleteRoom(){
        viewModelScope.launch(Dispatchers.IO){
            profileRepo.logout()

        }
    }

}
