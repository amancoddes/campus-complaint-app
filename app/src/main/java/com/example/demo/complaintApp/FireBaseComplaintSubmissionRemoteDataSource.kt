package com.example.demo.complaintApp

import com.google.firebase.auth.FirebaseAuth
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
                val complaintData = addComplaintId(userId = userId, idComplaint = generatedId, data = data)
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

    private fun addComplaintId(userId:String, idComplaint:String, data: FirstAppFireStoreDataClass):FirstAppFireStoreDataClass{
        return data.copy(id = idComplaint, userId = userId)
    }
}

