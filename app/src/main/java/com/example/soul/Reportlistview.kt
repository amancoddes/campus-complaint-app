//package com.example.soul
//
//import android.util.Log
//import androidx.lifecycle.ViewModel
//import androidx.lifecycle.viewModelScope
//import dagger.hilt.android.lifecycle.HiltViewModel
//import kotlinx.coroutines.flow.SharingStarted
//import kotlinx.coroutines.flow.stateIn
//import javax.inject.Inject
//
//
//@HiltViewModel
//class ReportListViewmodel @Inject constructor(private val repo:ReportsRepoRoom):ViewModel(){// esme val hatane ko kah raha hai
///*
//IDE कभी-कभी इसे गलत समझ लेता है और सोचता है कि:
//
//“repo property को class में कहीं use नहीं कर रहे।”, just ignore khud thik ho jayega
// */
//init {
//    Log.e("heloVm", "ReportList VM CREATED -> $this")
//}
//
//    override fun onCleared() {
//        Log.e("heloVm", "ReportList VM CLEARED -> $this")
//    }
//
//
//
//    val complaints = repo.observeUserComplaints()
//        .stateIn(
//            viewModelScope,
//            SharingStarted.WhileSubscribed(5_000),
//            emptyList()
//        )
//
//
//
//
//}
//
//
////
////
////@HiltViewModel
////class ReportListViewmodel @Inject constructor(
////    private val repo: ReportsRepoRoom
////) : ViewModel() {
////
////    val complaints =
////        repo.uidFlow
////            .filterNotNull()                      // user MUST exist
////            .flatMapLatest { uid ->               //  UID change → reload
////                repo.observeUserComplaints(uid)
////            }
////            .stateIn(
////                viewModelScope,
////                SharingStarted.WhileSubscribed(5_000),
////                emptyList()
////            )
////}