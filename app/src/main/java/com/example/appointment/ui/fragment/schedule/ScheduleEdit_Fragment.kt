package com.example.appointment.ui.fragment.schedule

import androidx.fragment.app.FragmentManager
import androidx.fragment.app.activityViewModels
import com.example.appointment.R
import com.example.appointment.`object`.ToastMessage
import com.example.appointment.databinding.FragmentScheduleEditBinding
import com.example.appointment.data.AlarmData
import com.example.appointment.data.SelectTransport
import com.example.appointment.data.StartCheckAlarmData
import com.example.appointment.viewmodel.MainViewModel

class ScheduleEdit_Fragment : BaseSceduleSet_Fragment<FragmentScheduleEditBinding>() {
    private val mainViewmodel: MainViewModel by activityViewModels()

    override fun layoutResId(): Int {
        return R.layout.fragment_schedule_edit_
    }

    override fun initializeUI() {
        binding.viewmodel = mainViewmodel
        binding.scheduleSet.setOnClickListener {}

        setMaxNumber(binding.editAlarmHH,23)
        setMaxNumber(binding.editAlarmMM,59)
    }

    override fun setObserve(){
        mainViewmodel.scheduleAmPmSet.observe(viewLifecycleOwner){
            AmPmSet(binding.btnAm,binding.btnPm,it)
        }

        mainViewmodel.selectTransport.observe(viewLifecycleOwner){
            val transportView = SelectTransport(
                binding.imgBus,
                binding.imgCar,
                binding.imgWalk,
                binding.textBus,
                binding.textCar,
                binding.textWalk
            )
            selectTransportActivate(transportView,it)
        }

        mainViewmodel.transportCheck.observe(viewLifecycleOwner){
            ToastMessage.transportCheck(it,requireContext())
        }

        mainViewmodel.meetingTimeOver.observe(viewLifecycleOwner){
            if(it){
                ToastMessage.overdue(requireContext())
            }
        }

        mainViewmodel.alarmSet.observe(viewLifecycleOwner){
            if (it){
                val alarmData = AlarmData(
                    mainViewmodel.selectFriendProfile.nickname,
                    mainViewmodel.meetingPlaceAddress.value!!,
                    mainViewmodel.startTime)
                setAlarm(alarmData,mainViewmodel.scheduleAlarmTime)
            }
        }

        mainViewmodel.scheduleEditSuccess.observe(viewLifecycleOwner){
            if (it){
                val startCheckData = StartCheckAlarmData(
                    mainViewmodel.startX,
                    mainViewmodel.startY,
                    mainViewmodel.scheduleEmailPath)
                setStartAlarm(startCheckData, mainViewmodel.scheduleStartAlarmTime)
                toast("수정 완료")
                val fragmentManager = requireActivity().supportFragmentManager
                fragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE)
            }
        }
    }
}