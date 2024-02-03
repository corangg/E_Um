package com.example.appointment.viewmodel.profile

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.example.appointment.data.Utils.Companion.auth
import com.example.appointment.data.Utils.Companion.firestore

class PasswordEditViewModel(application: Application): AndroidViewModel(application) {
    val profilePasswordCheck: MutableLiveData<String?> = MutableLiveData("")
    val newpassword :MutableLiveData<String?> = MutableLiveData("")
    val newpasswordCheck :MutableLiveData<String?> = MutableLiveData("")

    val passwordUpdate :MutableLiveData<Int> = MutableLiveData(-1)

    var password : String? = ""
    var passwordCheck : Boolean = false

    fun fnPasswordCheck(){
        firestore.collection("Privacy").document(auth.currentUser?.email.toString()).get().addOnSuccessListener { documentSnapshot ->
            if (documentSnapshot.exists()) {
                val dataMap = documentSnapshot.data
                password = dataMap?.get("password") as? String
                if(profilePasswordCheck.value == password){
                    passwordCheck = true
                    passwordUpdate.value = 0

                }else{
                    passwordUpdate.value = 1
                }
            }else {
            }
        }
    }

    fun fnPasswordSave(){
        val currentuser = auth.currentUser

        if(passwordCheck == true){
            if(newpassword.value == newpasswordCheck.value) {
                currentuser?.updatePassword(newpassword.value.toString())
                    ?.addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            firestore.collection("Privacy").document(auth.currentUser?.email.toString()).update("password", newpassword.value.toString())
                            passwordUpdate.value = 4
                        }else{
                            passwordUpdate.value = 5

                            val exception = task.exception
                            if (exception != null) {
                                val errorMessage = exception.message
                                println("Password update failed: $errorMessage")
                            }
                        }
                    }
            }else{
                passwordUpdate.value = 3
            }
        }else{
            passwordUpdate.value = 2
        }
    }
}