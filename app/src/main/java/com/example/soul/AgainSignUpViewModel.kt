//package com.example.soul
//
//import androidx.lifecycle.ViewModel
//import androidx.lifecycle.viewModelScope
//import com.google.firebase.auth.FirebaseUser
//import kotlinx.coroutines.Dispatchers
//import kotlinx.coroutines.flow.MutableStateFlow
//import kotlinx.coroutines.flow.asStateFlow
//import kotlinx.coroutines.launch
//
//
//class AgainViewModel(private val repo:AgainRepositry):ViewModel(){
//    private val _email = MutableStateFlow<String>("")
//    val email=_email.asStateFlow()
//    private val _password= MutableStateFlow<String>("")
//    val password=_password.asStateFlow()
//
//
//    fun emailSet(email:String){
//        _email.value=email
//    }
//
//    fun passwordSet(password:String){
//        _password.value=password
//    }
//
//    private val _user=MutableStateFlow<Result<FirebaseUser?>?>(null)
//    val user=_user.asStateFlow()
//
//
//
//    fun signUp(){
//        viewModelScope.launch(Dispatchers.IO) {
//            try {
//                repo.signUp(_email.value,_password.value).collect {
//                    result->
//                    _user.value=result
//                }
//            }catch (e:Exception){
//                _user.value=Result.failure(e)
//            }
//
//        }
//
//    }
//
//    private val _userLogin=MutableStateFlow<Result<FirebaseUser?>?>(null)
//    val userLogin=_userLogin.asStateFlow()
//
//    fun login(){
//        viewModelScope.launch(Dispatchers.IO) {
//            try{
//                repo.login(_email.value,password.value).collect{
//                        result->
//                    _userLogin.value=result
//                }
//            }catch (e:Exception){
//                _userLogin.value= Result.failure(e)
//            }
//
//        }
//
//    }
//    fun logout(){
//        repo.logout()
//    }
//
//    private val _authState=MutableStateFlow<FirebaseUser?>(null)
//    val authState=_authState.asStateFlow()
//    init {
//        viewModelScope.launch {// esme Dispatcher lagane ki jarurat nhi
//            repo.observeAuth().collect{
//                    state-> _authState.value=state
//            }
//        }
//
//    }
//}