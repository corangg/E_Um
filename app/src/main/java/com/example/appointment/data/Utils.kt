package com.example.appointment.data

import android.icu.text.SimpleDateFormat
import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.Date
import java.util.Locale

class Utils {
    companion object{
        val auth = FirebaseAuth.getInstance()
        val firestore = FirebaseFirestore.getInstance()
        val storage = FirebaseStorage.getInstance()
        val database = FirebaseDatabase.getInstance()
    }

    fun reverseString(data:String,reversePoint:String):String{
        return data.split(reversePoint).reversed().joinToString (reversePoint)
    }

    fun fnGetCurrentTimeString(): String {
        val dateFormat = SimpleDateFormat("yyyyMMddHHmmss", Locale.getDefault())
        val currentDate = Date(System.currentTimeMillis())
        return dateFormat.format(currentDate)
    }

    fun <T> getRetrofitData(call: Call<T>, callback: (T?) -> Unit) {
        call.enqueue(object : Callback<T> {
            override fun onResponse(call: Call<T>, response: Response<T>) {
                if (response.isSuccessful) {
                    callback(response.body())
                } else {
                    callback(null)
                }
            }

            override fun onFailure(call: Call<T>, t: Throwable) {
            }
        })
    }

    fun fnTimeToString(time : Int):String{
        var HM :String = ""
        val min = time.div(60)
        val quotient = min.div(60)
        val remainder = min.rem(60)
        HM = quotient.toString() + "시간" + remainder + "분"

        return  HM
    }//이동시간 초를 시간 분 텍스트로

    fun fnTimeSet(scheduleTime: ScheduleTime):String{
        var hour : String = ""
        var min : String = ""
        var time : String = ""
        if(scheduleTime.HH == "" && scheduleTime.MM == ""){
            hour = "00"
            min = "00"
        }else if(scheduleTime.HH == "" && scheduleTime.MM != ""){
            hour = "00"
            if(scheduleTime.MM.toInt()<10){
                min = "0" + scheduleTime.MM
            }else{
                min = scheduleTime.MM
            }
        }else if(scheduleTime.HH != "" && scheduleTime.MM == ""){
            if(scheduleTime.HH.toInt()<10){
                hour = "0" + scheduleTime.HH.toInt().toString()
            }else{
                hour = scheduleTime.HH
            }
            min = "00"
        }else if(scheduleTime.HH != "" && scheduleTime.MM != ""){
            if(scheduleTime.HH.toInt()<10){
                hour = "0" + scheduleTime.HH.toInt().toString()
            }else{
                hour = scheduleTime.HH
            }
            if(scheduleTime.MM.toInt()<10){
                min = "0" + scheduleTime.MM.toInt().toString()
            }else{
                min = scheduleTime.MM
            }
        }

        time = hour + min
        return time
    }

    fun fnAlarmYYYYMMDDhhmm(alarmTime: YYYYMMDDhhmm):String{
        var alarmYYYYMMDDhhmm : String = ""

        var YYYY = alarmTime.YYYY.toString()
        var MM = alarmTime.MM.toString()
        var DD = alarmTime.DD.toString()
        var hh = alarmTime.hh.toString()
        var mm = alarmTime.mm.toString()

        if(alarmTime.MM < 10){
            MM = "0" + MM
        }
        if(alarmTime.DD < 10){
            DD = "0" + DD
        }
        if(alarmTime.hh < 10){
            hh = "0" + hh
        }
        if(alarmTime.mm < 10){
            mm = "0" + mm
        }

        alarmYYYYMMDDhhmm = YYYY + MM + DD + hh + mm

        return alarmYYYYMMDDhhmm
    }

    suspend fun readDataFirebase(docRef: DocumentReference): Map<String, Any>? {
        return withContext(Dispatchers.IO) {
            try {
                val documentSnapshot = docRef.get().await()
                if (documentSnapshot.exists()) {
                    documentSnapshot.data
                } else {
                    null
                }
            } catch (e: Exception) {
                Log.e("Coroutine", "Error fetching document data", e)
                null
            }
        }
    }

    fun readDataFRDAddChildEventListener(reference: DatabaseReference, onChildAddedAction: (DataSnapshot) -> Unit) {
        reference.addChildEventListener(object : ChildEventListener {
            override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                onChildAddedAction(snapshot)
            }

            override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {}
            override fun onChildRemoved(snapshot: DataSnapshot) {}
            override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {}
            override fun onCancelled(error: DatabaseError) {
                Log.e("Firebase", "Error reading data", error.toException())
            }
        })
    }

    fun updataDataFireBase(docRef: DocumentReference, field: String, data: String?){
        docRef.update(field,data)
    }
}