package com.example.appointment.repository

import com.example.appointment.data.Utils
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class PasswordEditRepository(private val utils: Utils) {

    suspend fun passwordCheck(profilePassword : String) : Boolean{
        val docRef = Utils.firestore.collection("Privacy").document(Utils.auth.currentUser?.email.toString())
        val dataMap = utils.readDataFirebase(docRef)

        return if(dataMap != null){
            val password = dataMap.get("password") as String
            if(profilePassword == password){
                true
            }else{
                false
            }
        }else{
            false
        }
    }

    suspend fun passwordSave(password : String) : Boolean{
        return suspendCoroutine { continuation ->
            val currentUser = Utils.auth.currentUser

            currentUser?.updatePassword(password)?.addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Utils.firestore.collection("Privacy").document(Utils.auth.currentUser?.email!!).update("password", password)
                    continuation.resume(true)
                } else {
                    continuation.resume(false)
                }
            }
        }
    }
}