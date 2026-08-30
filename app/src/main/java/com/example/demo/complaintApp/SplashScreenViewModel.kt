package com.example.demo.complaintApp

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withTimeout
import javax.inject.Inject


@HiltViewModel
class SplashScreenViewModel @Inject constructor(
    private val auth: FirebaseAuth,
) : ViewModel() {

    private val _startDestination = MutableStateFlow<String?>(null)
    val startDestination = _startDestination.asStateFlow()

    init {
        checkUser()
    }

    private fun checkUser() {
        viewModelScope.launch {
            try {
                withTimeout(2000){
                    auth.currentUser?.reload()?.await()
                }
            } catch(_: Exception) {
                print(" exception on splash view")
            }

            val user = auth.currentUser
            when {
                user == null -> {
                    _startDestination.value = AuthScreens.Login_Screen.route
                }  // not logged in

                user.isEmailVerified ->{

                    Log.e("listener"," listener start ")
                    _startDestination.value = "main_Graph"
                }

                else -> {
                    _startDestination.value=AuthScreens.Auth_Verify.route
                }  // logged-in but not verified
            }
        }


    }
}