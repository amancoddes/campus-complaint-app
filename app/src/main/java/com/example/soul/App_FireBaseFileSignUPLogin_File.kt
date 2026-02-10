package com.example.soul

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class RepositoryDemo @Inject constructor(val auth:FirebaseAuth,val firestore:FirebaseFirestore){

   suspend fun singnUpRepo(email:String,password:String):Result<FirebaseUser?>{
       return try {

           auth.createUserWithEmailAndPassword(email,password).await()// create user in firebase
           val user=auth.currentUser

           user?.sendEmailVerification()?.await()// send email link

           Result.success(user)
       }
       catch (e:Exception){
           Result.failure(e)
       }

    }

    suspend fun isEmailVerified(): Boolean {
        auth.currentUser?.reload()?.await()
        return auth.currentUser?.isEmailVerified == true
    }



    suspend fun login(email: String, password: String): Result<FirebaseUser?> {
        return try {
            auth.signInWithEmailAndPassword(email, password).await()
            Result.success(auth.currentUser)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}