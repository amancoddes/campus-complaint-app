package com.example.soul

import com.example.demo.complaintApp.ComplaintSubmissionRepository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import io.mockk.coEvery
import io.mockk.mockk
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

//
//@RunWith(JUnit4::class)
//class RepositoriesTestPreviewScreen {
//
//
//    private lateinit var firebaseStore:FirebaseFirestore
//    private lateinit var auth:FirebaseAuth
//    @Before
//    fun set(){
//        firebaseStore= mockk()
//        auth= mockk()
//    }
//
//    @Test
//    fun sendComplaint_WhenFirebaseSuccessful_ShouldReturnResultSuccess(){
//
//
//
//
//
//    }
//
//}