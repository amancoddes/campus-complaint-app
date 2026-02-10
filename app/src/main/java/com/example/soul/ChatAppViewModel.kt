package com.example.soul

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ChatAppViewModel(private val repository: ChatAppRepoInterfaceImplementation):ViewModel(){

// first text field
    private val _message=MutableStateFlow<String>("")
    val message=_message.asStateFlow()

    fun messageInput(input:String){
        _message.value=input
    }

    private val _messageEdit=MutableStateFlow<String>("")
    val messageEdit=_messageEdit.asStateFlow()

    fun messageInputEdit(input:String){
        _messageEdit.value=input
    }


    // send message
    private val _messageSend=MutableStateFlow<Result<String>?>(null)
    val messageSend=_messageSend.asStateFlow()

    fun sendMessage(input: String){

        when (input) {
            "" -> println("its invalid")
            " " -> println("its invalid also")
            else -> viewModelScope.launch(Dispatchers.IO) {
                repository.sendMessages(input).collect{ result->
                    _messageSend.value=result
                }
            }
        }
        _message.value=""
    }



    // data from the backend
    private val _messageDataFromDataBase=MutableStateFlow<Result<List<Map<String,Any>>>?>(null)
    val messageDataFromDataBase=_messageDataFromDataBase.asStateFlow()

    init {
        loadMessages()
    }
    private fun loadMessages(){
        viewModelScope.launch(Dispatchers.IO) {
            repository.messageFromDatabase().collect{
                result-> _messageDataFromDataBase.value=result
            }
        }
    }

    // delete message
    fun deleteMessage(docId2:String){
        viewModelScope.launch {
            repository.deleteMessage(docId = docId2)
        }
        topbarChangeCancel()
    }



// edit karo message ko
    fun editMessage45(docId2: String,editMessage:String){
        viewModelScope.launch {
            repository.editMessage(docId2,editMessage)
        }
    _messageEdit.value=""
        topbarChangeCancel()
    startEditCancel()

    }

    // topbar change
    private val _carpClick=MutableStateFlow<String?>(null)
    val carpClick=_carpClick.asStateFlow()

    fun topbarChange(change:String){
        _carpClick.value=change
    }
    fun topbarChangeCancel(){
        _carpClick.value=null
    }
// Edit karna ke liye
    private val _startEdit=MutableStateFlow<String?>(null)
    val startEdit=_startEdit.asStateFlow()

     fun editStart(id:String){
         _startEdit.value=id
     }
    private fun startEditCancel(){
        _startEdit.value=null
    }


}