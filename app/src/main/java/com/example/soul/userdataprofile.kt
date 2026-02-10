//package com.example.soul
//
//import com.google.firebase.firestore.FirebaseFirestore
//import kotlinx.coroutines.tasks.await
//import javax.inject.Inject
//
//
///*
//.toObject(...) isliye exist karta है —
//
//taaki Firebase se aaye JSON-like map ko tumhare data class me convert kar de.
//
// */
//class UserProfileDataFirebaseRepository @Inject constructor( private val firebaseFirestore: FirebaseFirestore){
//
//
//    suspend fun userDataProfileFetch(uid:String):UserResponse{
//
//        return  try {
//            val data = firebaseFirestore.collection("users").document(uid).get().await().toObject(UserData::class.java)
//
//            if (data==null){
//                return UserResponse.NotFound("user not found")
//            }
//            else{
//                return UserResponse.Success(data = data)
//            }
//        }
//        catch (e:Exception){
//            UserResponse.Error(e)
//        }
//
//
//    }
//}
//
//sealed class UserResponse {
//    data class Success(val data: UserData) : UserResponse()
//    data class NotFound(val message: String) : UserResponse()
//    data class Error(val exception: Exception) : UserResponse()
//}
//
///*
// WHY sealed class ProfileRepository ke लिए बनाते हैं
//
//→ ViewModel के लिए नहीं?
//
//Because:
//
//✔ FirebaseRepository → direct data नहीं देता
//
//✔ FirebaseRepository → सिर्फ “result” देता है
//
//(success / not found / error)
//
//✔ ProfileRepository ko ye result पढ़कर decision लेना होता है
//
//Yani sealed class का main consumer:
//
//❌ ViewModel nahi
//
//✔ ProfileRepository hai
// */