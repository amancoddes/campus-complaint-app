package com.example.demo.complaintApp
import com.google.firebase.firestore.DocumentChange
import android.util.Log
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.example.demo.complaintApp.ComplaintDataRoom.ComplaintEntity
import com.example.demo.complaintApp.di.HiltDependencies
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.shareIn
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.coroutines.withContext
import kotlinx.coroutines.withTimeoutOrNull
import javax.inject.Inject

class UserComplaintsReadRepository @Inject constructor (private val dao: ComplaintDataRoom.ComplaintDao
                                                        , private val auth: FirebaseAuth, private val firebase: ReportsRepoFirebase, private val mutex: Mutex,
                                                        private val dataStoreManager: DataStoreManager
) {


    suspend fun fetchComplaints(
        hash: List<String>,
        cutoffTime: Long
    ): Result<List<FirstAppFireStoreDataClass>> {
        return firebase.fetchComplaintFromBackend(hash, cutoffTime)
    }

    suspend fun fetchInsideTileKeys(hash: String): Result<List<FirstAppFireStoreDataClass>> {
        return firebase.fetchTileKyesInside(hash)
    }




// super base
    // web hook



    suspend fun checkUidCompalints():ComplaintResultInList= withContext(Dispatchers.IO) {// withContext use is there optional because firebase and room can support suspend concept
        mutex.withLock {
            val userUid = currentUid() ?: return@withLock ComplaintResultInList.Login
            Log.e("runs14","see 🌞🌞🌞🌞 run the main funciton ")

            val isEmpty = dao.countUserComplaints(userUid) == 0
            val lastTime = dataStoreManager.getLastSyncTime().first()
            val filterTime = if (isEmpty) null else lastTime
            return@withLock when (val result = firebase.fetchAllUserComplaints(userUid, filterTime)) {
                is ComplaintFetchResultInList.Success-> {
                    dao.insertAll(result.data)
                    ComplaintResultInList.Success
                }
                is ComplaintFetchResultInList.Error -> {
                    ComplaintResultInList.Error(result.error)
                }
                ComplaintFetchResultInList.NotFound -> {
                    ComplaintResultInList.NotFound
                }
            }
        }
    }

    val pendingCountFlow = dao.observePendingCount()
    val resolvedCountFlow = dao.observeResolvedCount()

    suspend fun observeUserOneComplaints(id: String): ComplaintEntity? {
        return dao.getComplaint(id)
    }

    suspend fun fetchNewComplaint(id: String): RefreshResult = mutex.withLock {// add update when make admin app
        when (val result = firebase.fetchSingleComplaint(id)) {
            is ComplaintFetchResult.Success -> {
                dao.insertComplaint(result.data)
                RefreshResult.Success
            }

            is ComplaintFetchResult.Error -> {

                RefreshResult.Error(result.exception.message ?: "Unknown error")
            }

            ComplaintFetchResult.NotFound -> {
                RefreshResult.NotFound
            }
        }

    }

    fun observeRecentComplaints(): Flow<List<ComplaintEntity>> {
        return dao.observeRecentComplaints()
    }

    private val appScope = CoroutineScope(SupervisorJob() + Dispatchers.Default)

    val uidFlow: Flow<String?> =
        callbackFlow {
            Log.e("callBack","run call back")
            class Hey:FirebaseAuth.AuthStateListener{
                override fun onAuthStateChanged(auth: FirebaseAuth) {
                    trySend(auth.currentUser?.uid)
                }
            }

            val listener=Hey()

//            val listener = FirebaseAuth.AuthStateListener { firebase ->
//                trySend(firebase.currentUser?.uid)
//            }
            auth.addAuthStateListener(listener)// add listener
            awaitClose {
                Log.e("callBack","remove listener")
                auth.removeAuthStateListener(listener)
            }
        }
            .distinctUntilChanged()
            .shareIn(// its use for stop multiple listener
                scope = appScope,
                started = SharingStarted.WhileSubscribed(0),
                replay = 1
            )
//     private suspend fun currentUid(): String? =
//        uidFlow.firstOrNull()

    private suspend fun currentUid(): String? =
        withTimeoutOrNull(4000) {
            uidFlow.filterNotNull().first()
        }

    fun getPagedComplaintsRepo(tab: HomeTab): Flow<PagingData<ComplaintEntity>> {

        val pagingSourceFactory = when (tab) {
            HomeTab.RECENT -> { { dao.getRecentComplaints() } }
            HomeTab.PENDING -> { { dao.getPendingComplaints() } }
            HomeTab.RESOLVED -> { { dao.getResolvedComplaints() } }
        }

        return Pager(
            config = PagingConfig(pageSize = 10,enablePlaceholders = false),
            pagingSourceFactory = pagingSourceFactory
        ).flow
    }











}


sealed class ComplaintResultInList {
    data object Success : ComplaintResultInList()
    data class Error(val message: String) : ComplaintResultInList()
    data object Login:ComplaintResultInList()
    data object NotFound:ComplaintResultInList()
}





sealed class RefreshResult {
    object Success : RefreshResult()
    object NotFound : RefreshResult()
    data class Error(val message: String) : RefreshResult()
}


