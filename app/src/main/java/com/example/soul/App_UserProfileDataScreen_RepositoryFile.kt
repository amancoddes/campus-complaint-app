package com.example.soul

import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import javax.inject.Inject

class UserProfileDataRepo @Inject constructor (private val dao:ProfileRoom.ProfileQueries,private val auth:FirebaseAuth,private val fireRepo:UserProfileDataFirebaseRepository,private val mutex: Mutex,private val dao2: ComplaintDataRoom.ComplaintDao
,private val repo:ReportsRepoRoom){

    fun observeUser(): Flow<ProfileRoom.ProfileEntity?> =
        repo.uidFlow
            .filterNotNull()
            .distinctUntilChanged()
            .flatMapLatest { uid ->
                dao.observeUser(uid)
                    .distinctUntilChanged()//Jab database mein koi change nahi hota
                //Room Flow zero CPU, zero battery, zero SQL runs
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


    suspend fun checkAndFetch()=mutex.withLock{
        val uid = currentUid() ?: return
        val local = dao.getUser(uid)
        if (local==null){
            when(val result= fireRepo.userDataProfileFetch(uid)){
                //
                is UserResponse.Success->{
                    dao.insertProfile(dataProfile = result.data.toEntity(uid))
                }
                is UserResponse.NotFound->{

                }
                is UserResponse.Error->{

                }
            }
        }

    }




}

fun UserData.toEntity(uid: String): ProfileRoom.ProfileEntity {
    return ProfileRoom.ProfileEntity(
        uid = uid,
        name = this.name,
        rollNo = this.rollNo,
        phone = this.phone,
        branch = this.branch
    )
}