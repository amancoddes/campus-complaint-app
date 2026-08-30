package com.example.demo.complaintApp


import android.net.Uri
import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class ComplaintSubmissionRepository @Inject constructor(private val backendRepo:ComplaintSubmissionRemoteDataSource) {
    suspend fun sendComplain(
        data: FirstAppFireStoreDataClass
    ): Result<String> {
        return backendRepo.sendComplaint(data)
    }


    private val storageRef =// its give the ref of starting point not give any folder ref
        FirebaseStorage.getInstance().reference// add in hilt
    val userId = FirebaseAuth.getInstance().currentUser?.uid
    suspend fun uploadImage(uri: Uri): Result<String> {
        return try {
            val fileName = "complaintImages/$userId/${System.currentTimeMillis()}.jpg"

            Log.e("there1","  -> $userId")
            val imageRef = storageRef.child(fileName)



            Log.e("there1"," going upload")
            // upload

            Log.e(
                "URI_CHECK",
                uri.toString()
            )
            imageRef.putFile(uri).await()
            Log.e("there1"," after upload")





            // url

            val downloadUrl = imageRef.downloadUrl.await()
            Log.e("there1"," download url $downloadUrl")

            Result.success(downloadUrl.toString())

        } catch (e: Exception) {
            Log.e("there1","${e.message}")
            Result.failure(e)
        }
    }


// paging 3
//    fun getComplaintsPaging(): Pager<DocumentSnapshot, FirstAppFireStoreDataClass> {
//        return Pager(
//            config = PagingConfig(pageSize = 5),//prefetch
//            pagingSourceFactory = { FirestoreComplaintPagingSource(firebase) },
//        )
//    }
}

