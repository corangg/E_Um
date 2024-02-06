package com.example.appointment.ui.fragment.schedule

import android.graphics.PorterDuff
import android.text.InputFilter
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.widget.AppCompatButton
import androidx.core.content.ContextCompat
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.activityViewModels
import com.example.appointment.AlarmUtil
import com.example.appointment.R
import com.example.appointment.data.ToastMessage
import com.example.appointment.model.AlarmTime
import com.example.appointment.model.SelectTransport
import com.example.appointment.model.StartCheckAlarmData
import com.example.appointment.ui.fragment.BaseFragment
import com.example.appointment.viewmodel.MainViewmodel
import com.example.appointment.viewmodel.profile.ProfileViewModel

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

    fun setAlarm(startCheckData : StartCheckAlarmData, alarmTime: AlarmTime, startTime: AlarmTime, nickName:String,alarmCheckTime:Int){
        AlarmUtil.setAlarm(requireContext(),alarmTime,nickName)
        AlarmUtil.checkStartMeeting(requireContext(),startTime,startCheckData,alarmCheckTime)
        AlarmUtil.startMeeting(requireContext(),startTime,startCheckData)
    }











}