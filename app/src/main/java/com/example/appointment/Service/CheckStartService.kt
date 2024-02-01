package com.example.appointment.Service

import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.Manifest
import android.content.pm.PackageManager
import android.location.Location
import android.util.Log
import androidx.core.content.ContextCompat
import com.example.appointment.Receiver.StartCheckAlarmReceiver
import com.example.appointment.APIService.TMapDistanceRoute
import com.example.appointment.ViewModel.MainViewmodel
import com.example.appointment.model.DistanceRouteResponse
import com.google.android.gms.location.LocationServices
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class CheckStartService : Service() {

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        val fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        val startX = intent?.getStringExtra("startx")
        val startY = intent?.getStringExtra("starty")

        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED &&
            ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
        }

        fusedLocationClient.lastLocation
            .addOnSuccessListener { location: Location? ->
                if (location != null) {
                    val latitude = location.latitude
                    val longitude = location.longitude

                    fndistanceGet(startX!!,startY!!,longitude.toString(),latitude.toString(),intent)

                }
            }

        return super.onStartCommand(intent, flags, startId)
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    fun startCheck(distance : Int,intent:Intent){

        if(distance < 0){
            Log.w("TMapDistanceError", "좌표 확인 불가")
        }else if(distance < 100){

            val broadcastIntent = Intent(this,StartCheckAlarmReceiver::class.java)
            broadcastIntent.putExtra("message","출발 시간까지 5분 남았습니다.")
            sendBroadcast(broadcastIntent)

        }else{
            fnMeetingStart(intent)
        }

    }

    fun fnMeetingStart(intent : Intent){
        val auth = FirebaseAuth.getInstance()
        val database = FirebaseDatabase.getInstance()

        val meetingName = intent.getStringExtra("emailPath")
        val emailReplace = auth.currentUser?.email!!.replace(".","_")
        val splitArray = meetingName?.split("||")
        val reference = database.getReference("appointment").child(meetingName!!)
        if(splitArray!![0] == emailReplace){
            reference.child("email1StartCheck").setValue("start")
        }else if(splitArray!![1] == emailReplace){
            reference.child("email2StartCheck").setValue("start")
        }
    }



    fun fndistanceGet(startX : String, startY : String, endX : String, endY : String,intent: Intent){
        var distance : Int = -1

        val retrofit = Retrofit.Builder()
            .baseUrl(MainViewmodel.TMAP_BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val TMapDistanceApiService = retrofit.create(TMapDistanceRoute::class.java)

        val call = TMapDistanceApiService.getDistanceRoute(MainViewmodel.TMAP_API_KEY,"1","DistanceRouteResponse", startX, startY, endX, endY)

        call.enqueue(object : Callback<DistanceRouteResponse> {
            override fun onResponse(call: Call<DistanceRouteResponse>, response: Response<DistanceRouteResponse>) {
                if (response.isSuccessful) {

                    distance = response.body()!!.distanceInfo.distance.toInt()

                    startCheck(distance,intent)
                } else {
                    Log.w("TMapDistanceAPI", "호출 실패: ${response.code()}")
                }
            }
            override fun onFailure(call: Call<DistanceRouteResponse>, t: Throwable) {
                Log.w("TMapDistanceAPI", "통신 실패: ${t.message}")
            }
        })
    }
}


//val intentFilter = IntentFilter("com.example.myapp.MY_BROADCAST_ACTION")
//            registerReceiver(receiver, intentFilter)
//
//            val alarmIntent = Intent(this,StartCheckAlarmReceiver::class.java)
//            sendBroadcast(alarmIntent)