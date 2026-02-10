//package com.example.soul
//
//import androidx.lifecycle.ViewModel
//import androidx.lifecycle.ViewModelProvider
//
//class AgainFactory():ViewModelProvider.Factory{
//    override fun <T : ViewModel> create(modelClass: Class<T>): T {
//        val repo=AgainRepositry()
//        return AgainViewModel(repo) as T
//    }
//}