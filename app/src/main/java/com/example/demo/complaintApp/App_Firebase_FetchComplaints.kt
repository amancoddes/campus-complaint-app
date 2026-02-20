package com.example.demo.complaintApp

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import kotlinx.coroutines.TimeoutCancellationException
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withTimeout
import javax.inject.Inject


sealed class ComplaintFetchResultInList {
    data class Success(val data: List<ComplaintDataRoom.ComplaintEntity>) : ComplaintFetchResultInList()
    data object NotFound : ComplaintFetchResultInList()
    data class Error(val error:String) : ComplaintFetchResultInList()
}



class ReportsRepoFirebase @Inject constructor(private val firebase:FirebaseFirestore){
// outside complaints
    suspend fun fetchComplaintFromBackend(
        hashes: List<String>,
        cutoffTime: Long
    ): Result<List<FirstAppFireStoreDataClass> >{

        if (hashes.isEmpty()) return Result.success(emptyList())

        return try {

val result = withTimeout(10_000){

    val db = FirebaseFirestore.getInstance()
    val snapshot = db.collection("complaints")
        .whereIn("hash", hashes)                 // tile + title filter
        .whereEqualTo("status", "ACTIVE")       // only active
        .whereGreaterThan("timestamp", cutoffTime) // recent only
        .orderBy("timestamp", Query.Direction.DESCENDING)
        .limit(10).get().await()


    snapshot.documents.mapNotNull { doc ->
        doc.toObject(FirstAppFireStoreDataClass::class.java)
    }
}

             return Result.success(result)
        }
        catch (e:TimeoutCancellationException){
            Result.failure(Exception("Network is slow. Please try again."))
        }
        catch (e:Exception){
            Result.failure(e)
        }


    }

// whereln() ye list leta hai
// for fetch inside complaints from backend
    suspend fun fetchTileKyesInside(
        hashes:String
    ):Result< List<FirstAppFireStoreDataClass>> {
    if (hashes.isEmpty()) return Result.success(emptyList())
    return try {
        val result= withTimeout(10_000){
            val db = FirebaseFirestore.getInstance()
            val query = db.collection("complaints")
                .whereEqualTo("hash", hashes)                 // tile + title filter
                .whereEqualTo("status", "ACTIVE")       // only active
                //   .whereGreaterThan("timestamp", cutoffTime) // recent only
                .orderBy("timestamp", Query.Direction.DESCENDING)
                .limit(10)

            val snapshot = query.get().await()

            snapshot.documents.mapNotNull { doc ->
                doc.toObject(FirstAppFireStoreDataClass::class.java)
            }
        }
        return Result.success(result)
    }
    catch (e:TimeoutCancellationException){
        Result.failure(Exception("network slow try again "))
    }
    catch (e:Exception){
        Result.failure(e)
    }
}




// fetching all complaint of a user its run when user login
    suspend fun fetchAllUserComplaints(uid:String):ComplaintFetchResultInList{
        return try {
            val complaint = withTimeout(10_000){
                val snapshot=firebase.collection("complaints").whereEqualTo("userId",uid).orderBy("timestamp",Query.Direction.DESCENDING)
                    .get().await()

                snapshot.documents.mapNotNull { it.toObject(ComplaintDataRoom.ComplaintEntity::class.java) }
            }
        if (complaint.isEmpty()){
              return  ComplaintFetchResultInList.NotFound
            }
            return ComplaintFetchResultInList.Success(complaint)
        }
        catch (e:TimeoutCancellationException){
            ComplaintFetchResultInList.Error(error="network slow try again")
        }
        catch (e:Exception){
            ComplaintFetchResultInList.Error(e.message?:"something wrong try again ")
        }

    }

    // after send complaint its run and fetch the send complaint for store in room
    suspend fun fetchSingleComplaint(id: String): ComplaintFetchResult {

        return try {
            val result= withTimeout(10_000){
                val doc = firebase.collection("complaints")
                    .document(id)
                    .get()
                    .await()
                if (!doc.exists()) {
                    null
                } else {
                    doc.toObject(ComplaintDataRoom.ComplaintEntity::class.java)
                }
            }
            return result?.let {// let give grantee it is non null
                ComplaintFetchResult.Success(it)
            } ?: ComplaintFetchResult.NotFound
        }catch (e:TimeoutCancellationException){
            ComplaintFetchResult.Error(Exception("network slow try again"))
        }
        catch (e: Exception) {
            ComplaintFetchResult.Error(e)
        }
    }
}

sealed class ComplaintFetchResult {
    data class Success(val data: ComplaintDataRoom.ComplaintEntity) : ComplaintFetchResult()
    data object NotFound : ComplaintFetchResult()
    data class Error(val exception: Exception) : ComplaintFetchResult()
}











