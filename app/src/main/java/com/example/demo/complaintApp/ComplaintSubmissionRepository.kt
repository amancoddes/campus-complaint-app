package com.example.demo.complaintApp


import androidx.paging.Pager
import androidx.paging.PagingConfig
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.TimeoutCancellationException
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withTimeout
import javax.inject.Inject

class ComplaintSubmissionRepository @Inject constructor(private val backendRepo:ComplaintSubmissionRemoteDataSource) {
    suspend fun sendComplain(
        data: FirstAppFireStoreDataClass
    ): Result<String> {
        return backendRepo.sendComplaint(data)
    }




// paging 3
//    fun getComplaintsPaging(): Pager<DocumentSnapshot, FirstAppFireStoreDataClass> {
//        return Pager(
//            config = PagingConfig(pageSize = 5),//prefetch
//            pagingSourceFactory = { FirestoreComplaintPagingSource(firebase) },
//        )
//    }
}

