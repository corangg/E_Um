package com.example.appointment.receiver

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.media.AudioAttributes
import android.media.MediaPlayer
import android.media.RingtoneManager
import android.net.Uri
import android.os.Build
import android.os.Handler
import android.os.Looper
import android.os.VibrationEffect
import android.os.Vibrator
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
import com.example.appointment.R
import com.example.appointment.ui.activity.MainActivity

class StartCheckAlarmReceiver : BaseAlarmReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        val text = intent!!.getStringExtra("message")!!
        val title = "이제 출발하실 시간입니다."
        notification(context!!,title,text)
        super.onReceive(context, intent)
    }
}