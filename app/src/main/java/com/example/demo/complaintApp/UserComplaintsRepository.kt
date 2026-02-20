package com.example.demo.complaintApp

import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.shareIn
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.coroutines.withContext
import javax.inject.Inject

class UserComplaintsReadRepository @Inject constructor (private val dao: ComplaintDataRoom.ComplaintDao
,private val auth: FirebaseAuth,private val firebase: ReportsRepoFirebase,private val mutex: Mutex) {


    suspend fun fetchComplaints(
        hash: List<String>,
        cutoffTime: Long
    ): Result<List<FirstAppFireStoreDataClass>> {
        return firebase.fetchComplaintFromBackend(hash, cutoffTime)
    }

    suspend fun fetchInsideTileKeys(hash: String): Result<List<FirstAppFireStoreDataClass>> {
        return firebase.fetchTileKyesInside(hash)
    }


    private suspend fun currentUid(): String? =
        uidFlow.firstOrNull()


    suspend fun checkUidCompalints(): ComplaintResultInList = withContext(Dispatchers.IO) {// withContext use is there optional because firebase and room can support suspend concept
        mutex.withLock {
            Log.e("success34", "run repo ")
            val userUid = currentUid() ?: return@withLock ComplaintResultInList.Login
            if (dao.countUserComplaints(userUid) > 0) {
                Log.e("success34", "run after check2 ")
                return@withLock ComplaintResultInList.Success
            }
            Log.e("success34", "run after check ")

            return@withLock when (val result = firebase.fetchAllUserComplaints(userUid)) {
                is ComplaintFetchResultInList.Success -> {
                    dao.insertAll(result.data)
                    ComplaintResultInList.Success
                }

                is ComplaintFetchResultInList.Error -> {
                    ComplaintResultInList.Error(message = result.error)
                }

                ComplaintFetchResultInList.NotFound -> {
                    ComplaintResultInList.NotFound
                }


            }

        }

    }

    suspend fun observeUserOneComplaints(id: String): ComplaintDataRoom.ComplaintEntity? {
        return dao.getComplaint(id)
    }

    suspend fun fetchNewComplaint(id: String) = mutex.withLock {// add update when make admin app
        when (val result = firebase.fetchSingleComplaint(id)) {
            is ComplaintFetchResult.Success -> {
                dao.insertComplaint(result.data)
            }

            is ComplaintFetchResult.Error -> {

            }

            is ComplaintFetchResult.NotFound -> {

            }
        }

    }

    @OptIn(ExperimentalCoroutinesApi::class)
    fun observeUserComplaints(): Flow<ComplaintUiStates> =
        uidFlow
            .filterNotNull()
            .distinctUntilChanged()
            .flatMapLatest { userUid ->
                dao.observeComplaints(userUid).distinctUntilChanged()
                    .map { list ->
                        if (list.isEmpty()) {
                            Log.e("success34", "empty run")
                            ComplaintUiStates.Empty
                        } else {
                            Log.e("success34", "success run")
                            ComplaintUiStates.Success(list)
                        }
                    }
                    .onStart {
                        emit(ComplaintUiStates.Loading)
                    }
                    .catch { e ->
                        Log.e("success34", "error run")
                        emit(ComplaintUiStates.Error(e.message ?: "Something went wrong"))
                    }
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

sealed class ComplaintUiStates {
    data object Loading : ComplaintUiStates()
    data class Success(val data: List<ComplaintDataRoom.ComplaintEntity>) : ComplaintUiStates()
    data class Error(val message: String) : ComplaintUiStates()
    data object Empty : ComplaintUiStates()
}


sealed class ComplaintResultInList {
    data object Success : ComplaintResultInList()
    data class Error(val message: String) : ComplaintResultInList()
    data object Login:ComplaintResultInList()
    data object NotFetch:ComplaintResultInList()
    data object NotFound:ComplaintResultInList()
}