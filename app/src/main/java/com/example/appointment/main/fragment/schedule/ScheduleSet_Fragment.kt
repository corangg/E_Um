package com.example.appointment.main.fragment.schedule

import android.app.Activity
import android.content.Intent
import android.graphics.PorterDuff
import android.os.Bundle
import android.text.InputFilter
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.activity.addCallback
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.activityViewModels
import com.example.appointment.AlarmUtil
import com.example.appointment.R
import com.example.appointment.databinding.FragmentScheduleSetBinding
import com.example.appointment.main.MainViewmodel
import com.example.appointment.model.StartCheckAlarmData


class ScheduleSet_Fragment : Fragment() {
    val mainViewmodel: MainViewmodel by activityViewModels()
    lateinit var binding: FragmentScheduleSetBinding

    companion object{
        const val MAP_REQUEST_CODE =2921
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentScheduleSetBinding.inflate(inflater,container,false)
        binding.viewmodel = mainViewmodel
        binding.lifecycleOwner = this
        binding.scheduleSet.setOnClickListener {}

        setMaxNumber(binding.editHH,12)
        setMaxNumber(binding.editMM,59)
        setMaxNumber(binding.editAlarmHH,23)
        setMaxNumber(binding.editAlarmMM,59)

        mainViewmodel.fnStartingPointSet()//프래그멘트 열라고 할때 불러도 될듯?

        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner) {
            mainViewmodel.fnScheduleSetDataReset()

            if (!isHandlingBackPressed()) {
                isEnabled = false
                requireActivity().onBackPressed()
            }
        }


        setObserve()

        return binding.root
    }

    private fun isHandlingBackPressed(): Boolean {
        return false
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



    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == MAP_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            if (data != null) {
                val endX = data.getStringExtra("endX")
                val endY = data.getStringExtra("endY")
                val address = data.getStringExtra("address")
                val keywordName = data.getStringExtra("keywordName")
                mainViewmodel.meetingPlaceAddress.value = address
                mainViewmodel.endX.value = endX
                mainViewmodel.endY.value = endY
                mainViewmodel.kewordName.value = keywordName
            }
        }
    }

    private fun setObserve(){
        mainViewmodel.scheduleAmPmSet.observe(viewLifecycleOwner){
            if(it){
                binding.btnAm.setBackgroundResource(R.drawable.btn_theme)
                binding.btnPm.setBackgroundResource(R.drawable.btn_round_gray)
            }else{
                binding.btnAm.setBackgroundResource(R.drawable.btn_round_gray)
                binding.btnPm.setBackgroundResource(R.drawable.btn_theme)
            }
        }

        mainViewmodel.scheduleMapActivityStart.observe(viewLifecycleOwner){
            if(it){
                mainViewmodel.scheduleMapActivityStart.value = false
                val intent = Intent(requireContext(), ScheduleMapActivity::class.java)
                startActivityForResult(intent, MAP_REQUEST_CODE)
            }
        }

        mainViewmodel.selectTransport.observe(viewLifecycleOwner){
            val selectColor = ContextCompat.getColor(requireContext(), R.color.themecolor)
            val notSelectColor = ContextCompat.getColor(requireContext(), R.color.gary)
            when(it){
                1->{
                    binding.imgBus.setColorFilter(selectColor,PorterDuff.Mode.SRC_IN)
                    binding.imgCar.setColorFilter(notSelectColor,PorterDuff.Mode.SRC_IN)
                    binding.imgWalk.setColorFilter(notSelectColor,PorterDuff.Mode.SRC_IN)

                    binding.textBus.setTextColor(selectColor)
                    binding.textCar.setTextColor(notSelectColor)
                    binding.textWalk.setTextColor(notSelectColor)
                }
                2->{
                    binding.imgBus.setColorFilter(notSelectColor,PorterDuff.Mode.SRC_IN)
                    binding.imgCar.setColorFilter(selectColor,PorterDuff.Mode.SRC_IN)
                    binding.imgWalk.setColorFilter(notSelectColor,PorterDuff.Mode.SRC_IN)

                    binding.textBus.setTextColor(notSelectColor)
                    binding.textCar.setTextColor(selectColor)
                    binding.textWalk.setTextColor(notSelectColor)
                }
                3->{
                    binding.imgBus.setColorFilter(notSelectColor,PorterDuff.Mode.SRC_IN)
                    binding.imgCar.setColorFilter(notSelectColor,PorterDuff.Mode.SRC_IN)
                    binding.imgWalk.setColorFilter(selectColor,PorterDuff.Mode.SRC_IN)

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

        mainViewmodel.appointmentRequestSuccess.observe(viewLifecycleOwner){
            if(it){
                val fragmentManager = requireActivity().supportFragmentManager
                fragmentManager.popBackStack(null,FragmentManager.POP_BACK_STACK_INCLUSIVE)

                val startCheckData = StartCheckAlarmData(
                    mainViewmodel.startX.value!!,
                    mainViewmodel.startY.value!!,
                    mainViewmodel.scheduleEmailPath.value!!
                )

                AlarmUtil.setAlarm(requireContext(),mainViewmodel.fnAlarmTimeSet(),mainViewmodel.selectFriendProfile.value!!.nickname)
                AlarmUtil.checkStartMeeting(requireContext(),mainViewmodel.fnCheckStartAlarmTimeSet(),startCheckData,mainViewmodel.startCheckAlarmTime.value!!)
                AlarmUtil.startMeeting(requireContext(),mainViewmodel.fnCheckStartAlarmTimeSet(),startCheckData)

                Toast.makeText(activity,"${mainViewmodel.selectFriendProfile.value!!.nickname}님에게 약속 요청을 보냈습니다.",Toast.LENGTH_SHORT).show()
            }
        }

        mainViewmodel.meetingTimeOver.observe(viewLifecycleOwner){
            if(it){
                Toast.makeText(activity,"약속시간이 이미 지났습니다.",Toast.LENGTH_SHORT).show()
            }
        }
    }
}