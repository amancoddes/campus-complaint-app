package com.example.soul

import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withTimeout

//        return try {
//
//            // Upload image
//            val fileName = "complaints/${UUID.randomUUID()}.jpg"
//            val imageRef = storage.reference.child(fileName)
//            imageRef.putFile(image).await()
//            val downloadUrl = imageRef.downloadUrl.await().toString()
//
//            // Update model
//            val updatedData = data.copy(imageUrl = downloadUrl)
//
//            // Save to firestore
//            val doc = firebase.collection("Complaints")
//                .add(updatedData)
//                .await()
//
//            Result.success(doc.id)
//        } catch (e: Exception) {
//            Result.failure(e)
//        }




////
//return try {
//    withTimeout(10_000) {
//        val userId = auth.currentUser?.uid ?: "anonymous"
//        val docRef = firebase.collection("complaints").document()
//        val generatedId = docRef.id
//        val complaintData = data.copy(
//            userId = userId,
//            id = generatedId
//        )
//        //document = Firestore ke new record ka reference
//        //.id = us record ka unique primary key
//        //To Firestore tumhare data ko ek new document ke रूप me store karta hai aur DocumentReference return karta hai. document mei kya store hai
//        docRef.set(complaintData)// set() jo id hai usme saved the data , add() mei hum firebase ko bolte hai id tum bana lo or hume milta bhi nhi hai elslye hum baad mei up date or delete bhi nahi kar sakte hai
//            // colleciton mei data add karega
//            .await()// ye code excicutaiton rok kar rakhe ga agar kamm nhii hua ho to
//// ye kuch return nhi karte agar error aya to catch block run hoga or agar sab aache se hogaya to //  Result.success("its sucess ") ye run hoga or return hoga
//        Result.success(docRef.id)
//    }
//}catch (e: TimeoutCancellationException){
//    Result.failure(Exception("Timeout! Check internet connection"))
//}
//catch (e: Exception) {
//    Result.failure(e)
//}
//
//}
////