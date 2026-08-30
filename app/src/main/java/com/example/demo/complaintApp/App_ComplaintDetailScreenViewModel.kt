package com.example.demo.complaintApp

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.time.delay
import javax.inject.Inject

@HiltViewModel
class ComplaintDetailViewModel @Inject constructor(
    private val repo: UserComplaintsReadRepository
) : ViewModel() {



    private val _snackBarEvent = MutableSharedFlow<String>()
    val snackBarEvent=_snackBarEvent.asSharedFlow()

    private val _complaint = MutableStateFlow<ComplaintDataRoom.ComplaintEntity?>(null)
    val complaint = _complaint

    private val _loading = MutableStateFlow(true)
    val loading = _loading
    private var lastRefreshTime = 0L
    private val refreshCooldown = 10_000L



    fun roomLoad(id: String){
        viewModelScope.launch {
            _complaint.value = repo.observeUserOneComplaints(id)
            _loading.value=false
        }

    }


    fun load(id: String) {

        viewModelScope.launch {

            _loading.value = true

            val currentTime = System.currentTimeMillis()

            if (currentTime - lastRefreshTime < refreshCooldown) {
                _loading.value = false
                _snackBarEvent.emit("Please wait before refreshing again after 10 seconds ⏳")
                return@launch

            }

            lastRefreshTime = currentTime



            // fetch latest from backend
            when (val result= repo.fetchNewComplaint(id)) {
                is RefreshResult.Error -> {
                    _snackBarEvent.emit(result.message)
                }

                RefreshResult.NotFound -> {
                    _snackBarEvent.emit("Complaint not found ❌")
                }

                RefreshResult.Success -> {
                    //
                }
            }
            // fetch updated data from room
            _complaint.value = repo.observeUserOneComplaints(id)

            _loading.value = false
        }
    }
}