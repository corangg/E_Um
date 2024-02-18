package com.example.appointment.service

import android.content.Intent
import com.example.appointment.receiver.StartCheckAlarmReceiver

class MeetingStartService : BaseStartService() {

    override fun startBroadcast(intent: Intent) {
        fnSendStartMessage(intent,"출발하지 않았습니다. 따로 연락해 보세요.")
        val broadcastIntent = Intent(this, StartCheckAlarmReceiver::class.java)
        broadcastIntent.putExtra("message","지금 출발하지 않으면 지각입니다!!")
        sendBroadcast(broadcastIntent)
    }
}