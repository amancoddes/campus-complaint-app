package com.example.demo.complaintApp

import android.util.Log
import com.example.demo.complaintApp.di.HiltDependencies
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.coroutines.withContext
import javax.inject.Inject

class ProfileRepository @Inject constructor (
    private val dao: ProfileRoom.ProfileQueries,// pas proxy class object which implement the Dao interface internally
    private val fireRepo: ProfileDataFetchRemoteSource,
    private val mutex: Mutex,
    private val dao2: ComplaintDataRoom.ComplaintDao,
    private val repo: UserComplaintsReadRepository,
    @HiltDependencies.MainDispatcher val mainDispatcher:CoroutineDispatcher,
    @HiltDependencies.IoDispatcher val ioDispatcher: CoroutineDispatcher

){



    suspend fun fetchProfileData():UserProfileDataStateRepository=withContext(context = ioDispatcher) {
        val uid = currentUid() ?: return@withContext UserProfileDataStateRepository.Login
        mutex.withLock {
            dao.getUser(uid)
                ?: return@withLock when (val result = fireRepo.userDataProfileFetch(uid)) {
                is UserProfileData.Success -> {
                    dao.insertProfile(dataProfile = result.data.toEntity(uid))// toEntity{} crete entity object
                    UserProfileDataStateRepository.Success
                }

                is UserProfileData.NotFound -> {
                    UserProfileDataStateRepository.NotFound("user data not found add user data")
                }

                is UserProfileData.Error -> {
                    UserProfileDataStateRepository.Error(result.exception.message ?: "something wrong")
                }
            }
            return@withLock UserProfileDataStateRepository.Success// when room is not empty
        }

    }


    @OptIn(ExperimentalCoroutinesApi::class)
    fun observeUser(): Flow<ProfileFetchRoom> =
        repo.uidFlow
            .filterNotNull()
            .distinctUntilChanged()
            .flatMapLatest { uid ->
                dao.observeUser(uid)
                    .distinctUntilChanged()//Jab database mein koi change nahi hota //Room Flow zero CPU, zero battery, zero SQL runs
                    .map { list ->
                        if (list==null) {
                            Log.e("success34", "empty run")
                            ProfileFetchRoom.Empty
                        } else {
                            Log.e("success34", "success run")
                            ProfileFetchRoom.Success(list)
                        }
                    }
                    .onStart {
                        emit(ProfileFetchRoom.Loading)
                    }
                    .catch { e ->
                        Log.e("success34", "error run")
                        emit(ProfileFetchRoom.Error(e.message ?: "Something went wrong"))
                    }
            }


    private suspend fun currentUid(): String? =
        repo.uidFlow.firstOrNull()
/*
without .distinctUntilChanged

same par bhi bar bar run ho raha hai
observeUser("user123") → cancel
observeUser("user123") → restart
observeUser("user123") → restart


wapas call karne se data same hi milega,
but system extra kaam karega jo bilkul unnecessary hai.
 */

   suspend fun logout(){
       val uid = currentUid() ?: return
        dao2.deleteUserComplaints(uid =uid )
       dao.deleteUserProfileData(uid=uid)
    }




}

fun UserData.toEntity(uid: String): ProfileRoom.ProfileEntity {// create Room entity class
    return ProfileRoom.ProfileEntity(
        uid = uid,
        name = this.name,
        rollNo = this.rollNo,
        phone = this.phone,
        branch = this.branch
    )
}

sealed class UserProfileDataStateRepository {
    data class NotFound(val message: String) : UserProfileDataStateRepository()
    data object Success : UserProfileDataStateRepository()
    data class Error(val error: String) : UserProfileDataStateRepository()
    data object Login:UserProfileDataStateRepository()
}


sealed class ProfileFetchRoom {
    data object Loading : ProfileFetchRoom()
    data class Success(val data: ProfileRoom.ProfileEntity) : ProfileFetchRoom()
    data class Error(val message: String) : ProfileFetchRoom()
    data object Empty : ProfileFetchRoom()
}