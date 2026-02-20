package com.example.demo.complaintApp

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class UserProfileViewModel @Inject constructor(private val profileRepo: UserProfileDataRepo):ViewModel(){



    private val _profileFetching = MutableStateFlow<ProfileFetchState>(ProfileFetchState.Loading)




    fun fetchProfileAfterLoginAndSignUp(){
        Log.e("success34","run1 ")
        _profileFetching.value=ProfileFetchState.Loading
        Log.e("success34","run8 ")
        viewModelScope.launch {
            when(val result=profileRepo.checkAndFetch()){
                is UserProfileDataStateRepository.Error -> {
                    val message:String = result.error
                    _profileFetching.value=ProfileFetchState.Error(errorMessage = message)

                }
                is UserProfileDataStateRepository.NotFound -> {
                    _profileFetching.value=ProfileFetchState.NotFound("add complaint")
                }
                is UserProfileDataStateRepository.Success -> {
                    _profileFetching.value=ProfileFetchState.Success
                    Log.e("success34","run ")
                }
                UserProfileDataStateRepository.Login -> {
                    _profileFetching.value=ProfileFetchState.Login
                }
            }
        }

    }








    init {
        fetchProfileAfterLoginAndSignUp()
    }

    val user = profileRepo.observeUser()
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            ProfileFetchRoom.Loading
        )







    fun logoutDeleteRoom(){
        viewModelScope.launch(Dispatchers.IO){
            profileRepo.logout()

        }
    }



    val uiState: StateFlow< CombineProfileFetchState> = combine(_profileFetching,user) { fetching, room ->

        when{
            fetching is ProfileFetchState.Loading ->{
               CombineProfileFetchState.Loading
            }
            fetching is ProfileFetchState.Error  ->{
               CombineProfileFetchState.Error(errorMessage = fetching.errorMessage)
            }
            fetching is ProfileFetchState.Login -> {
               CombineProfileFetchState.Login
            }
            room is ProfileFetchRoom.Empty ->{
               CombineProfileFetchState.Empty(message = "user data not available")
            }
            room is ProfileFetchRoom.Success -> {
                Log.e("success34", "success ")
               CombineProfileFetchState.Success(data = room.data)
            }

            else -> {
               CombineProfileFetchState.Loading
            }
        }

    }.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000),
       CombineProfileFetchState.Loading
    )




}



sealed class CombineProfileFetchState {
    data object Loading : CombineProfileFetchState()
    data class Empty(val message: String) : CombineProfileFetchState()
    data class Success(val data: ProfileRoom.ProfileEntity) : CombineProfileFetchState()
    data class Error(val errorMessage:String) : CombineProfileFetchState()
    data object Login:CombineProfileFetchState()
}



sealed class ProfileFetchState {
    data object Loading : ProfileFetchState()
    data class NotFound(val message: String) : ProfileFetchState()
    data object Success: ProfileFetchState()
    data class Error(val errorMessage:String) : ProfileFetchState()
    data object Login:ProfileFetchState()
}



