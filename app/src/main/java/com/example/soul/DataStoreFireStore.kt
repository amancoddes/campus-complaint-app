//package com.example.soul
//
//import android.widget.Toast
//import androidx.compose.foundation.background
//import androidx.compose.foundation.layout.Arrangement
//import androidx.compose.foundation.layout.Column
//import androidx.compose.foundation.layout.Row
//import androidx.compose.foundation.layout.fillMaxSize
//import androidx.compose.foundation.layout.fillMaxWidth
//import androidx.compose.foundation.layout.padding
//import androidx.compose.foundation.shape.RoundedCornerShape
//import androidx.compose.material3.Button
//import androidx.compose.material3.Card
//import androidx.compose.material3.Checkbox
//import androidx.compose.material3.OutlinedTextField
//import androidx.compose.material3.Text
//import androidx.compose.material3.TextField
//import androidx.compose.runtime.Composable
//import androidx.compose.runtime.collectAsState
//import androidx.compose.runtime.getValue
//import androidx.compose.runtime.mutableStateOf
//import androidx.compose.runtime.remember
//import androidx.compose.runtime.setValue
//import androidx.compose.ui.Alignment
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.graphics.Brush
//import androidx.compose.ui.graphics.Color
//import androidx.compose.ui.platform.LocalContext
//import androidx.compose.ui.platform.LocalFocusManager
//import androidx.compose.ui.platform.LocalSoftwareKeyboardController
//import androidx.compose.ui.text.font.FontWeight
//import androidx.compose.ui.unit.dp
//import androidx.lifecycle.ViewModel
//import androidx.lifecycle.viewModelScope
//import androidx.lifecycle.viewmodel.compose.viewModel
//import com.google.firebase.firestore.FirebaseFirestore
//import com.google.firebase.firestore.Query
//import kotlinx.coroutines.Dispatchers
//import kotlinx.coroutines.channels.awaitClose
//import kotlinx.coroutines.flow.Flow
//import kotlinx.coroutines.flow.MutableStateFlow
//import kotlinx.coroutines.flow.asStateFlow
//import kotlinx.coroutines.flow.callbackFlow
//import kotlinx.coroutines.launch
//
//class DataRepository {
//    private val db = FirebaseFirestore.getInstance()
//
//    // Add Data with timestamp
//    fun addData(userName: String, message: String): Flow<Result<String>?> = callbackFlow {
//        data class UserData(
//            val userName: String = "",
//            val message: String = "",
//            val time: Long = System.currentTimeMillis() // 🔑 timestamp field
//        )
//
//        val obj = UserData(userName, message)
//        db.collection("messages").add(obj)
//            .addOnSuccessListener { result ->
//                trySend(Result.success(result.id))
//            }
//            .addOnFailureListener { e ->
//                trySend(Result.failure(e))
//            }
//
//        awaitClose { }
//    }
//
//    // Load Data in ascending order of "time"
//    /*
//    	•	Query basically ek “instructions set” hai ki kaunse documents chahiye aur kaise sort karne hai.
//	•	Ye abhi tak data fetch nahi karta, sirf prepare karta hai query ko.
//
//⸻
//
//Step 2: .addSnapshotListener { snapshot, error -> ... }
//	•	Query class me .addSnapshotListener() method hota hai.
//	•	Ye real-time listener attach karta hai. Jab bhi database me matching documents change hote hain, ye listener invoke hota hai.
//     */
//    fun messageLoad(): Flow<Result<List<Map<String, Any>>>> = callbackFlow {
//        val subscription = db.collection("messages")
//            .orderBy("time", Query.Direction.ASCENDING)
//            .addSnapshotListener { snapshot, error ->
//                if (error != null) {
//                    trySend(Result.failure(error))
//                    return@addSnapshotListener
//                }
//
//                val list = snapshot?.documents?.map {
//                    val data = it.data ?: emptyMap()
//                    data + ("id" to it.id)
//                } ?: emptyList()
//
//                trySend(Result.success(list))
//            }
//
//        awaitClose { subscription.remove() }
//    }
//    fun updateRepo(input: String, output: String): Flow<Result<Unit>> = callbackFlow {
//        db.collection("messages").document(input)
//            .update("message", output)
//            .addOnSuccessListener { trySend(Result.success(Unit)) }
//            .addOnFailureListener { e -> trySend(Result.failure(e)) }
//        awaitClose {}
//    }
//
//    fun delete(input: String): Flow<Result<Unit>> = callbackFlow {
//        db.collection("messages").document(input)
//            .delete()
//            .addOnSuccessListener { trySend(Result.success(Unit)) }
//            .addOnFailureListener { e -> trySend(Result.failure(e)) }
//        awaitClose {}
//    }
//}
//
//
//
//
//class DataViewModel(private val repo:DataRepository=DataRepository()):ViewModel() {
//    private val _dataAdd = MutableStateFlow<Result<String>?>(null)
//    val dataAdd = _dataAdd.asStateFlow()
//
//    fun addData(name: String, message: String) {
//        viewModelScope.launch(Dispatchers.IO) {
//            repo.addData(name, message).collect { result ->
//                _dataAdd.value = result
//            }
//        }
//        showMessages()
//    }
//
//    private val _userName = MutableStateFlow<String>("")
//    val userName = _userName.asStateFlow()
//
//    fun setUserName(name: String) {
//        _userName.value = name
//    }
//
//    private val _userMessage = MutableStateFlow<String>("")
//    val userMessage = _userMessage.asStateFlow()
//
//    fun setUserMessage(message: String) {
//        _userMessage.value = message
//    }
//
//
//    private val _listOfMessage = MutableStateFlow<Result<List<Map<String, Any>>>?>(null)
//    val listOfMessage = _listOfMessage.asStateFlow()
//    private fun showMessages(){
//        viewModelScope.launch(Dispatchers.IO) {
//            repo.messageLoad().collect { result ->
//                _listOfMessage.value = result
//            }
//        }
//
//    }
//    init {
//        showMessages()
//    }
//
//
//
//
//        fun update(input: String, output: String) {
//            viewModelScope.launch(Dispatchers.IO) {
//                repo.updateRepo(input, output).collect { result ->
//                    when {
//                        result.isSuccess -> {
//                            println("success updated")
//                        }
//                        result.isFailure->{
//                            println("excepiton ")
//                        }
//                    }
//                }
//            }
//            showMessages()
//        }
//
//    fun delete(input: String) {
//        viewModelScope.launch(Dispatchers.IO) {
//            repo.delete(input).collect{
//                result->
//                when{
//                    result.isSuccess->{
//                        print("delete complete")
//                    }
//                    result.isFailure -> {
//                        print("exception ${result.exceptionOrNull()}")
//                    }
//                }
//            }
//        }
//        showMessages()
//    }
//}
//@Composable
//fun MessageApp(){
//    val context= LocalContext.current
//
//
//    val focus= LocalFocusManager.current
//    val keyboard= LocalSoftwareKeyboardController.current
//    val view:DataViewModel= viewModel()
//    val userName by view.userName.collectAsState()
//    val userMessage by view.userMessage.collectAsState()
//    val dataUser by view.dataAdd.collectAsState()
//    val dataMessage by view.listOfMessage.collectAsState()
//
//    Column(modifier = Modifier
//        .fillMaxSize()
//        .background(
//            brush = Brush.verticalGradient(
//                colors = listOf(
//                    Color.Blue.copy(0.4f), Color.White
//                )
//            )
//        )
//        .padding(horizontal = 20.dp)
//        .clearSabFocus(focus, keyboard), horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center){
//        OutlinedTextField(
//            value = userName,
//            onValueChange = { view.setUserName(it) },
//            singleLine = true,
//            label = {Text("user name")}
//        )
//
//        OutlinedTextField(
//            value = userMessage,
//            onValueChange = { view.setUserMessage(it) },
//            singleLine = true,
//            label = {Text("enter message")}
//        )
//
//
//        Button(onClick = {view.addData(userName,userMessage)}) {
//            Text("message send")
//        }
//        dataUser?.let { result->
//            when{
//                result.isSuccess-> {
//                    Toast.makeText(context, "${result.getOrNull()}", Toast.LENGTH_SHORT).show()}
//                result.isFailure-> Text("error : ${result.exceptionOrNull()?:Exception("data fetch failed")}")
//            }
//        }
//
//
//
//
//        dataMessage?.let { result ->
//            when {
//                result.isSuccess -> {
//                    val list = result.getOrNull() ?: emptyList()
//                    list.forEach { item ->
//                        val id = item["id"] as? String ?: return@forEach// return@forEach-> its work like continue which we use in the loop
//                        val user = item["userName"] as? String ?: "Unknown"
//                        val msg = item["message"] as? String ?: ""
//
//                        MessageItem(
//                            id = id,
//                            userName = user,
//                            message = msg,
//                            onUpdate = { msgId, newMsg ->
//                                view.update(msgId, newMsg)
//                            },
//                            onDelete = { msgId ->
//                                view.delete(msgId)
//                            }
//                        )
//                    }
//                }
//                result.isFailure -> Text("error : ${result.exceptionOrNull() ?: Exception("failed fetch message")}")
//            }
//        }
//
//
//    }
//
//
//}
//
//@Composable
//fun MessageItem(
//    id: String,
//    userName: String,
//    message: String,
//    onUpdate: (String, String) -> Unit,
//    onDelete: (String) -> Unit
//) {
//    var isEditing by remember { mutableStateOf(false) }
//    var editedText by remember { mutableStateOf(message) }
//
//    Card(
//        modifier = Modifier
//            .fillMaxWidth()
//            .padding(8.dp),
//        shape = RoundedCornerShape(10.dp)
//    ) {
//        Column(Modifier.padding(10.dp)) {
//
//            Text("👤 $userName", fontWeight = FontWeight.Bold)
//
//            if (isEditing) {
//                // 🔹 Edit mode
//                OutlinedTextField(
//                    value = editedText,
//                    onValueChange = { editedText = it },
//                    modifier = Modifier.fillMaxWidth(),
//                    label = { Text("Edit Message") }
//                )
//
//                Row {
//                    Button(onClick = {
//                        onUpdate(id, editedText) // Firestore update
//                        isEditing = false
//                    }) {
//                        Text("Save")
//                    }
//                    Button(onClick = { isEditing = false }) {
//                        Text("Cancel")
//                    }
//                }
//            } else {
//                // 🔹 Normal mode
//                Text(text = message)
//
//                Row {
//                    Button(onClick = { isEditing = true }) {
//                        Text("Edit")
//                    }
//                    Button(onClick = { onDelete(id) }) {
//                        Text("Delete")
//                    }
//                }
//            }
//        }
//    }
//}