package com.example.soul

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class UserDataRepo @Inject constructor(val firestore: FirebaseFirestore,private val auth: FirebaseAuth){

suspend fun sendUserData(data: UserData):Result<Unit>{
    val uid=auth.currentUser?.uid ?: return Result.failure(Exception("User not logged in"))
//seee there
    return try {
        firestore.collection("users").document(uid).set(data).await()

        Result.success(Unit)
    }
    catch (e:Exception){
        Result.failure(e)
    }
}

}

