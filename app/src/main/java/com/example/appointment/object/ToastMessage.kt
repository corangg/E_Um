package com.example.appointment.`object`

import android.content.Context
import android.view.LayoutInflater
import android.widget.TextView
import android.widget.Toast
import com.example.appointment.R

object ToastMessage {
    fun transportCheck(it:Int,context: Context){
        when(it){
            1-> toast(context,"AmPm을 설정해 주세요.")
            2-> toast(context,"시간을 입력해 주세요.")
            3-> toast(context,"약속장소를 입력해 주세요.")
            4-> toast(context,"주소가 정확한지 확인해 주세요.")
            5-> toast(context,"거리가 너무 가깝습니다.")
            6-> toast(context,"교통수단을 입력해 주세요.")
        }
    }

    fun overdue(context: Context){
        toast(context,"약속시간이 이미 지났습니다.")
    }

    fun mismatchPassword(context: Context){
        toast(context,"비밀번호가 일치하지 않습니다.")
    }

    fun toast(context: Context, message : String){
        val inflater = LayoutInflater.from(context)
        val layout = inflater.inflate(R.layout.toast_layout, null)
        val text = layout.findViewById<TextView>(R.id.text)
        text.text = message
        val toast = Toast(context)
        toast.view = layout
        toast.duration = Toast.LENGTH_SHORT
        toast.show()
    }
}