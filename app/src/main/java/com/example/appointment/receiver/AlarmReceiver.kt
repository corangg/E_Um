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

class AlarmReceiver : BaseAlarmReceiver() {
    companion object{
        var mediaPlayer : MediaPlayer? = null
        lateinit var vibrator : Vibrator
    }

    override fun onReceive(context: Context?, intent: Intent?) {
        val text = intent!!.getStringExtra("nickname") + "님과의 약속을 준비할 시간입니다."
        val title = "이제 준비하실 시간입니다."
        notification(context!!, title, text)
        super.onReceive(context, intent)
    }
}
