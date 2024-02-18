package com.example.appointment.`object`

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.icu.util.Calendar
import com.example.appointment.data.YYYYMMDDhhmm

object AlarmUtil {
    fun settingAlarm(context: Context, intent: Intent, requestCode: Int, alarmTime: YYYYMMDDhhmm, checkAlarmTime:Int){
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

        val pendingIntent = PendingIntent.getBroadcast(context, requestCode, intent, PendingIntent.FLAG_CANCEL_CURRENT or PendingIntent.FLAG_IMMUTABLE)

        val calendar = Calendar.getInstance()

        calendar.set(Calendar.YEAR, alarmTime.YYYY)
        calendar.set(Calendar.MONTH, alarmTime.MM-1)
        calendar.set(Calendar.DAY_OF_MONTH, alarmTime.DD)
        calendar.set(Calendar.HOUR_OF_DAY, alarmTime.hh)
        calendar.set(Calendar.MINUTE, alarmTime.mm - checkAlarmTime)
        calendar.set(Calendar.SECOND, 0)

        alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.timeInMillis, pendingIntent)
    }
}