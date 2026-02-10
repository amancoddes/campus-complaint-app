package com.example.soul

import android.location.Location
import android.location.LocationManager
import android.util.Log
import javax.inject.Inject


class LocationValidator @Inject constructor() {

    private var attempts = 0
    private var startTime = System.currentTimeMillis()
    private var finished = false

    fun reset() {
        attempts = 0
        startTime = System.currentTimeMillis()
        finished = false
    }

    fun validate(
        location: Location,
        accept: (Location,Boolean) -> Unit,
        retry: () -> Unit,
        showError: () -> Unit,
        inside:Boolean=false
    ) {
        if (finished) return

        attempts++
        val elapsed = System.currentTimeMillis() - startTime
        val indoor = EnvironmentDetector.isLikelyIndoor(location)

        var maxAccuracy = when(inside){
            true -> 80f
            false -> 18f
        }

//         = 18f
//       // val maxAccuracy = if (indoor) 80f else 18f

        // good fix (any time)
        if (attempts == 1 && location.accuracy <= 10f && !indoor) {
            Log.e("endLocation","perfect location ")
            accept(location,indoor)
            finished = true
            return
        }

        // acceptable after retries
        if (attempts >= 2 && location.accuracy <= maxAccuracy) {
            Log.e("endLocation"," by max accuracy")


            accept(location,indoor)
            finished = true
            return
        }

        //timeout fallback
        if (elapsed >= 5000) {
            Log.e("endLocation","complete by timeout")
            if (location.accuracy <= maxAccuracy) {
                accept(location,indoor)
                finished=true
                return
            } else {
                Log.e("endLocation","complete by timeout and error")
                showError()
                finished=true
                return
            }
            finished = true
            return
        }

        Log.e("endLocation","retry")
        retry()
    }
}
//
//object EnvironmentDetector {
//
//    fun isLikelyIndoor(location: Location): Boolean {
//        val weakAccuracy = location.accuracy > 50f
//        val veryWeakAccuracy = location.accuracy > 80f
//        val notMoving = location.hasSpeed() && location.speed < 0.5f
//        val lowSatellites = !location.hasBearing() // rough proxy for poor fix
//
//        return when {
//            veryWeakAccuracy -> true                 // almost surely indoor
//            weakAccuracy && notMoving -> true       // two weak signals
//            weakAccuracy && lowSatellites -> true
//            else -> false
//        }
//    }
//}



object EnvironmentDetector {

    fun isLikelyIndoor(location: Location): Boolean {
        val weakAccuracy = location.accuracy > 50f
        val veryWeakAccuracy = location.accuracy > 80f

        val notMoving = location.hasSpeed() && location.speed < 0.5f
        val noBearing = !location.hasBearing()

        val isNetwork = location.provider == LocationManager.NETWORK_PROVIDER
        val isStale = System.currentTimeMillis() - location.time > 10_000

        return when {
            veryWeakAccuracy -> true
            weakAccuracy && isNetwork -> true
            weakAccuracy && notMoving && noBearing -> true
            isStale && weakAccuracy -> true
            else -> false
        }
    }
}