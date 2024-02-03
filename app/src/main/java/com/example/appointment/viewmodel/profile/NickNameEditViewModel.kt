package com.example.appointment.viewmodel.profile

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData

class NickNameEditViewModel(application: Application): AndroidViewModel(application) {
    val nickName : MutableLiveData<String> = MutableLiveData("")
    val statusMessage : MutableLiveData<String> = MutableLiveData("")

    var nickNameEditSave : MutableLiveData<Unit> = MutableLiveData()

    fun nickNameSave(){
        nickNameEditSave.value = Unit
    }
}