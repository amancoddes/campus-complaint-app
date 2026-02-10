package com.example.soul

import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.shareIn
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import javax.inject.Inject

class ReportsRepoRoom @Inject constructor (private val dao: ComplaintDataRoom.ComplaintDao,private val auth: FirebaseAuth,private val firebase: ReportsRepoFirebase,private val mutex: Mutex){


   suspend fun fetchTileKeys(hash:List<String>,cutoffTime: Long):List<FirstAppFireStoreDataClass>{
         return firebase.fetchHash(hash,cutoffTime)
    }

    suspend fun fetchInsideTileKeys(hash:String):List<FirstAppFireStoreDataClass>{
        return firebase.fetchTileKyesInside(hash)
    }



    private suspend fun currentUid(): String? =
        uidFlow.firstOrNull()
    init {
        Log.e("Repo", "ReportsRepoRoom created 🦋🦋☘️🦋🦋")
    }
    //  checkAndFetch()
    //checkUidCompalints()
    suspend fun checkUidCompalints() = mutex.withLock {// ye withLock{} block one by one run hoge or enke andar ke code bhi
        val userUid = currentUid() ?: return

        if (dao.countUserComplaints(userUid) == 0) {
            Log.e("room","room se fetch ☘️")
            when (val result = firebase.fetchComplaint(userUid)) {
                is ComplaintFetchResult2.NotFound -> {
                    Log.e("runroom", "check not found case")
                }
                is ComplaintFetchResult2.Error -> {
                    Log.e("runroom", "check method error ${result.exception.message}")
                }
                is ComplaintFetchResult2.Success -> {
                    dao.insertAll(result.data)
                }
            }
        }
    }





//    fun observeUserComplaints(userUid:String): Flow<List<ComplaintDataRoom.ComplaintEntity>> {
//        Log.e("room"," user id - > $userUid")
//        return dao.observeComplaints(userUid)
//    }

     suspend fun observeUserOneComplaints(id:String): ComplaintDataRoom.ComplaintEntity?{
        return dao.getComplaint(id)
    }

    suspend fun fetchNewComplaint(id: String) = mutex.withLock {// add update when make admin app
        when (val result = firebase.fetchSingleComplaint(id)) {
            is ComplaintFetchResult3.Success -> {
                dao.insertComplaint(result.data)
            }
            is ComplaintFetchResult3.Error -> {

            }
            is ComplaintFetchResult3.NotFound -> {

            }
        }

    }
    fun observeUserComplaints():  Flow<List<ComplaintDataRoom.ComplaintEntity>> =

        uidFlow
            .filterNotNull()
            .distinctUntilChanged()
            .flatMapLatest { userUid ->
                dao.observeComplaints(userUid) .distinctUntilChanged()
            }

    private val appScope = CoroutineScope(SupervisorJob() + Dispatchers.Default)

    val uidFlow: SharedFlow<String?> =
        callbackFlow {
            val listener = FirebaseAuth.AuthStateListener { firebase ->
                trySend(firebase.currentUser?.uid)
            }

            auth.addAuthStateListener(listener)

            awaitClose {
                auth.removeAuthStateListener(listener)
            }
        }
            .distinctUntilChanged()
            .shareIn(
                scope = appScope,
                started = SharingStarted.WhileSubscribed(5000),
                replay = 1
            )


}