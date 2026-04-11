package com.example.demo.complaintApp

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.demo.complaintApp.ComplaintDataRoom.ComplaintEntity
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.time.LocalTime
import java.util.Calendar
import javax.inject.Inject


@HiltViewModel
class HomeScreenViewModel @Inject constructor(private val repository: UserComplaintsReadRepository,

): ViewModel() {


    init {

        Log.e("check"," Home screen view again create")
    }
    val complaintsUiState: StateFlow<ComplaintUiStates> =
        observeUserComplaints()
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5000),
                initialValue = ComplaintUiStates.Loading
            )

    @OptIn(ExperimentalCoroutinesApi::class)
   private fun observeUserComplaints(): Flow<ComplaintUiStates> =
        repository.uidFlow
            .distinctUntilChanged()// flat wants lambda returns flow<R> and map only wants R , so you have to manually return flowOf()
            .flatMapLatest { userUid ->// all extension function return but return type depend on there last expression return type ex: flatMapLatest depend map
                if(userUid==null) {
                    flowOf(ComplaintUiStates.NotLogin("not login"))// flowOf for flow create
                }
                else{
                    repository.observeRecentComplaints().map { list ->
                        if (list.isEmpty()) {
                            ComplaintUiStates.Empty// lambda return last line return decide when both if else condtions are
                        } else {
                            ComplaintUiStates.Success(list)
                        }
                    }.distinctUntilChanged()// its full compare not only compare success
                        .onStart {
                            emit(ComplaintUiStates.Loading)
                        }.catch { e ->
                            emit(ComplaintUiStates.Error(e.message ?: "Something went wrong"))// emit() only emit value in existing flow
                        }
                }
            }















    val counts = combine(
        repository.pendingCountFlow,
        repository.resolvedCountFlow
    ) { pending, resolved ->
        HomeCounts(pending, resolved)
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), HomeCounts())


    private val _uiState = MutableStateFlow<HomeUiState2>(
        HomeUiState2.Loading(greeting = provideGreeting())
    )
    val uiState = _uiState.asStateFlow()

    private val _uiEvent = MutableSharedFlow<String>()
    val uiEvent = _uiEvent.asSharedFlow()

    fun retrySync() {
        _uiState.value = HomeUiState2.Loading(greeting = provideGreeting())
        syncOnce()
    }



    private var isSynced = false
    fun syncOnce() {
        if (isSynced) return

        isSynced = true
        viewModelScope.launch {
           Log.e("check","its call firebase -> 🌞🌞 ")
            _uiState.value = HomeUiState2.Loading(greeting = provideGreeting())

            try {
                when (val result = repository.checkUidCompalints()) {

                    is ComplaintResultInList.Success -> {
                        _uiState.value = HomeUiState2.Success(greeting = provideGreeting())

                    }

                    is ComplaintResultInList.Error -> {
                        _uiState.value = HomeUiState2.Error(
                            greeting = provideGreeting(),
                            message = result.message
                        )

                    }

                    ComplaintResultInList.NotFound -> {
                        _uiState.value = HomeUiState2.Empty(greeting = provideGreeting())

                    }

                    ComplaintResultInList.Login -> {
                        _uiState.value = HomeUiState2.NotLogin(greeting = provideGreeting())

                    }

                }

            } catch (e: Exception) {
                _uiEvent.emit("Something went wrong")
            }
        }

    }

    private val _selectedTab = MutableStateFlow(HomeTab.RECENT)
    val selectedTab = _selectedTab.asStateFlow()

    fun onTabChange(tab: HomeTab) {
        _selectedTab.value = tab
    }





}

enum class HomeTab {
    RECENT, PENDING, RESOLVED
}


sealed class HomeUiState2 {

    data class Loading(val greeting: String): HomeUiState2()

    data class Success(val greeting: String): HomeUiState2()

    data class Error(val greeting: String,val message: String): HomeUiState2()

    data class Empty(val greeting: String) : HomeUiState2()

    data class NotLogin(val greeting: String) : HomeUiState2()
}


fun provideGreeting(): String {

    val hour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY)

    return when(hour) {
        in 0..11 -> "Good Morning"
        in 12..17 -> "Good Afternoon"
        else -> "Good Evening"
    }

}


sealed class ComplaintUiStates {
    data object Loading : ComplaintUiStates()
    data class Success(val data: List<ComplaintDataRoom.ComplaintEntity>) : ComplaintUiStates()
    data class Error(val message: String) : ComplaintUiStates()
    data object Empty : ComplaintUiStates()
    data class NotLogin(val message: String):ComplaintUiStates()
}

