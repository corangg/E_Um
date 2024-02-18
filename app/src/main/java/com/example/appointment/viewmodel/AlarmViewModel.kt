package com.example.appointment.viewmodel

import android.app.Activity
import android.app.Application
import android.app.NotificationManager
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.MutableLiveData
import com.example.appointment.StartEvent
import com.example.appointment.repository.AlarmRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class AlarmViewModel @Inject constructor(
    application: Application,
    private val alarmRepository: AlarmRepository) : BaseViewModel(application){
    val nickname : MutableLiveData<String> = MutableLiveData("")
    val address : MutableLiveData<String> = MutableLiveData("")
    val hh : MutableLiveData<String> = MutableLiveData("")
    val mm : MutableLiveData<String> = MutableLiveData("")

    val alarmStop : MutableLiveData<Boolean> = MutableLiveData(false)

    val alarmActivity : MutableLiveData<Activity> = MutableLiveData()



    fun dataSet(intent: Intent, activity: Activity){
        val time = intent.getStringExtra("startTime")
        nickname.value = intent.getStringExtra("nickname") + "님 과의 약속을 준비하셔야 합니다."
        address.value = intent.getStringExtra("address")
        hh.value = time!!.substring(8,10)
        mm.value = time!!.substring(10,12)
        alarmActivity.value = activity
    }

    fun clickAlarmStop(){
        alarmRepository.alarmStop(alarmActivity.value!!)
        StartEvent(alarmStop)
    }
}