//package com.example.soul
//
//import androidx.lifecycle.ViewModel
//import androidx.lifecycle.ViewModelProvider
//import com.google.firebase.firestore.FirebaseFirestore
//
//class ChatAppFactory:ViewModelProvider.Factory{
//    override fun <T : ViewModel> create(modelClass: Class<T>): T {
//        val repo:ChatAppRepoInterfaceImplementation=ChatAppRepoInterfaceImplementation(FirebaseFirestore.getInstance())
//        return ChatAppViewModel(repo) as T
//    }
//}