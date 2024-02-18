package com.example.appointment.repository

import androidx.lifecycle.MutableLiveData
import com.example.appointment.data.Utils
import com.example.appointment.data.Utils.Companion.auth
import com.example.appointment.data.ScheduleDate
import com.example.appointment.data.ScheduleDateTime
import com.example.appointment.data.ScheduleSet
import com.example.appointment.data.ScheduleTime
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener

class ScheduleFragmentRepository(private val utils: Utils) {
    val scheduleDataList : MutableLiveData<MutableList<ScheduleSet>> = MutableLiveData()
    val scheduleAlarmDataList : MutableLiveData<MutableList<ScheduleSet>> = MutableLiveData(mutableListOf())

    fun FRDPathSplit(emailPath:String):Int{
        val userEmail = auth.currentUser?.email

        val emailPathReplace = emailPath.replace("_",".")
        var splitArray = emailPathReplace?.split("||")
        if(userEmail == splitArray!![0]){
            return 1
        }else if(userEmail == splitArray[1]){
            return 2
        }
        return 0
    }

    fun scheduleTimeSplit(date:String) : ScheduleDateTime {
        val YYYY = date.substring(0,4)
        val MM = date.substring(4,6)
        val DD = date.substring(6,8)
        var hh = date.substring(8,10)
        val mm = date.substring(10,12)
        var ampm : Boolean

        if(hh.toInt()<12){
            ampm = true
        }else{
            val pmhh = hh.toInt() - 12
            hh = pmhh.toString()
            ampm = false
        }

        val date = ScheduleDate(YYYY, MM, DD)
        val time = ScheduleTime(hh, mm)
        val dateTime = ScheduleDateTime(date, time ,ampm)

        return dateTime
    }

    fun deleteSchedule(reference: DatabaseReference){
        reference.removeValue().addOnSuccessListener {
            getScheduleListData()
        }
    }

    fun getScheduleListData(){
        val scheduleList = mutableListOf<ScheduleSet>()
        val scheduleAlarmList = mutableListOf<ScheduleSet>()

        val reference = Utils.database.getReference("appointment")

        reference.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if (dataSnapshot.exists()) {
                    utils.readDataFRDAddChildEventListener(reference){
                        val emailPath = it.key
                        if(FRDPathSplit(emailPath!!) == 1){
                            val scheduleData = ScheduleSet(
                                emailPath,
                                it.child("email2").value.toString(),
                                it.child("nickname2").value.toString(),
                                it.child("email2ProfileImgURl").value.toString(),
                                it.child("meetingTime").value.toString(),
                                it.child("meetingPlace").value.toString(),
                                it.child("meetingPlaceName").value.toString(),
                                it.child("email2Status").value.toString(),
                                it.child("meetingplaceX").value.toString(),
                                it.child("meetingplaceY").value.toString(),
                                it.child("email2Alarm").value.toString(),
                                it.child("email2Transport").value.toString(),
                                it.child("email2StartX").value.toString(),
                                it.child("email2StartY").value.toString(),
                                it.child("email2TransportTime").value.toString(),
                                it.child("email1Transport").value.toString(),
                                it.child("email1Alarm").value.toString(),
                            )
                            if(it.child("email1Status").value.toString() == "consent"){
                                scheduleList.add(scheduleData)
                                scheduleDataList.value = scheduleList
                                scheduleAlarmDataList.value = scheduleAlarmList
                            }else if(it.child("email1Status").value.toString() == "wait"){
                                scheduleAlarmList.add(scheduleData)
                                scheduleAlarmDataList.value = scheduleAlarmList
                            }
                        }else if(FRDPathSplit(emailPath!!) == 2){
                            val scheduleData = ScheduleSet(
                                emailPath,
                                it.child("email1").value.toString(),
                                it.child("nickname1").value.toString(),
                                it.child("email1ProfileImgURl").value.toString(),
                                it.child("meetingTime").value.toString(),
                                it.child("meetingPlace").value.toString(),
                                it.child("meetingPlaceName").value.toString(),
                                it.child("email1Status").value.toString(),
                                it.child("meetingplaceX").value.toString(),
                                it.child("meetingplaceY").value.toString(),
                                it.child("email1Alarm").value.toString(),
                                it.child("email1Transport").value.toString(),
                                it.child("email1StartX").value.toString(),
                                it.child("email1StartY").value.toString(),
                                it.child("email1TransportTime").value.toString(),
                                it.child("email2Transport").value.toString(),
                                it.child("email2Alarm").value.toString(),
                            )
                            if(it.child("email2Status").value.toString() == "consent"){
                                scheduleList.add(scheduleData)
                                scheduleDataList.value = scheduleList
                                scheduleAlarmDataList.value = scheduleAlarmList
                            }else if(it.child("email2Status").value.toString() == "wait"){
                                scheduleAlarmList.add(scheduleData)
                                scheduleAlarmDataList.value = scheduleAlarmList
                            }
                        }else{
                            scheduleDataList.value = scheduleList
                            scheduleAlarmDataList.value = scheduleAlarmList
                        }
                    }
                } else {
                    scheduleDataList.value = scheduleList
                    scheduleAlarmDataList.value = scheduleAlarmList
                }
            }override fun onCancelled(error: DatabaseError) {}
        })
    }
}