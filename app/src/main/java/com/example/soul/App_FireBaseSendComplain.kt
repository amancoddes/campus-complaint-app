package com.example.soul

import androidx.paging.Pager
import androidx.paging.PagingConfig
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.TimeoutCancellationException
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withTimeout
import javax.inject.Inject


//,private val storage: FirebaseStorage
//,image:Uri
class FirstAppModuleRepository @Inject constructor(private val firebase:FirebaseFirestore,private val auth:FirebaseAuth){
    suspend fun sendComplain(data: FirstAppFireStoreDataClass): Result<String>{
        return try {
            withTimeout(10_000) {
                val userId = auth.currentUser?.uid ?: "anonymous"
                val docRef = firebase.collection("complaints").document()
                val generatedId = docRef.id
                val complaintData = data.copy(
                    userId = userId,
                    id = generatedId
                )
                //document = Firestore new record reference
                //.id = add record primary key
                docRef.set(complaintData)// set() jo id hai usme saved the data
                    // colleciton mei data add karega
                    .await()// ye code excicutaiton rok kar rakhe ga agar kamm nhii hua ho to
// ye kuch return nhi karte agar error aya to catch block run hoga or agar sab aache se hogaya to //  Result.success("its sucess ") ye run hoga or return hoga
                Result.success(docRef.id)
            }
        }catch (e: TimeoutCancellationException){
            Result.failure(Exception("Timeout! Check internet connection"))
        }
        catch (e: Exception) {
            Result.failure(e)
        }

    }
//


    ///
    //
    //
    fun getComplaintsPaging(): Pager<DocumentSnapshot, FirstAppFireStoreDataClass> {
        return Pager(
            config = PagingConfig(pageSize = 5),//prefetch
            pagingSourceFactory = { FirestoreComplaintPagingSource(firebase) },
        )
    }



}










//