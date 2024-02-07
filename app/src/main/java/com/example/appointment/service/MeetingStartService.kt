package com.example.appointment.service

import android.Manifest
import android.app.Service
import android.content.Intent
import android.content.pm.PackageManager
import android.icu.text.SimpleDateFormat
import android.location.Location
import android.os.IBinder
import android.util.Log
import androidx.core.content.ContextCompat
import com.example.appointment.apiservice.APIData
import com.example.appointment.receiver.StartCheckAlarmReceiver
import com.example.appointment.apiservice.TMapDistanceRoute
import com.example.appointment.model.DistanceRouteResponse
import com.google.android.gms.location.LocationServices
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.Date
import java.util.Locale

class MeetingStartService : BaseStartService() {

    override fun startBroadcast(intent: Intent) {
        fnSendStartMessage(intent,"출발하지 않았습니다. 따로 연락해 보세요.")
        val broadcastIntent = Intent(this, StartCheckAlarmReceiver::class.java)
        broadcastIntent.putExtra("message","지금 출발하지 않으면 지각입니다!!")
        sendBroadcast(broadcastIntent)
    }
}