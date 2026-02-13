package com.example.soul;

import android.app.Application;
import androidx.room.Room
import com.google.firebase.Firebase
import com.google.firebase.FirebaseApp;
import com.google.firebase.firestore.FirebaseFirestoreSettings
import com.google.firebase.firestore.MemoryCacheSettings
import com.google.firebase.firestore.firestore
import com.google.firebase.firestore.firestoreSettings
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