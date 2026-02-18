package com.example.soul

import android.util.Log
import androidx.compose.runtime.collectAsState
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class ReportListViewmodel @Inject constructor(private val repo:ReportsRepoRoom, private val userRepoComplint:ReportsRepoRoom):ViewModel(){// esme val hatane ko kah raha hai



    private val _userDataScreenState= MutableStateFlow<ComplaintSyncState>(ComplaintSyncState.Idle)

    init {
        fetchUserDataAfterLoginAndSignUp()
    }


    fun fetchUserDataAfterLoginAndSignUp(){
        Log.e("success34","run1 ")
        _userDataScreenState.value=ComplaintSyncState.Loading
        Log.e("success34","run8 ")
        viewModelScope.launch {
            when(val result=userRepoComplint.checkUidCompalints()){
                is ComplaintResultInList.Error -> {
                    val message:String = result.message
                    _userDataScreenState.value=ComplaintSyncState.Error(message)

                }
                is ComplaintResultInList.NotFound -> {
                    _userDataScreenState.value=ComplaintSyncState.NotFound("add complaint")
                }
                is ComplaintResultInList.Success -> {
                    _userDataScreenState.value=ComplaintSyncState.Success
                    Log.e("success34","run ")
                }
                ComplaintResultInList.Login -> {
                    _userDataScreenState.value=ComplaintSyncState.Login
                }
                ComplaintResultInList.NotFetch -> ComplaintSyncState.NotFetch
            }
        }

    }



    val complaints =
        repo.observeUserComplaints()
            .stateIn(
                viewModelScope,
                SharingStarted.WhileSubscribed(5000),
                ComplaintUiStates.Loading
            )




    val uiState: StateFlow<CombineState> = combine(_userDataScreenState,complaints) { fetching ,room ->

        when{
            fetching is ComplaintSyncState.Loading ->{
                CombineState.Loading
            }
            fetching is ComplaintSyncState.Error  ->{
                CombineState.Error(message = fetching.message )
            }
            fetching is ComplaintSyncState.Login -> {
                CombineState.Login
            }
            room is ComplaintUiStates.Empty ->{
                CombineState.Empty(message = "empty complaints list")
            }
            room is ComplaintUiStates.Success -> {
                Log.e("success34", "success ")
                CombineState.Success(data = room.data)
            }

            else -> {
                CombineState.Loading
            }
        }

    }.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        CombineState.Loading
    )




}

sealed class ComplaintSyncState {
    data object Idle : ComplaintSyncState()
    data object Loading:ComplaintSyncState()
    data class NotFound(val message: String) : ComplaintSyncState()
    data object Success: ComplaintSyncState()
    data class Error(val message: String) : ComplaintSyncState()
    data object Login:ComplaintSyncState()
    data object NotFetch:ComplaintSyncState()
}




sealed class CombineState {
    data object Loading:CombineState()
    data class Empty(val message: String) : CombineState()
    data class Success(val data: List<ComplaintDataRoom.ComplaintEntity>) : CombineState()
    data class Error(val message: String) : CombineState()
    data object Login:CombineState()
}












