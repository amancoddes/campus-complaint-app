package com.example.demo.complaintApp

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.demo.complaintApp.ComplaintDataRoom.ComplaintEntity
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class UserAllComplaintsScreenViewModel @Inject constructor( private val repository:UserComplaintsReadRepository):ViewModel(){// esme val hatane ko kah raha hai


    val counts = combine(
        repository.pendingCountFlow,
        repository.resolvedCountFlow
    ) { pending, resolved ->
        HomeCounts(pending, resolved)
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), HomeCounts())


    private val _uiState = MutableStateFlow<HomeUiState>(
        HomeUiState.Loading
    )
    val uiState = _uiState.asStateFlow()

    private val _uiEvent = MutableSharedFlow<String>()
    val uiEvent = _uiEvent.asSharedFlow()

    fun retrySync() {
        syncOnce()

    }
    private var isSynced = false



    fun syncOnce() {
        Log.e("sync","sync return or not ")
      //  if (isSynced) return

        //isSynced = true
        Log.e("sync"," sync not return not ")

        viewModelScope.launch {
            _uiState.value = HomeUiState.Loading

            try {
                when (val result = repository.checkUidCompalints()) {

                    is ComplaintResultInList.Success -> {
                        _uiState.value = HomeUiState.Success

                    }

                    is ComplaintResultInList.Error -> {
                        _uiState.value = HomeUiState.Error(
                            message = result.message
                        )

                    }

                    ComplaintResultInList.NotFound -> {
                        _uiState.value = HomeUiState.Empty

                    }

                    ComplaintResultInList.Login -> {
                        _uiState.value = HomeUiState.NotLogin

                    }

                }

            } catch (e: Exception) {
                _uiState.value = HomeUiState.Error(e.message?:"something wrong")
                _uiEvent.emit("Something went wrong")
            }
        }

    }

    private val _selectedTab = MutableStateFlow(HomeTab.RECENT)
    val selectedTab = _selectedTab.asStateFlow()

    fun onTabChange(tab: HomeTab) {
        _selectedTab.value = tab
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    val pagingFlow: Flow<PagingData<ComplaintEntity>> =
        selectedTab.flatMapLatest { tab ->
            repository.getPagedComplaintsRepo(tab)
        }.cachedIn(viewModelScope)

}



sealed class HomeUiState {

    data object Loading : HomeUiState()

    data object Success : HomeUiState()

    data class Error(val message: String): HomeUiState()

    data object Empty : HomeUiState()

    data object NotLogin : HomeUiState()
}





data class HomeCounts(
    val pending: Int = 0,
    val resolved: Int = 0
)
