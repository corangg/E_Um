package com.example.appointment.repository

import androidx.lifecycle.MutableLiveData
import com.example.appointment.data.Utils
import com.example.appointment.model.PrivacyDataModel
import com.example.appointment.model.ProfileDataModel
import com.example.appointment.model.SignUpDataModel
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class SignUpRepository {
    val signupSuccess :MutableLiveData<Boolean> = MutableLiveData(false)
    val signUpZoneCode : MutableLiveData<String> = MutableLiveData("")
    val signUpAddress : MutableLiveData<String> = MutableLiveData("")

    fun splitAddress(addressData : String){
        var splitArray = addressData.toString().split(",")
        signUpZoneCode.value = splitArray[0]
        signUpAddress.value = splitArray[1]
    }

    suspend fun signUpTry(email : String, password : String, signUpData : SignUpDataModel) : Boolean{
        return suspendCoroutine {continuation->
            Utils.auth.createUserWithEmailAndPassword(email,password).addOnCompleteListener {
                if(it.isSuccessful){
                    continuation.resume(true)
                    savePrivacy(signUpData)
                }else{
                    continuation.resume(false)
                }
            }
        }
    }

    fun savePrivacy(signUpData : SignUpDataModel){
        Utils.firestore.collection("Profile").document(signUpData.profile.email).set(signUpData.profile)
        Utils.firestore.collection("Privacy").document(signUpData.privacy.email).set(signUpData.privacy).addOnCompleteListener {
            if(it.isSuccessful){
                signupSuccess.value = true
                Utils.auth.currentUser?.sendEmailVerification()
            }
        }
    }
}