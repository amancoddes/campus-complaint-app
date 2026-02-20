package com.example.soul

import android.util.Log
import kotlinx.coroutines.tasks.await

// Paging 3
import androidx.paging.PagingSource
import androidx.paging.PagingState

// Firebase Firestore
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.Query

// Your Model
import com.google.firebase.firestore.Source
import kotlinx.coroutines.withTimeout

class FirestoreComplaintPagingSource(
    private val firebase: FirebaseFirestore
) : PagingSource<DocumentSnapshot, FirstAppFireStoreDataClass>() {

    override fun getRefreshKey(
        state: PagingState<DocumentSnapshot, FirstAppFireStoreDataClass>
    ) = null

    override suspend fun load(
        params: LoadParams<DocumentSnapshot>
    ): LoadResult<DocumentSnapshot, FirstAppFireStoreDataClass> {
        Log.d("PagingSource", "load called with params: $params")
        return try {
            val currentPage = params.key
            val pageSize = params.loadSize



            var query = firebase.collection("complaints")
                .whereEqualTo("status", "Approved")
                .orderBy("timestamp", Query.Direction.DESCENDING)
                .limit(pageSize.toLong())

            currentPage?.let {x->
                query = query.startAfter(x)
            }


            val snapshot = withTimeout(10000) {     // 5 second timeout
                query.get(Source.SERVER).await()
            }

            val complaints = snapshot.toObjects(FirstAppFireStoreDataClass::class.java)
            val nextKey = if (snapshot.size() < pageSize) null else snapshot.documents.last()
            val x=snapshot.metadata.isFromCache
            Log.e("offline","$x")
            LoadResult.Page(
                data = complaints,
                prevKey = null,
                nextKey = nextKey//nextKey = snapshot.documents.last()
            )

        } catch (e: Exception) {
            Log.e("FirestorePaging", "Paging Error 76876876989879: ${e.message}")

            LoadResult.Error(e)
        }
    }
}


