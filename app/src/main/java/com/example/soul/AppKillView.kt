package com.example.soul

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.StateFlow

class AppView(private val handle:SavedStateHandle):ViewModel(){


    companion object{
        private const val  KEYNAME="name"
        private const val  KEYPASSWORD="Password"
    }


    val drift:StateFlow<String> = handle.getStateFlow(KEYNAME,"")
    val password:StateFlow<String> = handle.getStateFlow(KEYPASSWORD,"")





    fun update(input:String){
        handle[KEYNAME]=input
    }

    fun updatePassword(input: String){
        handle[KEYPASSWORD]=input
    }

}

