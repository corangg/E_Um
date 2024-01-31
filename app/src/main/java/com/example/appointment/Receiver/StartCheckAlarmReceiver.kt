package com.example.appointment.Receiver

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
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.os.VibrationEffect
import android.os.Vibrator
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
import com.example.appointment.R
import com.example.appointment.Receiver.AlarmReceiver.Companion.vibrator
import com.example.appointment.main.MainActivity

class StartCheckAlarmReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        val message : String = intent.getStringExtra("message")!!
        vibrator(context)
        notification(context,message)
        sound(context)
    }

    private fun vibrator(context: Context){
        AlarmReceiver.vibrator = context.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val vibrationEffect = VibrationEffect.createWaveform(longArrayOf(100,1000,100,1000),
                intArrayOf(0, VibrationEffect.DEFAULT_AMPLITUDE,0, VibrationEffect.DEFAULT_AMPLITUDE),3
            )
            AlarmReceiver.vibrator.vibrate(vibrationEffect)

        } else {
            AlarmReceiver.vibrator.vibrate(10000)
        }
    }

    private fun sound(context: Context){
        try {
            AlarmReceiver.mediaPlayer = MediaPlayer()

            val resId = R.raw.alarmtest
            AlarmReceiver.mediaPlayer?.setDataSource(context, Uri.parse("android.resource://${context.packageName}/$resId"))
            AlarmReceiver.mediaPlayer?.prepare()
            AlarmReceiver.mediaPlayer?.isLooping = true

            AlarmReceiver.mediaPlayer?.start()

            Handler(Looper.getMainLooper()).postDelayed({
                AlarmReceiver.mediaPlayer?.isLooping = false
                AlarmReceiver.mediaPlayer?.stop()
                AlarmReceiver.mediaPlayer?.release()
            }, 30000)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun notification(context: Context,message:String){
        val manager = context.getSystemService(AppCompatActivity.NOTIFICATION_SERVICE) as NotificationManager
        val builder: NotificationCompat.Builder
        val intent = Intent(context, MainActivity::class.java)
        intent.putExtra("path","openNotification")
        val pendingIntent = PendingIntent.getActivity(context,2094,intent, PendingIntent.FLAG_CANCEL_CURRENT or PendingIntent.FLAG_IMMUTABLE)

        val dismissIntent = Intent(context, AlarmCancelReceiver::class.java)
        dismissIntent.action = AlarmCancelReceiver.ACTION_DISMISS_ALARM
        val dismissPendingIntent = PendingIntent.getBroadcast(context, 2338, dismissIntent, PendingIntent.FLAG_CANCEL_CURRENT or PendingIntent.FLAG_IMMUTABLE)
        val dismissAction = NotificationCompat.Action(
            R.drawable.ic_calendar,
            "알람 끄기",
            dismissPendingIntent
        )


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channelId = "one-channel"
            val channelName = "My Channel One"
            val channel = NotificationChannel(
                channelId,
                channelName,
                NotificationManager.IMPORTANCE_DEFAULT
            ).apply {
                description = "My Channel One Description"
                setShowBadge(true)
                val uri: Uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
                val audioAttributes = AudioAttributes.Builder()
                    .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                    .setUsage(AudioAttributes.USAGE_ALARM)
                    .build()
                setSound(uri, audioAttributes)
            }

            manager.createNotificationChannel(channel)

            builder = NotificationCompat.Builder(context, channelId)
        } else {
            //26버전 이하
            builder = NotificationCompat.Builder(context)
        }

        builder.run {
            val notificationText : String = message//"출발 시간까지 5분 남았습니다."
            setSmallIcon(android.R.drawable.ic_notification_overlay)
            setWhen(System.currentTimeMillis())
            setContentTitle("이제 출발하실 시간입니다.")
            setContentText(notificationText)
            setContentIntent(pendingIntent)
            setAutoCancel(true)
            addAction(dismissAction)
        }

        manager.notify(12, builder.build())
    }
}