//package com.example.soul
//import android.widget.Toast
//import androidx.compose.foundation.Image
//import androidx.compose.foundation.background
//import androidx.compose.foundation.layout.Arrangement
//import androidx.compose.foundation.layout.Box
//import androidx.compose.foundation.layout.Column
//import androidx.compose.foundation.layout.Row
//import androidx.compose.foundation.layout.Spacer
//import androidx.compose.foundation.layout.fillMaxSize
//import androidx.compose.foundation.layout.fillMaxWidth
//import androidx.compose.foundation.layout.height
//import androidx.compose.foundation.layout.padding
//import androidx.compose.material3.Button
//import androidx.compose.material3.OutlinedTextField
//import androidx.compose.material3.Text
//import androidx.compose.runtime.Composable
//import androidx.compose.runtime.collectAsState
//import androidx.compose.runtime.getValue
//import androidx.compose.ui.Alignment
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.graphics.Brush
//import androidx.compose.ui.graphics.Color
//import androidx.compose.ui.layout.ContentScale
//import androidx.compose.ui.platform.LocalContext
//import androidx.compose.ui.platform.LocalFocusManager
//import androidx.compose.ui.platform.LocalSoftwareKeyboardController
//import androidx.compose.ui.res.painterResource
//import androidx.compose.ui.unit.dp
//import androidx.lifecycle.ViewModel
//import androidx.lifecycle.ViewModelProvider
//import androidx.lifecycle.viewModelScope
//import androidx.lifecycle.viewmodel.compose.viewModel
//import com.google.firebase.auth.FirebaseAuth
//import com.google.firebase.auth.FirebaseUser
//import kotlinx.coroutines.Dispatchers
//import kotlinx.coroutines.channels.awaitClose
//import kotlinx.coroutines.flow.Flow
//import kotlinx.coroutines.flow.MutableStateFlow
//import kotlinx.coroutines.flow.StateFlow
//import kotlinx.coroutines.flow.asStateFlow
//import kotlinx.coroutines.flow.callbackFlow
//import kotlinx.coroutines.launch
//
//class AuthRepository2(){
//
//    //signUp wala code
//    private val auth=FirebaseAuth.getInstance()
//    fun signup(email: String, password: String): Flow<Result<FirebaseUser?>> = callbackFlow {
//        auth.createUserWithEmailAndPassword(email, password)
//            .addOnCompleteListener { task ->
//                if (task.isSuccessful) {
//                    val user = task.result.user
//                    if (user != null) {
//                        trySend(Result.success(user))
//                    } else {
//                        trySend(Result.failure(Exception("User is null even though signup succeeded")))
//                    }
//                } else {
//                    trySend(Result.failure(task.exception ?: Exception("Signup failed")))
//                }
//
//            }
//        awaitClose { }
//    }
//
//    fun login(email: String,password: String):Flow<Result<FirebaseUser?> > = callbackFlow{
//        auth.signInWithEmailAndPassword(email,password).addOnCompleteListener {
//            task->
//            if (task.isSuccessful){
//                val user: FirebaseUser? =task.result.user
//                trySend(Result.success(user))
//            }
//            else{
//                trySend(Result.failure(task.exception?:Exception("login fail")))
//            }
//close()
//        }
//        awaitClose {  }
//    }
//
//
//    fun logout(){
//        auth.signOut()
//    }
//
//    fun observeAuthState(): Flow<FirebaseUser?> = callbackFlow {
////        val listener = FirebaseAuth.AuthStateListener { auth1 ->
////            trySend(auth1.currentUser)
////        }
//        val listener = object : FirebaseAuth.AuthStateListener {
//            override fun onAuthStateChanged(auth1: FirebaseAuth) {
//                trySend(auth1.currentUser)
//            }
//        }
//        auth.addAuthStateListener(listener)
//        awaitClose { auth.removeAuthStateListener(listener) }
//    }
//
//}
//
//
//
//
//class SignupViewModel(private val repoAuth: AuthRepository2):ViewModel(){
//    private var name=MutableStateFlow<String>("")
//    val name2=name.asStateFlow()
//    private var password=MutableStateFlow<String>("")
//    val password2=password.asStateFlow()
//
//    fun nameSet(input:String){
//        name.value=input
//    }
//
//    fun passwordSet(input: String){
//        password.value=input
//    }
//
//    fun logout() {
//        repoAuth.logout()
//    }
//
//    // Flow wala code
//    private val _signupState = MutableStateFlow<Result<FirebaseUser?>?>(null)
//    val signupState=_signupState.asStateFlow()
//    private val _signupStateLogin = MutableStateFlow<Result<FirebaseUser?>?>(null)
//    val signupStateLogin=_signupStateLogin.asStateFlow()
//
//    fun signUp() {
//        // Launch coroutine in IO dispatcher directly
//        viewModelScope.launch(Dispatchers.IO) {
//            try {
//                // Collect the Flow from repository
//                repoAuth.signup(name.value, password.value)
//                    .collect { result ->
//                        _signupState.value = result
//                    }
//            } catch (e: Exception) {
//                _signupState.value = Result.failure(e)
//            }
//        }
//    }
//
//
//
//    fun login(){
//
//        viewModelScope.launch(Dispatchers.IO) {
//            try{
//                repoAuth.login(name.value,password.value).collect{
//                        result->
//                    _signupState.value=result
//                }
//            }
//            catch (e:Exception){
//                _signupState.value=Result.failure(e)
//            }
//            }
//    }
//
//
//    private val _authState = MutableStateFlow<FirebaseUser?>(null)
//    val authState: StateFlow<FirebaseUser?> = _authState
//
//    init {
//        // observe auth state changes continuously
//        viewModelScope.launch {
//            repoAuth.observeAuthState().collect { user ->
//                _authState.value = user
//            }
//        }
//    }
//
//
//}
//
//
//class ViewModelFactory(private val repoAuth:AuthRepository2):ViewModelProvider.Factory{
//    override fun <T : ViewModel> create(modelClass: Class<T>): T {
//        return SignupViewModel(repoAuth) as T
//    }
//}
//
//@Composable
//fun FirstScreen(){
//
//    val repo=AuthRepository2()
//    val view:SignupViewModel= viewModel(factory = ViewModelFactory(repoAuth = repo))
//    val signupState by view.signupState.collectAsState()
//    val signupStateLogin by view.signupStateLogin.collectAsState()
//
//    val focus= LocalFocusManager.current
//    val keyBoard= LocalSoftwareKeyboardController.current
//    val email by view.name2.collectAsState()
//    val password by view.password2.collectAsState()
//    val user23 by view.authState.collectAsState()////
//    if(user23==null){
//
//        Box(modifier = Modifier.fillMaxSize().background(brush = Brush.verticalGradient(colors = listOf(
//            Color.Blue.copy(0.5f), Color.White))).padding(20.dp).clearSabFocus(x = focus,y=keyBoard), contentAlignment = Alignment.Center
//        ){
//
//            Column(horizontalAlignment = Alignment.CenterHorizontally) {
//
//
//                Image(painter = painterResource(id = R.drawable.ic_launcher_foreground), contentDescription = null, contentScale = ContentScale.Crop)
//
//
//
//                OutlinedTextField(
//                    value = email,
//                    onValueChange = {view.nameSet(input = it)},
//                    label = {
//                        Text("enter the email")
//                    },
//                    singleLine = true,
//                    modifier = Modifier.fillMaxWidth()
//
//                )
//
//                Spacer(modifier = Modifier.height(20.dp))
//
//                OutlinedTextField(value = password,
//                    onValueChange = {view.passwordSet(input = it)},
//                    label = {
//                        Text("enter the password")
//                    },
//                    singleLine = true,
//                    modifier = Modifier.fillMaxWidth()
//                )
//
//                Spacer(modifier = Modifier.height(10.dp))
//                val context= LocalContext.current
//                Row (modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween){
//                    Button(onClick = { view.signUp() }) { Text("signup") }
//                    signupState?.let { result ->
//                        result.onSuccess {
//
//                        }.onFailure { e ->
//                            Toast.makeText(context,e.message, Toast.LENGTH_SHORT).show()
//                        }
//                    }
//                      Button(onClick = { view.login() } ) { Text("login") }
//                    signupStateLogin?.let { result ->
//                        result.onSuccess {
//                        }.onFailure { e ->
//                            Toast.makeText(context,e.message, Toast.LENGTH_SHORT).show()
//                        }
//                    }
//                }
//            }
//
//        }
//
//
//    }
//    else{
//        Box(modifier = Modifier.fillMaxSize().background(Color.Magenta.copy(0.6f)), contentAlignment = Alignment.Center
//        ){
//            Column {
//                Text("Login is complete")
//
//                Button(onClick = {
//                    view.logout()
//                }) { Text("Logout") }
//            }
//
//        }
//    }
//}