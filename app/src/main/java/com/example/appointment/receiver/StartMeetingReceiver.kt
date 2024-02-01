package com.example.appointment.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.example.appointment.service.MeetingStartService

class StartMeetingReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        val startX = intent.getStringExtra("startX")
        val startY = intent.getStringExtra("startY")
        val emailPath = intent.getStringExtra("emailPath")

        val serviceIntent = Intent(context, MeetingStartService::class.java)
        serviceIntent.putExtra("startx", startX)
        serviceIntent.putExtra("starty", startY)
        serviceIntent.putExtra("emailPath", emailPath)
        context.startForegroundService(serviceIntent)
    }
}