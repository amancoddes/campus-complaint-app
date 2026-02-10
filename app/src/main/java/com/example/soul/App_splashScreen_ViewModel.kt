package com.example.soul

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
class SplashViewModel @Inject constructor(
    private val auth: FirebaseAuth
) : ViewModel() {

    private val _startDestination = MutableStateFlow<String?>(null)
    val startDestination = _startDestination.asStateFlow()

    init {
        Log.e("splashView"," init of view splash ")
        checkUser()
    }

    private fun checkUser() {



        viewModelScope.launch {
            try {
                withTimeout(2000){
                    auth.currentUser?.reload()?.await()
                }
            } catch(e: Exception) {
            }

            val user = auth.currentUser
            Log.e("splashView","check() run ")
            _startDestination.value = when {
                user == null -> AuthScreens.Login_Screen.route  // not logged in

                user.isEmailVerified -> "main_Graph"  // logged-in + verified

                else -> AuthScreens.Auth_Verify.route  // logged-in but not verified
            }
        }


    }
}