//package com.example.demo.complaintApp
//
//import com.google.firebase.firestore.DocumentChange
//import com.google.firebase.firestore.FirebaseFirestore
//import com.google.firebase.firestore.ListenerRegistration
//import kotlinx.coroutines.CoroutineScope
//import kotlinx.coroutines.Dispatchers
//import kotlinx.coroutines.SupervisorJob
//import kotlinx.coroutines.flow.first
//import kotlinx.coroutines.launch
//import javax.inject.Inject
//import javax.inject.Singleton
//
//
//@Singleton
//class ListenerRepository @Inject constructor(
//    private val firebase: FirebaseFirestore,
//    private val dao: ComplaintDataRoom.ComplaintDao,
//    private val dataStoreManager: DataStoreManager
//) {
//
//    private var listener: ListenerRegistration? = null
//    private val scope= CoroutineScope(context = Dispatchers.IO + SupervisorJob())// SJ  isolate the failure so other coroutine can run when one coroutine fail
//    fun startListening(uid: String) {
//
//        if (listener != null) return
//
//        listener = firebase.collection("complaints")
//            .whereEqualTo("userId", uid)
//            .addSnapshotListener { snapshot, _ ->
//
//                snapshot?.documentChanges?.forEach { change ->
//
//                    val data = change.document
//                        .toObject(ComplaintDataRoom.ComplaintEntity::class.java)
//
//scope.launch {
//    when (change.type) {
//        DocumentChange.Type.ADDED -> dao.insertComplaint(data)//"Listener uses local cache and syncs with server"
//        DocumentChange.Type.MODIFIED -> dao.insertComplaint(data)
//        DocumentChange.Type.REMOVED -> dao.deleteById(id=data.id)
//    }
//}
//
//                }
//
//            }
//    }
//
//    fun stopListening() {
//        listener?.remove()
//        listener = null
//    }
//
//}