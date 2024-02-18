package com.example.appointment.ui.activity

import android.app.NotificationManager
import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.databinding.DataBindingUtil.setContentView
import com.example.appointment.R
import com.example.appointment.databinding.ActivityAlarmBinding
import com.example.appointment.receiver.AlarmReceiver
import com.example.appointment.viewmodel.AlarmViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AlarmActivity : BaseActivity<ActivityAlarmBinding>() {
    val viewmodel : AlarmViewModel by viewModels()

    override fun layoutResId(): Int {
        return R.layout.activity_alarm
    }

    override fun initializeUI() {
        binding.viewmodel = viewmodel
        viewmodel.dataSet(intent,this)
    }

    override fun setObserve() {
        viewmodel.alarmStop.observe(this){
            if(it){
                finish()
            }
        }
    }
}