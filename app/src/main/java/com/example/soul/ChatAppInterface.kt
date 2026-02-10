package com.example.soul

import android.os.Message
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow


interface ChatAppInterface{
   abstract fun sendMessages(message:String):Flow<Result<String>>
   abstract fun messageFromDatabase():Flow<Result<List<Map<String,Any>>>>
   abstract suspend fun editMessage(docId:String,editMessage: String):Result<Unit>
   abstract suspend fun deleteMessage(docId: String):Result<Unit>
}