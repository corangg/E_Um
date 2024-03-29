package com.example.appointment.ui.fragment.schedule

import android.content.Intent
import android.graphics.PorterDuff
import android.text.InputFilter
import android.widget.EditText
import androidx.appcompat.widget.AppCompatButton
import androidx.core.content.ContextCompat
import androidx.databinding.ViewDataBinding
import com.example.appointment.`object`.AlarmUtil
import com.example.appointment.R
import com.example.appointment.data.RequestCode
import com.example.appointment.data.AlarmData
import com.example.appointment.data.YYYYMMDDhhmm
import com.example.appointment.data.SelectTransport
import com.example.appointment.data.StartCheckAlarmData
import com.example.appointment.receiver.AlarmReceiver
import com.example.appointment.receiver.StartAlarmReceiver
import com.example.appointment.ui.fragment.BaseFragment

abstract class BaseSceduleSet_Fragment<B:ViewDataBinding>:BaseFragment<B>() {

    fun setMaxNumber(editText: EditText, maxValue: Int) {
        val inputFilter = InputFilter { source, start, end, dest, dstart, dend ->
            try {
                val input = (dest.toString() + source.toString()).toInt()
                if (input <= maxValue) {
                    null
                } else {
                    ""
                }
            } catch (e: NumberFormatException) {
                ""
            }
        }
        editText.filters = arrayOf(inputFilter)
    }

    fun AmPmSet(am:AppCompatButton, pm:AppCompatButton, it:Boolean){
        if(it){
            am.setBackgroundResource(R.drawable.btn_theme)
            pm.setBackgroundResource(R.drawable.btn_round_gray)
        }else{
            am.setBackgroundResource(R.drawable.btn_round_gray)
            pm.setBackgroundResource(R.drawable.btn_theme)
        }
    }

    fun selectTransportActivate(transport: SelectTransport, it:Int){
        val selectColor = ContextCompat.getColor(requireContext(), R.color.themecolor)
        val notSelectColor = ContextCompat.getColor(requireContext(), R.color.gary)

        when(it){
            1->{
                transport.imgBus.setColorFilter(selectColor, PorterDuff.Mode.SRC_IN)
                transport.imgCar.setColorFilter(notSelectColor, PorterDuff.Mode.SRC_IN)
                transport.imgWalk.setColorFilter(notSelectColor, PorterDuff.Mode.SRC_IN)

                transport.textBus.setTextColor(selectColor)
                transport.textCar.setTextColor(notSelectColor)
                transport.textWalk.setTextColor(notSelectColor)
            }
            2->{
                transport.imgBus.setColorFilter(notSelectColor, PorterDuff.Mode.SRC_IN)
                transport.imgCar.setColorFilter(selectColor, PorterDuff.Mode.SRC_IN)
                transport.imgWalk.setColorFilter(notSelectColor, PorterDuff.Mode.SRC_IN)

                transport.textBus.setTextColor(notSelectColor)
                transport.textCar.setTextColor(selectColor)
                transport.textWalk.setTextColor(notSelectColor)
            }
            3->{
                transport.imgBus.setColorFilter(notSelectColor, PorterDuff.Mode.SRC_IN)
                transport.imgCar.setColorFilter(notSelectColor, PorterDuff.Mode.SRC_IN)
                transport.imgWalk.setColorFilter(selectColor, PorterDuff.Mode.SRC_IN)

                transport.textBus.setTextColor(notSelectColor)
                transport.textCar.setTextColor(notSelectColor)
                transport.textWalk.setTextColor(selectColor)
            }
        }
    }

    fun setAlarm(alarmData: AlarmData, alarmTime: YYYYMMDDhhmm){
        val intent = Intent(context, AlarmReceiver::class.java)
        intent.putExtra("address", alarmData.address)
        intent.putExtra("nickname", alarmData.nickname)
        intent.putExtra("startTime", alarmData.startTime)

        AlarmUtil.settingAlarm(requireContext(),intent, RequestCode.SET_ALARM_REQUEST_CODE, alarmTime,0)
    }

    fun setStartAlarm(startAlarmData : StartCheckAlarmData, alarmTime: YYYYMMDDhhmm){
        val intent = Intent(context, StartAlarmReceiver::class.java)
        intent.putExtra("startX",startAlarmData.startX)
        intent.putExtra("startY",startAlarmData.startY)
        intent.putExtra("emailPath",startAlarmData.meetingName)
        AlarmUtil.settingAlarm(requireContext(), intent, RequestCode.SET_START_ALARM_REQUEST_CODE, alarmTime, 0)
    }
}