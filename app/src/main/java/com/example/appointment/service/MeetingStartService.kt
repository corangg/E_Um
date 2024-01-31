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
import com.example.appointment.FirebaseCertified.Companion.auth
import com.example.appointment.Receiver.StartCheckAlarmReceiver
import com.example.appointment.TMapDistanceRoute
import com.example.appointment.main.MainViewmodel
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

class MeetingStartService : Service() {
    val auth = FirebaseAuth.getInstance()
    val database = FirebaseDatabase.getInstance()

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

            fnSendStartMessage(intent,"출발하지 않았습니다. 따로 연락해 보세요.")
            val broadcastIntent = Intent(this, StartCheckAlarmReceiver::class.java)
            broadcastIntent.putExtra("message","지금 출발하지 않으면 지각입니다!!")
            sendBroadcast(broadcastIntent)


        }else{
            fnMeetingStart(intent)
            fnSendStartMessage(intent,"출발했습니다.")
        }

    }

    fun fnSendStartMessage(intent : Intent,message:String){
        val chatName : String = intent.getStringExtra("emailPath")!!
        val reversedchatName = chatName.split("||").reversed().joinToString("||")
        val reference = database.getReference("chat")
        reference.child(chatName).addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    val chatPath = reference.child(chatName).child(fnGetCurrentTimeString())
                    val dataMap = mapOf(
                        "message" to message,
                        "senderemail" to auth.currentUser?.email,
                        "sendernickname" to "",
                    )
                    chatPath.setValue(dataMap)
                }else{
                    reference.child(reversedchatName).addListenerForSingleValueEvent(object : ValueEventListener{
                        override fun onDataChange(snapshot: DataSnapshot) {
                            if (snapshot.exists()) {
                                val chatPath = reference.child(reversedchatName).child(fnGetCurrentTimeString())
                                val dataMap = mapOf(
                                    "message" to message,
                                    "senderemail" to auth.currentUser?.email,
                                    "sendernickname" to "",
                                )
                                chatPath.setValue(dataMap)
                            }
                        }

                        override fun onCancelled(error: DatabaseError) {
                            TODO("Not yet implemented")
                        }
                    })
                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })
    }

    fun fnGetCurrentTimeString(): String {
        val dateFormat = SimpleDateFormat("yyyyMMddHHmmss", Locale.getDefault())
        val currentDate = Date(System.currentTimeMillis())
        return dateFormat.format(currentDate)
    }

    fun fnMeetingStart(intent : Intent){

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