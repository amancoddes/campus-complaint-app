package com.example.soul

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await


class ChatAppRepoInterfaceImplementation(private val db:FirebaseFirestore):ChatAppInterface {
    override fun sendMessages(message: String): Flow<Result<String>> = callbackFlow {
        db.collection("ChatApp_messageData1").add(DataOfUser(message))
            .addOnSuccessListener { docRef ->
                trySend(Result.success(docRef.id))
            }.addOnFailureListener { error ->
            trySend(Result.failure(error))
        }
        awaitClose { }
    }

    override fun messageFromDatabase(): Flow<Result<List<Map<String, Any>>>> = callbackFlow {
        val subscription =
            db.collection("ChatApp_messageData1").orderBy("time", Query.Direction.ASCENDING)
                .addSnapshotListener { value, error ->

                    if (error != null) {
                        trySend(Result.failure(error))
                        return@addSnapshotListener
                    }
                   val list=
                       value?.documents?.map {

                          val x= it.data?: emptyMap()
                           x+("id" to it.id)
                       } ?: emptyList()

                    trySend(Result.success(list))

                }
        awaitClose { subscription.remove() }
    }

    override suspend fun editMessage(docId: String, editMessage:String): Result<Unit> {
        return try {
            db.collection("ChatApp_messageData1").document(docId).update("message", editMessage).await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }    }

    override suspend fun deleteMessage(docId: String): Result<Unit> {
        return try{
            db.collection("ChatApp_messageData1").document(docId).delete().await()
            Result.success(Unit)
        }
        catch (e:Exception){
            Result.failure(e)
        }
    }


}



/*
	2.	addSnapshotListener internally:
	•	Firestore me ek listener register karta hai jo database ke changes ko monitor kare.
	•	Ye listener har change par invoke hota hai (insert/update/delete).
	•	Ye ListenerRegistration object return karta hai, jisme listener ka internal handle/store hota hai.
	3.	subscription ke andar basically ye hota hai:
	•	Listener ka internal reference
	•	Ye reference hume later listener remove karne ke liye chahiye.
	4.	Agar aap subscription.remove() call karte ho:
	•	Firestore ke internal listener unregister ho jata hai.
	•	Aur ab database changes ke liye callback nahi aayega.

💡 Summary: subscription me listener ka handle/store hai, jo aapko us listener ko control (remove) karne me help karta hai.
 */



/*
Haan, bilkul sahi! ✅
	•	ListenerRegistration ek class hai jo Firestore ka listener handle karti hai.
	•	Jab aap addSnapshotListener { ... } call karte ho, ye ek ListenerRegistration object return karta hai.
	•	Us object ko hum variable me store karte hain, jaise:

val subscription: ListenerRegistration = db.collection("messages")
    .addSnapshotListener { snapshot, error ->
        // update UI
    }

	•	subscription ab us listener ka reference hai.
	•	Jab hum subscription.remove() call karte hain, ye listener ko Firestore se unregister kar deta hai.

Simple shabdon me: ListenerRegistration = listener ka handle.

Agar chaho, me isko ek diagram ke saath bhi explain kar du jisse ye clear ho jaye ki Flow, listener, aur remove kaise kaam karte hain. Chahoge mai bana du?
 */