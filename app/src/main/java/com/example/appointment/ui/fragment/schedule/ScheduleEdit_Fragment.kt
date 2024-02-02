package com.example.appointment.ui.fragment.schedule

import android.graphics.PorterDuff
import android.os.Bundle
import android.text.InputFilter
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.activityViewModels
import com.example.appointment.AlarmUtil
import com.example.appointment.R
import com.example.appointment.databinding.FragmentScheduleEditBinding
import com.example.appointment.viewmodel.MainViewmodel
import com.example.appointment.model.StartCheckAlarmData
import com.example.appointment.ui.fragment.BaseFragment

class ScheduleEdit_Fragment : BaseFragment<FragmentScheduleEditBinding>() {
    private val mainViewmodel: MainViewmodel by activityViewModels()

    override fun layoutResId(): Int {
        return R.layout.fragment_schedule_edit_
    }

    override fun initializeUI() {
        binding.viewmodel = mainViewmodel
        binding.scheduleSet.setOnClickListener {}



        setMaxNumber(binding.editAlarmHH,23)
        setMaxNumber(binding.editAlarmMM,59)
    }

    private fun setMaxNumber(editText: EditText, maxValue: Int) {
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
    override fun setObserve(){
        mainViewmodel.scheduleAmPmSet.observe(viewLifecycleOwner){
            if(it){
                binding.btnAm.setBackgroundResource(R.drawable.btn_theme)
                binding.btnPm.setBackgroundResource(R.drawable.btn_round_gray)
            }else{
                binding.btnAm.setBackgroundResource(R.drawable.btn_round_gray)
                binding.btnPm.setBackgroundResource(R.drawable.btn_theme)
            }
        }

        mainViewmodel.selectTransport.observe(viewLifecycleOwner){
            val selectColor = ContextCompat.getColor(requireContext(), R.color.themecolor)
            val notSelectColor = ContextCompat.getColor(requireContext(), R.color.gary)
            when(it){
                1->{
                    binding.imgBus.setColorFilter(selectColor, PorterDuff.Mode.SRC_IN)
                    binding.imgCar.setColorFilter(notSelectColor, PorterDuff.Mode.SRC_IN)
                    binding.imgWalk.setColorFilter(notSelectColor, PorterDuff.Mode.SRC_IN)

                    binding.textBus.setTextColor(selectColor)
                    binding.textCar.setTextColor(notSelectColor)
                    binding.textWalk.setTextColor(notSelectColor)
                }
                2->{
                    binding.imgBus.setColorFilter(notSelectColor, PorterDuff.Mode.SRC_IN)
                    binding.imgCar.setColorFilter(selectColor, PorterDuff.Mode.SRC_IN)
                    binding.imgWalk.setColorFilter(notSelectColor, PorterDuff.Mode.SRC_IN)

                    binding.textBus.setTextColor(notSelectColor)
                    binding.textCar.setTextColor(selectColor)
                    binding.textWalk.setTextColor(notSelectColor)
                }
                3->{
                    binding.imgBus.setColorFilter(notSelectColor, PorterDuff.Mode.SRC_IN)
                    binding.imgCar.setColorFilter(notSelectColor, PorterDuff.Mode.SRC_IN)
                    binding.imgWalk.setColorFilter(selectColor, PorterDuff.Mode.SRC_IN)

                    binding.textBus.setTextColor(notSelectColor)
                    binding.textCar.setTextColor(notSelectColor)
                    binding.textWalk.setTextColor(selectColor)
                }
            }
        }

        mainViewmodel.publicTransportCheck.observe(viewLifecycleOwner){
            when(it){
                1-> Toast.makeText(activity,"AmPm을 설정해 주세요.", Toast.LENGTH_SHORT).show()
                2-> Toast.makeText(activity,"시간을 입력해 주세요.", Toast.LENGTH_SHORT).show()
                3-> Toast.makeText(activity,"약속장소를 입력해 주세요.", Toast.LENGTH_SHORT).show()
                4-> Toast.makeText(activity,"주소가 정확한지 확인해 주세요.", Toast.LENGTH_SHORT).show()
                5-> Toast.makeText(activity,"거리가 너무 가깝습니다.", Toast.LENGTH_SHORT).show()
            }
        }

        mainViewmodel.carCheck.observe(viewLifecycleOwner){
            when(it){
                1-> Toast.makeText(activity,"AmPm을 설정해 주세요.", Toast.LENGTH_SHORT).show()
                2-> Toast.makeText(activity,"시간을 입력해 주세요.", Toast.LENGTH_SHORT).show()
                3-> Toast.makeText(activity,"약속장소를 입력해 주세요.", Toast.LENGTH_SHORT).show()
                4-> Toast.makeText(activity,"주소가 정확한지 확인해 주세요.", Toast.LENGTH_SHORT).show()
            }
        }

        mainViewmodel.walkCheck.observe(viewLifecycleOwner){
            when(it){
                1-> Toast.makeText(activity,"AmPm을 설정해 주세요.", Toast.LENGTH_SHORT).show()
                2-> Toast.makeText(activity,"시간을 입력해 주세요.", Toast.LENGTH_SHORT).show()
                3-> Toast.makeText(activity,"약속장소를 입력해 주세요.", Toast.LENGTH_SHORT).show()
                4-> Toast.makeText(activity,"주소가 정확한지 확인해 주세요.", Toast.LENGTH_SHORT).show()
            }
        }

        mainViewmodel.scheduleEditSuccess.observe(viewLifecycleOwner){
            if (it){

                val startCheckData = StartCheckAlarmData(
                    mainViewmodel.startX.value!!,
                    mainViewmodel.startY.value!!,
                    mainViewmodel.scheduleEmailPath.value!!
                )

                AlarmUtil.setAlarm(requireContext(),mainViewmodel.fnAlarmTimeSet(),mainViewmodel.selectScheduleNickname.value!!)
                AlarmUtil.checkStartMeeting(requireContext(),mainViewmodel.fnCheckStartAlarmTimeSet(),startCheckData,mainViewmodel.startCheckAlarmTime.value!!)
                AlarmUtil.startMeeting(requireContext(),mainViewmodel.fnCheckStartAlarmTimeSet(),startCheckData)

                val fragmentManager = requireActivity().supportFragmentManager
                fragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE)
            }
        }

        mainViewmodel.meetingTimeOver.observe(viewLifecycleOwner){
            if(it){
                Toast.makeText(activity,"약속시간이 이미 지났습니다.", Toast.LENGTH_SHORT).show()
            }
        }
    }

}