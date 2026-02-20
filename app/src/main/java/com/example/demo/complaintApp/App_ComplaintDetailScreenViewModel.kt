package com.example.soul

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ComplaintDetailViewModel @Inject constructor(
    private val repo: ReportsRepoRoom
) : ViewModel() {
    init {
        Log.e("VM", "ReportList VM CREATED -> $this")
    }

    override fun onCleared() {
        Log.e("VM", "ReportList VM CLEARED -> $this")
    }

    private val _complaint = MutableStateFlow<ComplaintDataRoom.ComplaintEntity?>(null)
    val complaint = _complaint

    private val _loading = MutableStateFlow(true)
    val loading = _loading

    fun load(id: String) {
        viewModelScope.launch {
            _loading.value = true
Log.e("run2", "load run ")
            // Fetch from Firestore (latest)
            repo.fetchNewComplaint(id)

            //  Now read the updated value from Room
            _complaint.value = repo.observeUserOneComplaints(id)

            _loading.value = false
        }
    }
}