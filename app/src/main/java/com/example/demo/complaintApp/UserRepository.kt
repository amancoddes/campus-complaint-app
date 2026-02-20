package com.example.demo.complaintApp

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class UserRepository @Inject constructor(val firestore: FirebaseFirestore,private val auth: FirebaseAuth){

suspend fun sendUserData(data: UserData):Result<Unit>{
    val uid=auth.currentUser?.uid ?: return Result.failure(Exception("User not logged in"))

    return try {
        firestore.collection("users").document(uid).set(data).await()

        Result.success(Unit)
    }
    catch (e:Exception){
        Result.failure(e)
    }
}

}

