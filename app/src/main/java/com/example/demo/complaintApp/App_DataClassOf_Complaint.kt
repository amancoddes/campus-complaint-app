package com.example.demo.complaintApp


data class FirstAppFireStoreDataClass(
    val id: String = "",
    val complain: String = "",
    val description: String = "",
    val timestamp: Long = System.currentTimeMillis(),
    val address: String = "",
    val status: String = "ACTIVE",
    val userId: String = "",
   // val imageUrl: String = ""// firestorage mie string ke form mei store hoti hai
    val latitude: Double? = null,
    val longitude: Double? = null,
    val hash: String="",
    val accuracy: Float =0f,
    val confidence: Confidence=Confidence.MEDIUM,
    val mode:Mode = Mode.OUTDOOR,
    val numberOfPeoples:Int=0
// indoor ya outdoor
// jitne bhi parameters hai unko default value deni hogi kyu jab firebase apne value store karge to usko emtpyt chiye
)