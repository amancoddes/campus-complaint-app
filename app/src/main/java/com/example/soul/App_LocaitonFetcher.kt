package com.example.soul

import android.annotation.SuppressLint
import android.content.Context
import android.location.Location
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.google.android.gms.tasks.CancellationTokenSource
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class LocationFetcher @Inject constructor(
    @ApplicationContext private val context: Context
) {
//Context
    private val client =
        LocationServices.getFusedLocationProviderClient(context)//proxy class  obejct it can be the run the IPC implementaiton code

    private var cts: CancellationTokenSource? = null

    @SuppressLint("MissingPermission") // permission UI layer ne ensure ki hai
    fun fetch(
        onResult: (Location?) -> Unit,
        onError: (Throwable) -> Unit
    ) {
        cts?.cancel()
        cts = CancellationTokenSource()

        client.getCurrentLocation(
            Priority.PRIORITY_HIGH_ACCURACY,
            cts!!.token
        )
            .addOnSuccessListener { onResult(it) }
            .addOnFailureListener { onError(it) }
    }

    fun cancel() {
        cts?.cancel()
    }
}


/*
FusedLocationProviderClient (FLP):
	•	khud background thread me GPS/network work karta hai
	•	aur callback always Main thread par deliver karta hai.
 */