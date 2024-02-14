package com.example.appointment.repository

import androidx.lifecycle.MutableLiveData
import com.example.appointment.data.Utils
import com.example.appointment.model.YYYYMMDDhhmm
import com.example.appointment.model.ScheduleDate
import com.example.appointment.model.ScheduleTime
import com.google.firebase.database.DatabaseReference
import com.prolificinteractive.materialcalendarview.CalendarDay

class ScheduleSetFragmentRepository(private val utils: Utils) {

    val scheduleDate : MutableLiveData<String> = MutableLiveData("")
    //val scheduleYYYYMMDD : MutableLiveData<String> = MutableLiveData("")

    fun scheduleSet(date: CalendarDay):ScheduleDate{
        val year = date.year
        val month = date.month
        val day = date.day

        var monthString = "00"
        var dayString = "00"

        if(month<10){
            monthString = "0"+month.toString()
        }else{
            monthString = month.toString()
        }
        if(day<10){
            dayString = "0"+day.toString()
        }else{
            dayString = day.toString()
        }
        val scheduleDate = ScheduleDate(year.toString(), monthString, dayString)

        return scheduleDate
    }


    fun dttmSet(ampm:Boolean?, scheduleTime: ScheduleTime, date : String):String{
        var hourint = 0

        if(ampm == true){
            if(scheduleTime.HH == ""){
                hourint = 0
            }else{
                hourint = scheduleTime.HH.toInt()
            }
        }else{
            if(scheduleTime.HH == ""){
                hourint = 12
            }else{
                hourint = scheduleTime.HH.toInt() + 12
            }
        }

        val scheduleTimeData = ScheduleTime(hourint.toString(), scheduleTime.MM)
        val Dttm :String = date + utils.fnTimeSet(scheduleTimeData)
        return Dttm
    }

    fun alarmTimeSet(ampm:Boolean, scheduleTime: ScheduleTime, scheduleAlarmTime: ScheduleTime, transportTime:Int, date: String): YYYYMMDDhhmm {
        var alarmTime : YYYYMMDDhhmm

        var YYYY : Int = date.substring(0,4).toInt()
        var MM : Int = date.substring(4,6).toInt()
        var DD : Int = date.substring(6,8).toInt()
        var hh : Int = 0
        var mm : Int = 0
        val transportMin = transportTime.div(60)
        var alarmhh : Int = 0
        var alarmmm : Int = 0

        if(scheduleTime.HH != ""){
            if(ampm == true){
                hh = scheduleTime.HH.toInt()
            }else{
                hh = scheduleTime.HH.toInt() + 12
            }
        }
        if(scheduleTime.MM != ""){
            mm = scheduleTime.MM.toInt()
        }

        if (scheduleAlarmTime.HH != ""){
            alarmhh = scheduleAlarmTime.HH.toInt()
        }

        if (scheduleAlarmTime.MM != ""){
            alarmmm = scheduleAlarmTime.MM.toInt()
        }

        mm = mm - transportMin.rem(60) - alarmmm
        if(mm < 0){
            mm = mm + 60
            hh = hh - 1
            if(mm<0){
                mm = mm + 60
                hh = hh - 1
            }
        }

        hh = hh - transportMin.div(60) - alarmhh

        if(hh < 0){
            hh = hh + 24
            DD = DD -1
        }

        if(DD < 1){
            MM = MM - 1
            when(MM){
                0,1,3,5,7,8,10,12 -> DD = DD + 31
                4,6,9,11 -> DD = DD + 30
                2->{
                    if(YYYY.rem(4)==0){
                        DD = DD + 29
                    }else{
                        DD = DD + 28
                    }
                }
            }
        }

        if(MM < 1){
            MM = MM + 12
            YYYY = YYYY - 1
        }

        alarmTime = YYYYMMDDhhmm(YYYY, MM, DD, hh, mm)

        return alarmTime
    }

    fun sendScheduleRequest(dataMap:Map<String,*>,reference: DatabaseReference, onSuccess: (Boolean) -> Unit){
        reference.setValue(dataMap).addOnSuccessListener {
            onSuccess(true)
        }
    }
}