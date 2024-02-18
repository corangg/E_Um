package com.example.appointment.data

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer

class StartEvent(private val T: MutableLiveData<Boolean>) {
    init {
        event()
    }
    fun event() {
        T.value = true
        T.value = false
    }
}
