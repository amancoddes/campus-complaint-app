package com.example.demo.complaintApp

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.TimeoutCancellationException
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withTimeout
import javax.inject.Inject


interface ComplaintSubmissionRemoteDataSource{
    suspend  fun sendComplaint(data: FirstAppFireStoreDataClass):Result<String>
}
// send complaint to backend repo
class FireBaseComplaintSubmissionRemoteDataSource @Inject constructor(private val firebase: FirebaseFirestore, private val auth: FirebaseAuth
):ComplaintSubmissionRemoteDataSource{
    override suspend fun sendComplaint(data: FirstAppFireStoreDataClass): Result<String> {
        val userId = auth.currentUser?.uid
            ?: return Result.failure(Exception("User not logged in"))
        return try {
            withTimeout(10_000) {
                val docRef = firebase.collection("complaints").document()
                val generatedId = docRef.id
                val complaintData = addComplaintMap(userId = userId, idComplaint = generatedId, data = data)
                docRef.set(complaintData)
                    .await()//stop the code execution
                Result.success(docRef.id)
            }
        }catch (e: TimeoutCancellationException){
            Result.failure(Exception("Timeout! Check internet connection"))
        }
        catch (e: Exception) {
            Result.failure(e)
        }
    }
    private fun addComplaintMap(
        userId: String,
        idComplaint: String,
        data: FirstAppFireStoreDataClass
    ): HashMap<String, Any?> {

        val map = hashMapOf(
            "id" to idComplaint,
            "userId" to userId,
            "complain" to data.complain,
            "description" to data.description,
            "address" to data.address,
            "status" to data.status,
            "latitude" to data.latitude,
            "longitude" to data.longitude,
            "hash" to data.hash,
            "accuracy" to data.accuracy,
            "confidence" to data.confidence.name,
            "mode" to data.mode.name,
            "numberOfPeoples" to data.numberOfPeoples,
            "timestamp" to FieldValue.serverTimestamp(),
            "updatedTime" to FieldValue.serverTimestamp(),
            "imageUrl" to data.imageUrl,
            "resolvedImageUrl" to data.resolvedImageUrl
        )
        return map
    }
}

