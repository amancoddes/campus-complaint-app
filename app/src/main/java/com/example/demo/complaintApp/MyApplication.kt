package com.example.demo.complaintApp

import android.app.Application
import com.google.firebase.Firebase
import com.google.firebase.FirebaseApp;
import com.google.firebase.firestore.FirebaseFirestoreSettings
import com.google.firebase.firestore.MemoryCacheSettings
import com.google.firebase.firestore.firestore
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class MyApplication2:Application(){

    override fun onCreate() {
        super.onCreate()
        FirebaseApp.initializeApp(this)
        val settings = FirebaseFirestoreSettings.Builder()
            .setLocalCacheSettings(MemoryCacheSettings.newBuilder().build()) // In-Memory cache only
            .build()
        Firebase.firestore.firestoreSettings = settings
    }
}