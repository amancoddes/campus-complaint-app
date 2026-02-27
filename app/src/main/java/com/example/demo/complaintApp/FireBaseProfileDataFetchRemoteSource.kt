package com.example.demo.complaintApp

import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.TimeoutCancellationException
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withTimeout
import javax.inject.Inject



interface  ProfileDataFetchRemoteSource{
    suspend fun userDataProfileFetch(uid: String):UserProfileData
}



sealed class UserProfileData {
    data class Success(val data:UserData) : UserProfileData()
    data object NotFound : UserProfileData()
    data class Error(val exception: Exception) : UserProfileData()
}


class FireBaseProfileDataFetchRemoteSource @Inject constructor( private val firebaseFirestore: FirebaseFirestore):ProfileDataFetchRemoteSource{
    override suspend fun userDataProfileFetch(uid:String):UserProfileData{
        return try {
            val result= withTimeout(10_000){
                firebaseFirestore.collection("users").document(uid).get().await().toObject(UserData::class.java)
            }
             result?.let {// solve smart typecasting case error
                UserProfileData.Success(it)
            } ?: UserProfileData.NotFound
        }
        catch (e:TimeoutCancellationException){
             return  UserProfileData.Error(Exception("network slow "))
        }
        catch (e:Exception){
           return UserProfileData.Error(e)
        }

    }
}
