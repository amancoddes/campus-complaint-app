package com.example.demo.complaintApp

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject


@HiltViewModel
class HomeScreenViewModel @Inject constructor(private val repository: ComplaintSubmissionRepository,

): ViewModel(){




    // search in top bar
    private val search= MutableStateFlow("")
    val searchToCompose = search.asStateFlow()

    fun updateSerchValue(input:String){
        search.value=input
    }

//
//
//    val complaintPagingFlow=
//        repository.getComplaintsPaging()
//            .flow// trigger internally code which code start flow code which observe and emit .load() return data , which call by any pager class method
//            .cachedIn(viewModelScope)


}
