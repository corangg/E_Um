package com.example.appointment.service

import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.Manifest
import android.content.pm.PackageManager
import android.location.Location
import android.util.Log
import androidx.core.content.ContextCompat
import com.example.appointment.apiservice.APIData
import com.example.appointment.receiver.StartCheckAlarmReceiver
import com.example.appointment.apiservice.TMapDistanceRoute
import com.example.appointment.model.DistanceRouteResponse
import com.google.android.gms.location.LocationServices
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class CheckStartService : BaseStartService() {

    override fun startBroadcast(intent: Intent) {
        val broadcastIntent = Intent(this,StartCheckAlarmReceiver::class.java)
        broadcastIntent.putExtra("message","출발 시간까지 5분 남았습니다.")
        sendBroadcast(broadcastIntent)
    }
}
