package com.example.appointment.viewmodel.logIn

import android.app.Application
import androidx.lifecycle.MutableLiveData
import com.example.appointment.data.Utils
import com.example.appointment.data.Utils.Companion.auth
import com.example.appointment.viewmodel.BaseViewModel
import com.google.firebase.auth.FirebaseAuthException

class Login_Viewmodel(application: Application): BaseViewModel(application) {

    val showSignup_Fragment:MutableLiveData<Boolean> = MutableLiveData(false)
    val showMainActivity:MutableLiveData<Boolean> = MutableLiveData(false)
    val checkLogin:MutableLiveData<Boolean> = MutableLiveData(false)

    val loginEmail : MutableLiveData<String> = MutableLiveData("")
    val loginPassword : MutableLiveData<String> = MutableLiveData("")

    val loginfail:MutableLiveData<Int> = MutableLiveData(-1)

    init{
        checkAuth()
    }
    fun changeSignup(){
        showSignup_Fragment.value = true
    }

    fun login(){
        if (loginEmail.value.toString().isNullOrEmpty() || loginPassword.value.toString().isNullOrEmpty()){
            loginfail.value = 0
        }else{
            auth.signInWithEmailAndPassword(loginEmail.value.toString(),loginPassword.value.toString()).addOnCompleteListener {
                if (it.isSuccessful) {
                    if (it.result.user?.isEmailVerified == true) {
                        showMainActivity.value = true
                    } else {
                        loginfail.value = 1
                    }
                } else {
                    val errorCode = (it.exception as FirebaseAuthException).errorCode

                    when(errorCode){
                        "ERROR_INVALID_EMAIL" -> loginfail.value = 2
                        "ERROR_INVALID_CREDENTIAL" -> loginfail.value = 3
                    }
                }
            }
        }
    }

    fun checkAuth(){
        val currentUser = auth.currentUser//FirebaseCertified.firebaseAuth.currentUser
        currentUser?.let {
            if(currentUser.isEmailVerified){
                checkLogin.value=true
            }
        }
    }

}
