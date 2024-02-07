package com.example.appointment.service

import android.Manifest
import android.app.Service
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.os.IBinder
import android.util.Log
import androidx.core.content.ContextCompat
import com.example.appointment.apiservice.APIData
import com.example.appointment.apiservice.TMapDistanceRoute
import com.example.appointment.data.Utils
import com.example.appointment.data.Utils.Companion.auth
import com.example.appointment.data.Utils.Companion.database
import com.example.appointment.model.DistanceRouteResponse
import com.example.appointment.receiver.StartCheckAlarmReceiver
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

abstract class BaseStartService : Service(){
    val utils = Utils()

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

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
        ){
            fusedLocationClient.lastLocation.addOnSuccessListener { location: Location? ->
                    if (location != null) {
                        val latitude = location.latitude
                        val longitude = location.longitude

                        fndistanceGet(startX!!,startY!!,longitude.toString(),latitude.toString(),intent)
                    }
            }
        }

        return super.onStartCommand(intent, flags, startId)
    }


    fun fndistanceGet(startX : String, startY : String, endX : String, endY : String,intent: Intent){
        var distance : Int = -1

        val retrofit = Retrofit.Builder()
            .baseUrl(APIData.TMAP_BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val TMapDistanceApiService = retrofit.create(TMapDistanceRoute::class.java)

        val call = TMapDistanceApiService.getDistanceRoute(APIData.TMAP_API_KEY,"1","DistanceRouteResponse", startX, startY, endX, endY)

        utils.getRetrofitData(call){
            if(it!=null){
                distance = it.distanceInfo.distance.toInt()
                startCheck(distance,intent)
            }
        }
    }

    fun startCheck(distance : Int,intent:Intent){

        if(distance < 0){
            Log.w("TMapDistanceError", "좌표 확인 불가")
        }else if(distance < 100){

            startBroadcast(intent)

        }else{
            fnSendStartMessage(intent,"출발했습니다.")
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


    fun fnSendStartMessage(intent : Intent,message:String){//만약 메세지 하나도 없으면?
        val chatName : String = intent.getStringExtra("emailPath")!!
        val reversedchatName = chatName.split("||").reversed().joinToString("||")
        val reference = database.getReference("chat")
        reference.child(chatName).addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    val chatPath = reference.child(chatName).child(utils.fnGetCurrentTimeString())
                    val dataMap = mapOf(
                        "message" to message,
                        "senderemail" to auth.currentUser?.email
                    )
                    chatPath.setValue(dataMap)
                }else{
                    reference.child(reversedchatName).addListenerForSingleValueEvent(object :
                        ValueEventListener {
                        override fun onDataChange(snapshot: DataSnapshot) {
                            if (snapshot.exists()) {
                                val chatPath = reference.child(reversedchatName).child(utils.fnGetCurrentTimeString())
                                val dataMap = mapOf(
                                    "message" to message,
                                    "senderemail" to auth.currentUser?.email
                                )
                                chatPath.setValue(dataMap)
                            }
                        }
                        override fun onCancelled(error: DatabaseError) {}
                    })
                }
            }
            override fun onCancelled(error: DatabaseError) {}
        })
    }

    abstract fun startBroadcast(intent: Intent)
}