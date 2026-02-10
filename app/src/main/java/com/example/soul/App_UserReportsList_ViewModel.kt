package com.example.soul

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject


@HiltViewModel
class ReportListViewmodel @Inject constructor(private val repo:ReportsRepoRoom):ViewModel(){// esme val hatane ko kah raha hai

init {
    Log.e("heloVm", "ReportList VM CREATED -> $this")
}

    override fun onCleared() {
        Log.e("heloVm", "ReportList VM CLEARED -> $this")
    }



    val complaints = repo.observeUserComplaints()
        .stateIn(
                viewModelScope,
                SharingStarted.WhileSubscribed(5_000),
                emptyList()
            )




}

