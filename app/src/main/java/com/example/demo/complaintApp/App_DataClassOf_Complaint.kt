package com.example.demo.complaintApp

import com.google.firebase.Timestamp
import com.google.firebase.firestore.FieldValue


data class FirstAppFireStoreDataClass(
    val id: String = "",
    val complain: String = "",
    val description: String = "",
    val timestamp: Timestamp? =null,
    val address: String = "",
    val status: String = "PENDING",
    val userId: String = "",
   val imageUrl: String?=null,
    val latitude: Double? = null,
    val longitude: Double? = null,
    val hash: String="",
    val accuracy: Float =0f,
    val confidence: Confidence=Confidence.MEDIUM,
    val mode:Mode = Mode.OUTDOOR,
    val numberOfPeoples:Int=0,
    val updatedTime: Timestamp?=null,
    val resolvedImageUrl: String?=null
// indoor ya outdoor
// jitne bhi parameters hai unko default value deni hogi kyu jab firebase apne value store karge to usko emtpyt chiye
)