//// MainActivity.kt
//package com.example.soul
//
//import android.widget.Toast
//import androidx.compose.foundation.layout.Arrangement
//import androidx.compose.foundation.layout.Column
//import androidx.compose.foundation.layout.Row
//import androidx.compose.foundation.layout.Spacer
//import androidx.compose.foundation.layout.fillMaxSize
//import androidx.compose.foundation.layout.fillMaxWidth
//import androidx.compose.foundation.layout.height
//import androidx.compose.foundation.layout.padding
//import androidx.compose.foundation.text.KeyboardOptions
//import androidx.compose.material3.Button
//import androidx.compose.material3.OutlinedTextField
//import androidx.compose.material3.Text
//import androidx.compose.runtime.Composable
//import androidx.compose.runtime.getValue
//import androidx.compose.runtime.mutableStateOf
//import androidx.compose.runtime.remember
//import androidx.compose.runtime.setValue
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.platform.LocalContext
//import androidx.compose.ui.text.input.KeyboardType
//import androidx.compose.ui.unit.dp
//import androidx.lifecycle.LiveData
//import androidx.lifecycle.MutableLiveData
//import androidx.lifecycle.ViewModel
//import androidx.lifecycle.viewmodel.compose.viewModel
//import com.google.firebase.auth.FirebaseAuth
//import com.google.firebase.auth.FirebaseUser
//
//
//// Repository
//class AuthRepository {
//    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
//
//    fun signup(email: String, password: String): LiveData<Result<FirebaseUser?>> {
//        val result = MutableLiveData<Result<FirebaseUser?>>()
//        auth.createUserWithEmailAndPassword(email, password)
//            .addOnCompleteListener { task1 ->
//                if (task1.isSuccessful) {
//                    result.value = Result.success(auth.currentUser)
//                } else {
//                    result.value = Result.failure(task1.exception ?: Exception("Signup failed"))
//                }
//            }
//        return result
//    }
//
//    fun login(email: String, password: String): LiveData<Result<FirebaseUser?>> {
//        val result = MutableLiveData<Result<FirebaseUser?>>()
//        auth.signInWithEmailAndPassword(email, password)
//            .addOnCompleteListener { task ->
//                if (task.isSuccessful) {
//                    result.value = Result.success(auth.currentUser)
//                } else {
//                    result.value = Result.failure(task.exception ?: Exception("Login failed"))
//                }
//            }
//        return result
//    }
//
//    fun logout() {
//        auth.signOut()
//    }
//
//    fun getCurrentUser(): FirebaseUser? {
//        return auth.currentUser
//    }
//}
//
//// ViewModel
//class AuthViewModel(private val repo: AuthRepository = AuthRepository()) : ViewModel() {
//    fun signup(email: String, password: String) = repo.signup(email, password)
//    fun login(email: String, password: String) = repo.login(email, password)
//    fun logout() = repo.logout()
//    fun getCurrentUser() = repo.getCurrentUser()
//}
//
//
//
//// Compose UI
//@Composable
//fun FirebaseAuthApp(viewModel: AuthViewModel = viewModel()) {
//    var email by remember { mutableStateOf("") }
//    var password by remember { mutableStateOf("") }
//    var isLoggedIn by remember { mutableStateOf(viewModel.getCurrentUser() != null) }
//
//    Column(
//        modifier = Modifier
//            .fillMaxSize()
//            .padding(16.dp),
//        verticalArrangement = Arrangement.Center
//    ) {
//
//        if (!isLoggedIn) {
//            // Email input
//            OutlinedTextField(
//                value = email,
//                onValueChange = { email = it },
//                label = { Text("Email") },
//                modifier = Modifier.fillMaxWidth(),
//                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email)
//            )
//            Spacer(modifier = Modifier.height(8.dp))
//
//            // Password input
//            OutlinedTextField(
//                value = password,
//                onValueChange = { password = it },
//                label = { Text("Password") },
//                modifier = Modifier.fillMaxWidth(),
//                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password)
//            )
//            Spacer(modifier = Modifier.height(16.dp))
//
//            // Buttons
//            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
//                val context = LocalContext.current
//
//                Button(onClick = {
//                    viewModel.signup(email, password).observeForever { result ->
//                        result.onSuccess {
//                            isLoggedIn = true
//                        }.onFailure { e ->
//                            e.message?.let { msg -> Toast.makeText(context, msg, Toast.LENGTH_SHORT).show() }
//                        }
//                    }
//                }) {
//                    Text("Signup")
//                }
//
//                Button(onClick = {
//                    viewModel.login(email, password).observeForever { result ->
//                        result.onSuccess {
//                            isLoggedIn = true
//                        }.onFailure { e ->
//                            e.message?.let { msg -> Toast.makeText(context, msg, Toast.LENGTH_SHORT).show() }
//                        }
//                    }
//                }) {
//                    Text("Login")
//                }
//            }
//
//        } else {
//            Text("Logged in as: ${viewModel.getCurrentUser()?.email}")
//            Spacer(modifier = Modifier.height(16.dp))
//            Button(onClick = {
//                viewModel.logout()
//                isLoggedIn = false
//            }) {
//                Text("Logout")
//            }
//        }
//    }
//}