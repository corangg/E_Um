package com.example.appointment.repository

import com.example.appointment.data.Utils
import com.google.firebase.auth.FirebaseAuthException
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class LoginRepository {

    suspend fun login(email : String, password : String):Int{
        return suspendCoroutine {continuation ->
            Utils.auth.signInWithEmailAndPassword(email,password).addOnCompleteListener {
                if (it.isSuccessful) {
                    if (it.result.user?.isEmailVerified == true) {
                        continuation.resume(4)
                    } else {
                        continuation.resume(1)
                    }
                } else {
                    val errorCode = (it.exception as FirebaseAuthException).errorCode

                    when(errorCode){
                        "ERROR_INVALID_EMAIL" -> continuation.resume(2)
                        "ERROR_INVALID_CREDENTIAL" -> continuation.resume(3)
                    }
                }
            }
        }
    }
}