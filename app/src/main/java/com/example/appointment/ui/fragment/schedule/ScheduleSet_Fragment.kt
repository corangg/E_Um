package com.example.appointment.ui.fragment.schedule

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
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.activityViewModels
import com.example.appointment.AlarmUtil
import com.example.appointment.R
import com.example.appointment.data.RequestCode.Companion.MAP_REQUEST_CODE
import com.example.appointment.data.ToastMessage
import com.example.appointment.ui.activity.schedule.ScheduleMapActivity
import com.example.appointment.databinding.FragmentScheduleSetBinding
import com.example.appointment.model.SelectTransport
import com.example.appointment.viewmodel.MainViewmodel
import com.example.appointment.model.StartCheckAlarmData
import com.example.appointment.ui.fragment.BaseFragment
import com.example.appointment.viewmodel.profile.ProfileViewModel


class ScheduleSet_Fragment : BaseSceduleSet_Fragment<FragmentScheduleSetBinding>() {
    private val mainViewmodel: ProfileViewModel by activityViewModels()
    val toast = ToastMessage()
    override fun layoutResId(): Int {
        return R.layout.fragment_schedule_set_
    }

    override fun initializeUI() {
        binding.viewmodel = mainViewmodel
        binding.scheduleSet.setOnClickListener {}

        setMaxNumber(binding.editHH,12)
        setMaxNumber(binding.editMM,59)
        setMaxNumber(binding.editAlarmHH,23)
        setMaxNumber(binding.editAlarmMM,59)

        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner) {
            mainViewmodel.fnScheduleSetDataReset()
            if (!isHandlingBackPressed()) {
                isEnabled = false
                requireActivity().onBackPressed()
            }
        }
    }

    private fun isHandlingBackPressed(): Boolean {
        return false
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        mainViewmodel.fnHandleActivityResult(requestCode, resultCode, data)
    }

    override fun setObserve(){
        mainViewmodel.scheduleAmPmSet.observe(viewLifecycleOwner){
            AmPmSet(binding.btnAm,binding.btnPm,it)
        }

        mainViewmodel.startScheduleMapActivity.observe(viewLifecycleOwner){
            if(it){
                val intent = Intent(requireContext(), ScheduleMapActivity::class.java)
                startActivityForResult(intent, MAP_REQUEST_CODE)
            }
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

        mainViewmodel.appointmentRequestSuccess.observe(viewLifecycleOwner){
            if(it){
                fragmentClose()

                val startCheckData = StartCheckAlarmData(
                    mainViewmodel.startX.value!!,
                    mainViewmodel.startY.value!!,
                    mainViewmodel.scheduleEmailPath.value!!
                )
                setAlarm(
                    startCheckData,
                    mainViewmodel.fnAlarmTimeSet(mainViewmodel.scheduleAlarmHH.value!!, mainViewmodel.scheduleAlarmMM.value!!),
                    mainViewmodel.fnAlarmTimeSet("0","0"),
                    mainViewmodel.selectFriendProfile.value!!.nickname,
                    mainViewmodel.startCheckAlarmTime.value!!
                )
                Toast.makeText(activity,"${mainViewmodel.selectFriendProfile.value!!.nickname}님에게 약속 요청을 보냈습니다.",Toast.LENGTH_SHORT).show()
            }
        }
    }
}