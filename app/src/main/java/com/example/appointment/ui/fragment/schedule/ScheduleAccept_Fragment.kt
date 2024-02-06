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
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.activityViewModels
import com.example.appointment.AlarmUtil
import com.example.appointment.R
import com.example.appointment.data.ToastMessage
import com.example.appointment.databinding.FragmentScheduleAcceptBinding
import com.example.appointment.model.SelectTransport
import com.example.appointment.viewmodel.MainViewmodel
import com.example.appointment.model.StartCheckAlarmData
import com.example.appointment.ui.fragment.BaseFragment
import com.example.appointment.viewmodel.profile.ProfileViewModel

class ScheduleAccept_Fragment : BaseSceduleSet_Fragment<FragmentScheduleAcceptBinding>() {
    private val mainViewmodel: ProfileViewModel by activityViewModels()

    val toast = ToastMessage()
    override fun layoutResId(): Int {
        return R.layout.fragment_schedule_accept_
    }

    override fun initializeUI() {
        binding.viewmodel = mainViewmodel
        binding.scheduleSet.setOnClickListener {}

        setMaxNumber(binding.editAlarmHH,23)
        setMaxNumber(binding.editAlarmMM,59)

        //mainViewmodel.fnStartingPointSet()//뭐 부족한듯
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

        mainViewmodel.scheduleAcceptSucess.observe(viewLifecycleOwner){
            if(it){
                val startCheckData = StartCheckAlarmData(
                    mainViewmodel.startX.value!!,
                    mainViewmodel.startY.value!!,
                    mainViewmodel.scheduleEmailPath.value!!
                )

                val fragmentManager = requireActivity().supportFragmentManager
                fragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE)

                setAlarm(
                    startCheckData,
                    mainViewmodel.fnAlarmTimeSet(mainViewmodel.scheduleAlarmHH.value!!, mainViewmodel.scheduleAlarmMM.value!!),
                    mainViewmodel.fnAlarmTimeSet("0","0"),
                    mainViewmodel.selectScheduleNickname.value!!,
                    mainViewmodel.startCheckAlarmTime.value!!)

                Toast.makeText(activity,"약속을 수락하셨습니다.",Toast.LENGTH_SHORT).show()
            }
        }
        mainViewmodel.meetingTimeOver.observe(viewLifecycleOwner){
            if(it){
                toast.overdue(requireContext())
            }
        }

        mainViewmodel.scheduleRefuse.observe(viewLifecycleOwner){
            if(it){
                val fragmentManager = requireActivity().supportFragmentManager
                fragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE)
                Toast.makeText(activity,"약속을 거절하셨습니다.",Toast.LENGTH_SHORT).show()
            }
        }
    }
}