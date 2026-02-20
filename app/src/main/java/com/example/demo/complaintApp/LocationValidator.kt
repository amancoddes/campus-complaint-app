package com.example.demo.complaintApp

import android.location.Location
import javax.inject.Inject


class LocationValidator @Inject constructor(
    private val currentTime : () -> Long
) {

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
        val elapsed = currentTime() - startTime
       // val indoor = EnvironmentDetector.isLikelyIndoor(location)

        var maxAccuracy = when(inside){
            true -> 80f
            false -> 18f
        }

//         = 18f
//       // val maxAccuracy = if (indoor) 80f else 18f

        // good fix (any time)
        if (attempts == 1 && location.accuracy <= 10f && !inside) {
            print("1 😚")
            accept(location,inside)
            finished = true
            return
        }

        // acceptable after retries
        if (attempts >= 2 && location.accuracy <= maxAccuracy) {
            print(" 2 🥸")
            accept(location,inside)
            finished = true
            return
        }

        //timeout fallback
        if (elapsed >= 5000) {
            print("last 😚")
            if (location.accuracy <= maxAccuracy) {
                accept(location,inside)
                finished=true
                return
            } else {
                print("show error locaiton grater than max ☺️")
                showError()
                finished=true
                return
            }
        }
print("see the attempts $attempts ☘️")
        retry()
    }
}


//
//object EnvironmentDetector {
//
//    fun isLikelyIndoor(location: Location): Boolean {
//        val weakAccuracy = location.accuracy > 50f
//        val veryWeakAccuracy = location.accuracy > 80f
//
//        val notMoving = location.hasSpeed() && location.speed < 0.5f
//        val noBearing = !location.hasBearing()
//
//        val isNetwork = location.provider == LocationManager.NETWORK_PROVIDER
//        val isStale = System.currentTimeMillis() - location.time > 10_000
//
//        return when {
//            veryWeakAccuracy -> true
//            weakAccuracy && isNetwork -> true
//            weakAccuracy && notMoving && noBearing -> true
//            isStale && weakAccuracy -> true
//            else -> false
//        }
//    }
//}