package com.example.demo.complaintApp

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class UserData(
    val name:String = "",
    val rollNo:String="",
    val phone:String="",
    val branch:String=""
)

@HiltViewModel
class ProfileSetupScreenViewModel @Inject constructor(private val repo: UserRepository):ViewModel(){

    private val _uiData =mutableStateOf(UserData())
    val uiData:State<UserData> =_uiData

    fun update(set:SetEnum,input:String){
        _uiData.value= when(set){
            SetEnum.Name -> _uiData.value.copy(name=input)
            SetEnum.Phone -> _uiData.value.copy(phone = input)
            SetEnum.RollNo-> _uiData.value.copy(rollNo = input)
            SetEnum.Branch-> _uiData.value.copy(branch = input)
        }
    }

private val _state=MutableStateFlow<SaveState>(SaveState.Idle)
    val state = _state.asStateFlow()
    fun sendData(){
        viewModelScope.launch {
            _state.value=SaveState.Loading
            val result= repo.sendUserData(data = _uiData.value)


            result.fold(
                onSuccess = {
                    _state.value=SaveState.Success

                    // check and call the firebase and fetch user data
                   // profileRepo.checkAndFetch()
                },
                onFailure = { msg->
                    _state.value=SaveState.Error(msg.message?:"unknow error")
                }
            )
        }
    }
}



enum class SetEnum{
    Name,
    RollNo,
    Phone,
    Branch
}

sealed class SaveState {
    data object Idle : SaveState()
    data object Loading : SaveState()
    data object Success : SaveState()
    data class Error(val msg: String) : SaveState()
}