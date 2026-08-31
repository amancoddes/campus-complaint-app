package com.example.demo.complaintApp
import android.util.Log
import com.google.firebase.Timestamp
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.Source
import com.google.type.Date
import kotlinx.coroutines.TimeoutCancellationException
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withTimeout
import javax.inject.Inject

sealed class ComplaintFetchResultInList {
    data class Success(val data: List<ComplaintDataRoom.ComplaintEntity>) : ComplaintFetchResultInList()
    data object NotFound : ComplaintFetchResultInList()
    data class Error(val error:String) : ComplaintFetchResultInList()
}





class ReportsRepoFirebase @Inject constructor(val firebase:FirebaseFirestore,val dataStoreObj:DataStoreManager){
// outside complaints
    suspend fun fetchComplaintFromBackend(
        hashes: List<String>,
        cutoffTime: Long
    ): Result<List<FirstAppFireStoreDataClass> >{

        if (hashes.isEmpty()) return Result.success(emptyList())

        return try {

val result = withTimeout(10_000){



    val snapshot = firebase.collection("complaints")
        .whereGreaterThan("timestamp", cutoffTime) // recent only
        .whereEqualTo("status", "ACTIVE")       // only active
        .whereIn("hash", hashes)                 // tile + title filter
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

            val query = firebase.collection("complaints")// there firebase sdk check current user get its token and send with request than firebase check the token and validate
                .whereEqualTo("status", "ACTIVE")       // only active
                .whereEqualTo("hash", hashes)                 // tile + title filter
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
    suspend fun fetchAllUserComplaints(uid:String,lastSyncTime:Long?):ComplaintFetchResultInList{
        return try {
            Log.e("runs14","see 🌞🌞🌞🌞 remote call ")

            val complaint = withTimeout(10_000){


                val baseQuery = firebase.collection("complaints")
                    .whereEqualTo("userId", uid)

                val query = if (lastSyncTime != null) {

                    val safeTime = lastSyncTime - 5 * 60 * 1000
                    val safeTimestamp = Timestamp(java.util.Date(safeTime))
                    baseQuery
                        .whereGreaterThan("updatedTime", safeTimestamp)
                        .orderBy("updatedTime", Query.Direction.DESCENDING)

                } else {
                    baseQuery
                        .orderBy("updatedTime", Query.Direction.DESCENDING)
                }

                val snapshot = query.get(Source.SERVER).await()


                snapshot.documents.mapNotNull { doc ->
                    val data = doc.toObject(FirstAppFireStoreDataClass::class.java)
                    data?.let {
                        ComplaintDataRoom.ComplaintEntity(
                            id = it.id,
                            complain = it.complain,
                            description = it.description,
                            timestamp = it.timestamp?.toDate()?.time ?: 0L,
                            address = it.address,
                            status = it.status,
                            userId = it.userId,
                            updatedTime = it.updatedTime?.toDate()?.time ?: 0L,
                            url = it.imageUrl,
                            resolvedImageUrl = it.resolvedImageUrl
                        )
                    }
                }

            }
        if (complaint.isEmpty()){
            Log.e("runs14","see 🌞🌞🌞🌞 Not Found ")
              return  ComplaintFetchResultInList.NotFound
            }

            val latestTime = complaint.maxOfOrNull { it.updatedTime } ?: 0L
            dataStoreObj.saveLastSyncTime(time = latestTime)
            Log.e("runs14","see 🌞🌞🌞🌞 success remote ")
            return ComplaintFetchResultInList.Success(complaint)
        }
        catch (e:TimeoutCancellationException){
            Log.e("runs14","see 🌞🌞🌞🌞 error timeout remote ")
            ComplaintFetchResultInList.Error(error="network slow try again")
        }
        catch (e:Exception){
            Log.e("runs14","see 🌞🌞🌞🌞 error remote  ")
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
                  val data=  doc.toObject(FirstAppFireStoreDataClass::class.java)
                    data?.let {
                        ComplaintDataRoom.ComplaintEntity(
                            id = it.id,
                            complain = it.complain,
                            description = it.description,
                            timestamp = it.timestamp?.toDate()?.time ?: 0L,
                            address = it.address,
                            status = it.status,
                            userId = it.userId,
                            updatedTime = it.updatedTime?.toDate()?.time ?: 0L,
                            url = it.imageUrl,
                            resolvedImageUrl = it.resolvedImageUrl
                        )
                    }
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





// test 3 to see git show head
// test 4 to se git head ~ 1 HEAD