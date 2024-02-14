package com.example.appointment.data

import android.icu.text.SimpleDateFormat
import android.util.Log
import com.example.appointment.model.AlarmTime
import com.example.appointment.model.ScheduleTime
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import org.checkerframework.checker.units.qual.mm
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.Date
import java.util.Locale

//@Inject constructor()
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

    fun fnTimeSet(/*hh:String,mm:String*/scheduleTime: ScheduleTime):String{
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
    }//시간하고 분 스트링으로 붙이기

    fun fnAlarmYYYYMMDDhhmm(alarmTime: AlarmTime):String{
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




    /*  interface DataMapCallback {
        fun onDataMapReceived(dataMap: Map<String, Any>?)
    }

    fun readDataFirebase(docRef: DocumentReference, callback: DataMapCallback) {

        docRef.get().addOnSuccessListener { documentSnapshot ->
            if (documentSnapshot.exists()) {
                val dataMap = documentSnapshot.data
                callback.onDataMapReceived(dataMap as? Map<String, Any>)
            } else {
                callback.onDataMapReceived(null)
            }
        }
    }*///코루틴 안사용한 버전인데 흠... 일단 혹시 모르니 두자
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

    /*fun readDataFRDaddListenerForSingleValueEvent(reference: DatabaseReference, onChildAddedAction: (DataSnapshot) -> Unit){
        reference.addListenerForSingleValueEvent(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                *//*if(snapshot.exists()){

                }else{

                }*//*
                onChildAddedAction(snapshot)

            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })

    }*///계속 오류나서 일단 지워둠

}