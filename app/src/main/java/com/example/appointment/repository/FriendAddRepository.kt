package com.example.appointment.repository

import com.example.appointment.data.Utils
import com.google.firebase.database.DatabaseReference
import com.google.firebase.firestore.DocumentReference
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine


class FriendAddRepository(private val utils: Utils) {

    suspend fun selectFriendRequestItem(reference: DatabaseReference):Boolean{
        val userEmail = Utils.auth.currentUser?.email

        return suspendCoroutine {
            reference.setValue(userEmail).addOnCompleteListener { task->
                if(task.isSuccessful){
                    it.resume(true)
                }else{
                    it.resume(false)
                }
            }
        }
    }
}