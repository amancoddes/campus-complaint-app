package com.example.demo.soul

import android.util.Log
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner


class MyObserver : DefaultLifecycleObserver {


    override fun onStart(owner: LifecycleOwner) {
        Log.d("LifecycleObserver", "Activity started")
    }

    override fun onResume(owner: LifecycleOwner) {
        super.onResume(owner)

        Log.d("LifecycleObserver", "Activity Resume")
    }

    override fun onPause(owner: LifecycleOwner) {
        super.onPause(owner)

        Log.d("LifecycleObserver", "Activity Pause")
    }
    override fun onStop(owner: LifecycleOwner) {
        Log.d("LifecycleObserver", "Activity stopped")
    }

    override fun onDestroy(owner: LifecycleOwner) {
        super.onDestroy(owner)
        Log.d("LifecycleObserver","Activity Destroy")
    }
}