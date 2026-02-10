//package com.example.soul
//
//import androidx.compose.foundation.layout.*
//import androidx.compose.material3.*
//import androidx.compose.runtime.*
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.unit.dp
//import androidx.lifecycle.ViewModel
//import androidx.lifecycle.viewModelScope
//import androidx.lifecycle.viewmodel.compose.viewModel
//import com.google.firebase.firestore.FirebaseFirestore
//import kotlinx.coroutines.Dispatchers
//import kotlinx.coroutines.channels.awaitClose
//import kotlinx.coroutines.flow.*
//import kotlinx.coroutines.launch
//
//// ---------------- Firestore Repository ----------------
//class FirestoreRepository {
//    private val db = FirebaseFirestore.getInstance()
//
//    fun addUser(name: String, email: String): Flow<Result<String>> = callbackFlow {
//        data class User(
//            val name: String = "",
//            val email: String = ""
//        )
//        val user = User(name, email)
//        db.collection("users").add(user)// collection hume bas path deta hai folder ka with name ko kosa banega
//            .addOnSuccessListener { docRef ->
//                trySend(Result.success(docRef.id))
//            }
//            .addOnFailureListener { e ->
//                trySend(Result.failure(e))
//            }
//        awaitClose { }
//    }
//
//    fun getUsers(): Flow<Result<List<Map<String, Any>>>> = callbackFlow {
//        db.collection("users")
//            .get()
//            .addOnSuccessListener { snapshot ->
//                val list = snapshot.documents.map { it.data ?: emptyMap() }
//                trySend(Result.success(list))
//            }
//            .addOnFailureListener { e ->
//                trySend(Result.failure(e))
//            }
//        awaitClose { }
//    }
//}
//
//// ---------------- Firestore ViewModel ----------------
//class FirestoreViewModel(private val repo: FirestoreRepository) : ViewModel() {
//    private val _addState = MutableStateFlow<Result<String>?>(null)
//    val addState: StateFlow<Result<String>?> = _addState
//
//
//
//    fun addUser(name: String, email: String) {
//        viewModelScope.launch(Dispatchers.IO) {
//            repo.addUser(name, email)
//                //  .catch { e -> _addState.value = Result.failure(e) } humne .falure ko try send se emit karva hai eliye ye lagane ki jarurat nhi hai
//                .collect { result -> _addState.value = result }
//        }
//    }
//    private val _users = MutableStateFlow<Result<List<Map<String, Any>>>?>(null)
//    val users: StateFlow<Result<List<Map<String, Any>>>?> = _users
//    fun loadUsers() {
//        viewModelScope.launch(Dispatchers.IO) {
//            repo.getUsers()
//                .catch { e -> _users.value = Result.failure(e) }
//                .collect { result -> _users.value = result }
//        }
//    }
//}
//
//// ---------------- FirestoreScreen Composable ----------------
//@Composable
//fun FirestoreScreen(viewModel: FirestoreViewModel = viewModel(factory = object : androidx.lifecycle.ViewModelProvider.Factory {
//    override fun <T : ViewModel> create(modelClass: Class<T>): T {
//        return FirestoreViewModel(FirestoreRepository()) as T
//    }
//})) {
//    val addState by viewModel.addState.collectAsState()
//    val usersState by viewModel.users.collectAsState()
//
//    var name by remember { mutableStateOf("") }
//    var email by remember { mutableStateOf("") }
//
//    Column(modifier = Modifier.padding(16.dp)) {
//
//        // Input fields
//        OutlinedTextField(value = name, onValueChange = { name = it }, label = { Text("Name") })
//        Spacer(modifier = Modifier.height(8.dp))
//        OutlinedTextField(value = email, onValueChange = { email = it }, label = { Text("Email") })
//
//        Spacer(modifier = Modifier.height(16.dp))
//
//        // Add User Button
//        Button(onClick = { viewModel.addUser(name, email) }) {
//            Text("Add User")
//        }
//
//        // Show Add User Result
//        addState?.let { result ->
//            when {
//                result.isSuccess -> Text("Added! Document ID: ${result.getOrNull()}")//
//                result.isFailure -> Text("Error: ${result.exceptionOrNull()?.message}")
//            }
//        }
//
//        Spacer(modifier = Modifier.height(24.dp))
//
//        // Load Users Button
//        Button(onClick = { viewModel.loadUsers() }) {
//            Text("Load Users")
//        }
//
//        // Show Users List
//        usersState?.let { result ->
//            when {
//                result.isSuccess -> {
//                    val list = result.getOrNull() ?: emptyList()
//                    list.forEach { user ->
//                        Text("Name: ${user["name"]}, Email: ${user["email"]}")
//                    }
//                }
//                result.isFailure -> Text("Error: ${result.exceptionOrNull()?.message}")
//            }
//        }
//    }
//}