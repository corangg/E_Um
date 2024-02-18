package com.example.appointment.repository

import android.app.Activity
import android.app.NotificationManager
import androidx.appcompat.app.AppCompatActivity
import com.example.appointment.data.Utils
import com.example.appointment.receiver.AlarmReceiver

class AlarmRepository(private val utils: Utils) {

    fun alarmStop(activity: Activity){
        val manager = activity.getSystemService(AppCompatActivity.NOTIFICATION_SERVICE) as NotificationManager
        manager.cancel(1234)
        stopAlarmSound()
        stopVibration()
    }

    private fun stopAlarmSound(){
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