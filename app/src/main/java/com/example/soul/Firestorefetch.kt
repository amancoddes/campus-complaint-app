package com.example.soul

// Kotlin Coroutines + Firebase extensions
import android.util.Log
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.LoadState
import androidx.paging.Pager
import androidx.paging.PagingConfig
import kotlinx.coroutines.tasks.await

// Paging 3
import androidx.paging.PagingSource
import androidx.paging.PagingState
import androidx.paging.cachedIn
import androidx.paging.compose.collectAsLazyPagingItems

// Firebase Firestore
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.Query

// Your Model
import com.example.soul.FirstAppFireStoreDataClass
import com.google.firebase.firestore.Source
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.withTimeout
import javax.inject.Inject


class FirestoreComplaintPagingSource(
    private val firebase: FirebaseFirestore
) : PagingSource<DocumentSnapshot, FirstAppFireStoreDataClass>() {

    override fun getRefreshKey(// eskko likhna jaruri hai kyuki ye abstract fucniton ka mehtod hai , esko pager internally use karta hai or es data aak specific time jo jab refresh ya others kuch cases mei interannly esko run karta hai or pramas.key mei sighn kar deta hai
        state: PagingState<DocumentSnapshot, FirstAppFireStoreDataClass>
    ) = null

    override suspend fun load(// we pass DocumentSnapshot beacause in firebase not use indexes so we pass last snapshot
        params: LoadParams<DocumentSnapshot>// LoadParams ke subclass ke obejct pass jisme key hote hai konsa obejct hum abhi pass kar rahe , agar REFRESH  hai to key = null, agar appenf = last key  value , agar prepend to jo humne retrun keya usme mei ka previw key value or , aak case jab user retry ya refresh kare to usem REFRESH ka hie obejct pass hot ahai or useme key nul hota hai
    ): LoadResult<DocumentSnapshot, FirstAppFireStoreDataClass> {
        Log.d("PagingSource", "load called with params: $params")
        return try {
            val currentPage = params.key
            val pageSize = params.loadSize



//// filter added // status or tamestamp ka index bana na padega firebase mei
            var query = firebase.collection("complaints")
                .whereEqualTo("status", "Approved")
                .orderBy("timestamp", Query.Direction.DESCENDING)
                .limit(pageSize.toLong())

            currentPage?.let {x->
                query = query.startAfter(x)
            }


            /* document
            {
  "complain": "Water leakage",
  "address": "Hostel Road",
  "timestamp": 1732500000000,
  "status": "Approved"
}
             */

/*
its a copy of one document
DocumentSnapshot {
    id = "abc123"// id add kar deta hai yaha par
    data = { complain = "...", address = "...", status = "...", timestamp = ... }
    metadata = {...}
}
 */


////////////3-
           //QueryShapshot
           // documents: List<DocumentSnapshot>
           // size: Int
           // metadata: { fromCache, hasPendingWrites }
            val snapshot = withTimeout(10000) {     // 5 second timeout
                query.get(Source.SERVER).await()
            }// its give Query snapshot which have list of DocumentSnapshot and it is copy of document ,

            val complaints = snapshot.toObjects(FirstAppFireStoreDataClass::class.java)// QuerySnapshot ke all documents ko data class ke obejct mei convert karta hai or aak list mei store kar deta hai

            // phir esme check hoga ki kya abhi jo send hua hai vo kam tha kya limit se agar hai to send null kyuki ab send karne ke liye nhi hai , khyuki ab document.last kyu send kare jab or hai hie nhi
            val nextKey = if (snapshot.size() < pageSize) null else snapshot.documents.last()// esme aa conditon lagai hai agar firestore mei hie agar pageSize jo la rahe or utna hie firestore mei nhi hua to fetch na ho

            val x=snapshot.metadata.isFromCache// its check ki firestore ke offline jaha store kiya tha waha se aa raha hai kya
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


//
//
//class ComplaintRepository @Inject constructor(
//    private val firebase: FirebaseFirestore
//) {
//
//    fun getComplaintsPaging(): Pager<DocumentSnapshot, FirstAppFireStoreDataClass> {
//        return Pager(
//            config = PagingConfig(pageSize = 10),
//            pagingSourceFactory = { FirestoreComplaintPagingSource(firebase) }
//        )
//    }
//}
//
//
//
//@HiltViewModel
//class HomeViewModel @Inject constructor(
//    private val repo: ComplaintRepository
//) : ViewModel() {
//
//    val complaintPagingFlow =
//        repo.getComplaintsPaging()
//            .flow
//            .cachedIn(viewModelScope)
//}

//
//@Composable
//fun ComplaintListScreen(viewModel: HomeViewModel) {
//
//    val pagingData = viewModel.complaintPagingFlow.collectAsLazyPagingItems()
//
//    LazyColumn {
//        items(pagingData.itemCount) { index ->
//            val item = pagingData[index]
//            if (item != null) {
//                Text(item.complain)
//            }
//        }
//
//        pagingData.apply {
//            when {
//                loadState.refresh is LoadState.Loading -> item { CircularProgressIndicator() }
//                loadState.append is LoadState.Loading -> item { CircularProgressIndicator() }
//                loadState.refresh is LoadState.Error -> item { Text("Error loading!") }
//            }
//        }
//    }
//}