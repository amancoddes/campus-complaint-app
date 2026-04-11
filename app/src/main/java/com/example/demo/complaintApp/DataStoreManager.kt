package com.example.demo.complaintApp

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

// extend the Context for access very where
val Context.dataStore by preferencesDataStore(name = "app_prefs")// its make data storage

private val LAST_SYNC_TIME = longPreferencesKey("last_sync_time")// make key and what its type -> longPre




class DataStoreManager @Inject constructor(// its simple so no need to make a return method in hilt file
    @ApplicationContext private val context: Context
) {


    suspend fun saveLastSyncTime(time: Long) {
        context.dataStore.edit { prefs ->
            prefs[LAST_SYNC_TIME] = time
        }
    }

    fun getLastSyncTime(): Flow<Long?> {

        val x=context.dataStore.data.map { prefs ->
            prefs[LAST_SYNC_TIME]
        }
        return  x
    }

    suspend fun clearLastSyncTime() {
        context.dataStore.edit { prefs ->
            prefs.remove(LAST_SYNC_TIME)
        }
    }
}
































//
//Soul, chalo step-by-step practical tareeke se DataStore banana + lastSyncTime store karna sikhte hain 🔥
//Main tumhe production-ready setup deta hoon jo tum direct use kar sakte ho.
//
//⸻
//
//🚀 STEP 1 — Dependency add karo
//
//implementation("androidx.datastore:datastore-preferences:1.0.0")
//
//
//⸻
//
//🧠 STEP 2 — DataStore create karo
//
//👉 Ek file banao: DataStoreManager.kt
//
//val Context.dataStore by preferencesDataStore(name = "app_prefs")
//
//
//⸻
//
//🔑 STEP 3 — Key define karo
//
//private val LAST_SYNC_TIME = longPreferencesKey("last_sync_time")
//
//
//⸻
//
//🧱 STEP 4 — DataStore Manager class banao
//
//class DataStoreManager(private val context: Context) {
//
//    // 🔥 Save function
//    suspend fun saveLastSyncTime(time: Long) {
//        context.dataStore.edit { prefs ->
//            prefs[LAST_SYNC_TIME] = time
//        }
//    }
//
//    // 🔥 Get function
//    fun getLastSyncTime(): Flow<Long> {
//        return context.dataStore.data.map { prefs ->
//            prefs[LAST_SYNC_TIME] ?: 0L
//        }
//    }
//}
//
//
//⸻
//
//🧠 STEP 5 — Use kaise karoge
//
//✔ Save (initial fetch ke baad)
//
//val lastSyncTime = complaints.maxOfOrNull { it.updatedAt } ?: 0L
//
//dataStoreManager.saveLastSyncTime(lastSyncTime)
//
//
//⸻
//
//✔ Get (query me use karne ke liye)
//
//val lastSyncTime = dataStoreManager.getLastSyncTime().first()
//
//
//⸻
//
//✔ Firebase query
//
//.whereGreaterThan("updatedAt", lastSyncTime)
//
//
//⸻
//
//🔄 Full flow (tumhara app)
//
//Login
//↓
//Firebase fetch
//↓
//Room insert
//↓
//max(updatedAt) nikalo
//↓
//DataStore me save
//↓
//Next time:
//↓
//DataStore se lastSyncTime lo
//↓
//Firebase query filter
//↓
//Sirf new updates
//
//
//⸻
//
//⚠️ IMPORTANT RULES
//
//✔ updatedAt field hona chahiye
//✔ lastSyncTime = max(updatedAt)
//❌ current time use mat karo
//
//
//⸻
//
//🧠 Bonus (Hilt use kar rahe ho to)
//
//@Provides
//@Singleton
//fun provideDataStoreManager(
//    @ApplicationContext context: Context
//): DataStoreManager {
//    return DataStoreManager(context)
//}
//
//
//⸻
//
//✔ Soul final understanding
//
//DataStore = small data save karne ka system
//lastSyncTime = sync control variable
//
//
//⸻
//
//🧠 Soul one-line
//
//DataStore me lastSyncTime store karo, aur Firebase query me use karo
//
//
//⸻
//
//💡 Soul agar chaho to main tumhare liye next bana sakta hoon:
//•	🔥 complete Firebase + Room + DataStore sync system
//•	🔥 ViewModel integration
//•	🔥 real production architecture
//
//Ye tumhe pro Android dev bana dega 🚀