package com.example.appointment.receiver

import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity

class AlarmCancelReceiver : BroadcastReceiver() {
    companion object {
        const val ACTION_DISMISS_ALARM = "action_dismiss_alarm"
    }

    override fun onReceive(context: Context, intent: Intent) {
        when (intent.action) {
            ACTION_DISMISS_ALARM -> {
                stopAlarmSound()
                stopVibration()
                val manager = context.getSystemService(AppCompatActivity.NOTIFICATION_SERVICE) as NotificationManager
                manager.cancel(1234)
            }
        }

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