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
import com.example.appointment.data.ToastMessage
import com.example.appointment.databinding.FragmentScheduleEditBinding
import com.example.appointment.model.SelectTransport
import com.example.appointment.viewmodel.MainViewmodel
import com.example.appointment.model.StartCheckAlarmData
import com.example.appointment.ui.fragment.BaseFragment
import com.example.appointment.viewmodel.profile.ProfileViewModel

class ScheduleEdit_Fragment : BaseSceduleSet_Fragment<FragmentScheduleEditBinding>() {
    private val mainViewmodel: ProfileViewModel by activityViewModels()

    val toast = ToastMessage()
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

        mainViewmodel.publicTransportCheck.observe(viewLifecycleOwner){
            toast.transportCheck(it,requireContext())
        }

        mainViewmodel.carCheck.observe(viewLifecycleOwner){
            toast.transportCheck(it,requireContext())
        }

        mainViewmodel.walkCheck.observe(viewLifecycleOwner){
            toast.transportCheck(it,requireContext())
        }

        mainViewmodel.meetingTimeOver.observe(viewLifecycleOwner){
            if(it){
                toast.overdue(requireContext())
            }
        }

        mainViewmodel.scheduleEditSuccess.observe(viewLifecycleOwner){
            if (it){
                val startCheckData = StartCheckAlarmData(
                    mainViewmodel.startX.value!!,
                    mainViewmodel.startY.value!!,
                    mainViewmodel.scheduleEmailPath.value!!)

                setAlarm(
                    startCheckData,
                    mainViewmodel.fnAlarmTimeSet(mainViewmodel.scheduleAlarmHH.value!!, mainViewmodel.scheduleAlarmMM.value!!),
                    mainViewmodel.fnAlarmTimeSet("0","0"),
                    mainViewmodel.selectScheduleNickname.value!!,
                    mainViewmodel.startCheckAlarmTime.value!!)

                val fragmentManager = requireActivity().supportFragmentManager
                fragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE)
            }
        }
    }
}