package com.example.appointment.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.example.appointment.service.CheckStartService

class CheckStartReceiver() : BaseStartChecckReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        val serviceIntent = Intent(context, CheckStartService::class.java)
        startService(context,intent,serviceIntent)
    }
}