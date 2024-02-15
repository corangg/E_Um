package com.example.appointment.viewmodel.profile

import android.app.Application
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.appointment.repository.PasswordEditRepository
import com.example.appointment.viewmodel.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PasswordEditViewModel@Inject constructor(
    application: Application,
    private val passwordEditRepository: PasswordEditRepository) : BaseViewModel(application) {
    val profilePasswordCheck: MutableLiveData<String?> = MutableLiveData("")
    val newpassword :MutableLiveData<String?> = MutableLiveData("")
    val newpasswordCheck :MutableLiveData<String?> = MutableLiveData("")
    val passwordUpdate :MutableLiveData<Int> = MutableLiveData(-1)

    var passwordCheck : Boolean = false

    fun fnPasswordCheck(){
        viewModelScope.launch {
            if(passwordEditRepository.passwordCheck(profilePasswordCheck.value!!)){
                passwordCheck = true
                passwordUpdate.value = 0
            }else{
                passwordUpdate.value = 1
            }
        }
    }

    fun passwordSave(){
        if(passwordCheck == true){
            if(newpassword.value == newpasswordCheck.value) {
                viewModelScope.launch {
                    if(passwordEditRepository.passwordSave(newpassword.value!!)){
                        passwordUpdate.value = 4
                    }else{
                        passwordUpdate.value = 5
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