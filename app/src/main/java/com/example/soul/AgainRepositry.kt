//package com.example.soul
//
//import com.google.firebase.auth.FirebaseAuth
//import com.google.firebase.auth.FirebaseUser
//import kotlinx.coroutines.channels.awaitClose
//import kotlinx.coroutines.flow.Flow
//import kotlinx.coroutines.flow.callbackFlow
//
//class AgainRepositry(){
//
//
//    private val auth:FirebaseAuth=FirebaseAuth.getInstance()
//
//    fun signUp(email:String,password:String):Flow<Result<FirebaseUser?>> = callbackFlow {
//        auth.createUserWithEmailAndPassword(email,password).addOnCompleteListener { task->
//            if (task.isSuccessful){
//                trySend(Result.success(task.result.user))
//            }
//            else{
//                trySend(Result.failure(task.exception?:Exception("signup is failed")))
//            }
//        }
//        awaitClose {  }// opt
//    }
//
//    fun login(email:String,password: String):Flow<Result<FirebaseUser?>> = callbackFlow {
//        auth.signInWithEmailAndPassword(email,password).addOnCompleteListener { task->
//            if(task.isSuccessful){
//                trySend(Result.success(task.result.user))
//            }
//            else{
//                trySend(Result.failure(task.exception?:Exception("login is failed")))
//            }
//        }
//        awaitClose {  }
//    }
//
//    fun logout(){
//        auth.signOut()
//    }
//
//    fun observeAuth():Flow<FirebaseUser?> = callbackFlow {
///*
//Agar interface me sirf ek abstract method ho, to lambda use karna zyada convenient hai.
//Agar multiple methods ho ya extra logic chahiye → anonymous object use karo.
// */
//        val listener=object :FirebaseAuth.AuthStateListener{
//            override fun onAuthStateChanged(p0: FirebaseAuth) {
//                trySend(auth.currentUser)
//            }
//        }
//        auth.addAuthStateListener(listener)// its register this refernce in the fireBaseAuth list ,than FirebaseAuth run automatically when state is changed (currentUser)
//        awaitClose{auth.removeAuthStateListener(listener) }
//    }
//
//}