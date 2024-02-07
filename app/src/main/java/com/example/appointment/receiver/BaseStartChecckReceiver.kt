package com.example.appointment.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent

abstract class BaseStartChecckReceiver : BroadcastReceiver(){
    fun startService(context: Context, intent: Intent, serviceIntent: Intent){
        val startX = intent.getStringExtra("startX")
        val startY = intent.getStringExtra("startY")
        val emailPath = intent.getStringExtra("emailPath")

        serviceIntent.putExtra("startx", startX)
        serviceIntent.putExtra("starty", startY)
        serviceIntent.putExtra("emailPath", emailPath)
        context.startForegroundService(serviceIntent)
    }
}