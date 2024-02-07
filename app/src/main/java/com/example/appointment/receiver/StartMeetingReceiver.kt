package com.example.appointment.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.example.appointment.service.MeetingStartService

class StartMeetingReceiver : BaseStartChecckReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        val serviceIntent = Intent(context, MeetingStartService::class.java)
        startService(context,intent,serviceIntent)
    }
}