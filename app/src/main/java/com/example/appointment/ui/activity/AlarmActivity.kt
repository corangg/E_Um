package com.example.appointment.ui.activity

import android.app.NotificationManager
import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.appointment.R
import com.example.appointment.receiver.AlarmReceiver

class AlarmActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_alarm)

        tea()
    }

    fun tea(){
        val manager = this.getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        manager.cancel(1234)
        stopAlarmSound()
        stopVibration()
    }

    fun stopAlarmSound(){
        try {
            if(AlarmReceiver.mediaPlayer != null){
                AlarmReceiver.mediaPlayer?.isLooping = false
                AlarmReceiver.mediaPlayer?.stop()
                AlarmReceiver.mediaPlayer?.release()
                AlarmReceiver.mediaPlayer = null

            }
        }catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun stopVibration() {
        try {
            if (AlarmReceiver.vibrator != null) {
                AlarmReceiver.vibrator.cancel()
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}