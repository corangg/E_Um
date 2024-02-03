package com.example.appointment

import androidx.multidex.MultiDexApplication
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class FirebaseCertified: MultiDexApplication() {
    companion object{
        //lateinit var auth: FirebaseAuth
        lateinit var firebaseAuth: FirebaseAuth

        var email: String? = null
        fun checkAuth():Boolean{
            val currentUser = firebaseAuth.currentUser
            return currentUser?.let {
                email = currentUser.email
                if(currentUser.isEmailVerified){
                    true
                }else{
                    false
                }
            }?:let { false }
        }
    }

    override fun onCreate() {
        super.onCreate()
        firebaseAuth= Firebase.auth
    }
}