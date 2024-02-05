package com.example.appointment.data

import android.content.Context
import android.widget.Toast

class ToastMessage {

    fun transportCheck(it:Int,context: Context){
        when(it){
            1-> Toast.makeText(context,"AmPm을 설정해 주세요.", Toast.LENGTH_SHORT).show()
            2-> Toast.makeText(context,"시간을 입력해 주세요.", Toast.LENGTH_SHORT).show()
            3-> Toast.makeText(context,"약속장소를 입력해 주세요.", Toast.LENGTH_SHORT).show()
            4-> Toast.makeText(context,"주소가 정확한지 확인해 주세요.", Toast.LENGTH_SHORT).show()
            5-> Toast.makeText(context,"거리가 너무 가깝습니다.", Toast.LENGTH_SHORT).show()
        }
    }

    fun overdue(context: Context){
        Toast.makeText(context,"약속시간이 이미 지났습니다.", Toast.LENGTH_SHORT).show()
    }
}