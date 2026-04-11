package com.example.demo.complaintApp

import android.util.Log
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class SessionManager @Inject constructor(
    private val repository: ListenerRepository
) {

    fun onLogin(uid: String) {
        Log.e("listener","listener start from the session manager")
        repository.startListening(uid)
    }

    fun onLogout() {
        Log.e("listener","listener stop from the session manager")
        repository.stopListening()
    }
}