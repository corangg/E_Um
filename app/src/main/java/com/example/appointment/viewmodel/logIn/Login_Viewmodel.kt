package com.example.appointment.viewmodel.logIn

import android.app.Application
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.appointment.StartEvent
import com.example.appointment.data.Utils.Companion.auth
import com.example.appointment.repository.LoginRepository
import com.example.appointment.viewmodel.BaseViewModel
import com.google.firebase.auth.FirebaseAuthException
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class Login_Viewmodel @Inject constructor(
    application: Application,
    private val loginRepository: LoginRepository): BaseViewModel(application) {

    val showSignupActivity:MutableLiveData<Boolean> = MutableLiveData(false)
    val showMainActivity:MutableLiveData<Boolean> = MutableLiveData(false)
    val checkLogin:MutableLiveData<Boolean> = MutableLiveData(false)

    val loginEmail : MutableLiveData<String> = MutableLiveData("")
    val loginPassword : MutableLiveData<String> = MutableLiveData("")

    val loginfail:MutableLiveData<Int> = MutableLiveData(-1)

    init{
        checkAuth()
    }
    fun changeSignup(){
        StartEvent(showSignupActivity)
    }

    fun login(){
        if (loginEmail.value.toString().isNullOrEmpty() || loginPassword.value.toString().isNullOrEmpty()){
            loginfail.value = 0
        }else{
            viewModelScope.launch {
                if(loginRepository.login(loginEmail.value!!,loginPassword.value!!) == 4){
                    StartEvent(showMainActivity)
                }else{
                    loginfail.value = loginRepository.login(loginEmail.value!!,loginPassword.value!!)
                }
            }
        }
    }

    fun checkAuth(){
        val currentUser = auth.currentUser
        currentUser?.let {
            if(currentUser.isEmailVerified){
                checkLogin.value=true
            }
        }
    }

}
