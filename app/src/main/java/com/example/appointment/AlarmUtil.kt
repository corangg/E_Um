package com.example.appointment

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.icu.util.Calendar
import com.example.appointment.receiver.AlarmReceiver
import com.example.appointment.receiver.StartCheckReceiver
import com.example.appointment.receiver.StartMeetingReceiver
import com.example.appointment.model.AlarmTime
import com.example.appointment.model.StartCheckAlarmData

object AlarmUtil {
    fun setAlarm(context: Context,alarmTime: AlarmTime,nickname:String/*,requestCode:Int*/) {
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

        val intent = Intent(context, AlarmReceiver::class.java)
        intent.putExtra("nickname",nickname)
        val pendingIntent = PendingIntent.getBroadcast(context, 2111, intent, PendingIntent.FLAG_CANCEL_CURRENT or PendingIntent.FLAG_IMMUTABLE)

        val calendar = Calendar.getInstance()

        calendar.set(Calendar.YEAR, alarmTime.YYYY)
        calendar.set(Calendar.MONTH, alarmTime.MM-1)
        calendar.set(Calendar.DAY_OF_MONTH, alarmTime.DD)
        calendar.set(Calendar.HOUR_OF_DAY, alarmTime.hh)
        calendar.set(Calendar.MINUTE, alarmTime.mm)
        calendar.set(Calendar.SECOND, 0)

        alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.timeInMillis, pendingIntent)
    }

    fun checkStartMeeting(context : Context, alarmTime : AlarmTime, startAlarmData : StartCheckAlarmData, chackAlarmTime:Int){
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

        val intent = Intent(context, StartCheckReceiver::class.java)
        intent.putExtra("startX",startAlarmData.startX)
        intent.putExtra("startY",startAlarmData.startY)
        intent.putExtra("emailPath",startAlarmData.meetingName)
        val pendingIntent = PendingIntent.getBroadcast(context, 2112, intent, PendingIntent.FLAG_CANCEL_CURRENT or PendingIntent.FLAG_IMMUTABLE)

        val calendar = Calendar.getInstance()

        calendar.set(Calendar.YEAR, alarmTime.YYYY)
        calendar.set(Calendar.MONTH, alarmTime.MM-1)
        calendar.set(Calendar.DAY_OF_MONTH, alarmTime.DD)
        calendar.set(Calendar.HOUR_OF_DAY, alarmTime.hh)
        calendar.set(Calendar.MINUTE, alarmTime.mm-chackAlarmTime)
        calendar.set(Calendar.SECOND, 0)


        alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.timeInMillis, pendingIntent)
    }

    fun startMeeting(context : Context, alarmTime : AlarmTime, startAlarmData : StartCheckAlarmData){
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

        val intent = Intent(context, StartMeetingReceiver::class.java)
        intent.putExtra("startX",startAlarmData.startX)
        intent.putExtra("startY",startAlarmData.startY)
        intent.putExtra("emailPath",startAlarmData.meetingName)
        val pendingIntent = PendingIntent.getBroadcast(context, 2113, intent, PendingIntent.FLAG_CANCEL_CURRENT or PendingIntent.FLAG_IMMUTABLE)

        val calendar = Calendar.getInstance()

        calendar.set(Calendar.YEAR, alarmTime.YYYY)
        calendar.set(Calendar.MONTH, alarmTime.MM-1)
        calendar.set(Calendar.DAY_OF_MONTH, alarmTime.DD)
        calendar.set(Calendar.HOUR_OF_DAY, alarmTime.hh)
        calendar.set(Calendar.MINUTE, alarmTime.mm)
        calendar.set(Calendar.SECOND, 0)


        alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.timeInMillis, pendingIntent)
    }

}