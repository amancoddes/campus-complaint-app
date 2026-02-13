package com.example.soul

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import kotlinx.coroutines.TimeoutCancellationException
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withTimeout


sealed class ComplaintFetchResult2 {
    data class Success(val data: List<ComplaintDataRoom.ComplaintEntity>) : ComplaintFetchResult2()
    data object NotFound : ComplaintFetchResult2()
    data class Error(val exception: Exception) : ComplaintFetchResult2()
}



class ReportsRepoFirebase(private val firebase:FirebaseFirestore){

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

    suspend fun fetchTileKyesInside(
        hashes:String
    ): List<FirstAppFireStoreDataClass> {

        if (hashes.isEmpty()) return emptyList()

        val db = FirebaseFirestore.getInstance()

        val query = db.collection("complaints")
            .whereEqualTo("hash", hashes)                 // tile + title filter
            .whereEqualTo("status", "ACTIVE")       // only active
         //   .whereGreaterThan("timestamp", cutoffTime) // recent only
            .orderBy("timestamp", Query.Direction.DESCENDING)
            .limit(10)

        val snapshot = query.get().await()

        return snapshot.documents.mapNotNull { doc ->
            doc.toObject(FirstAppFireStoreDataClass::class.java)
        }
    }






    suspend fun fetchComplaint(uid:String):ComplaintFetchResult2{
        return try {
            // why in which send querySnapshot bacuse esme there we use qeries like whereEqualTo() to query ka answer querysnapshot
            val snapshot=firebase.collection("complaints").whereEqualTo("userId",uid).orderBy("timestamp",Query.Direction.DESCENDING)
                .get().await()

            val list2=snapshot.documents.mapNotNull { it.toObject(ComplaintDataRoom.ComplaintEntity::class.java) }
            if(list2.isEmpty()){
                ComplaintFetchResult2.NotFound
            }
            else{
                ComplaintFetchResult2.Success(list2)
            }
        }
        catch (e:Exception){
            ComplaintFetchResult2.Error(e)
        }

    }

    suspend fun fetchSingleComplaint(id: String): ComplaintFetchResult3 {

        return try {
            // documentsnapshot aayega kyki hum aak hie fetch hie karna chahte hai
            val doc = firebase.collection("complaints")
                .document(id)
                .get()
                .await()

            // Case 1: Document does NOT exist
            if (!doc.exists()) {
                return ComplaintFetchResult3.NotFound
            }

            // Case 2: Firestore → Entity conversion failed
            val data = doc.toObject(ComplaintDataRoom.ComplaintEntity::class.java)
                ?: return ComplaintFetchResult3.Error(
                    Exception("Data is null or mapping failed")
                )

            // Case 3: Document exists AND data is valid → Success
            ComplaintFetchResult3.Success(data)

        } catch (e: Exception) {
            ComplaintFetchResult3.Error(e)
        }
    }
}

sealed class ComplaintFetchResult3 {
    data class Success(val data: ComplaintDataRoom.ComplaintEntity) : ComplaintFetchResult3()
    data object NotFound : ComplaintFetchResult3()
    data class Error(val exception: Exception) : ComplaintFetchResult3()
}











