package com.example.appointment.Receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.example.appointment.Service.CheckStartService

class StartCheckReceiver() : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {

        val startX = intent.getStringExtra("startX")
        val startY = intent.getStringExtra("startY")
        val emailPath = intent.getStringExtra("emailPath")

        val serviceIntent = Intent(context, CheckStartService::class.java)
        serviceIntent.putExtra("startx", startX)
        serviceIntent.putExtra("starty", startY)
        serviceIntent.putExtra("emailPath", emailPath)
        context.startForegroundService(serviceIntent)

    }

}