package com.example.appointment.ui.fragment.schedule

import android.content.Intent
import android.widget.Toast
import androidx.activity.addCallback
import androidx.fragment.app.activityViewModels
import com.example.appointment.R
import com.example.appointment.data.RequestCode.Companion.MAP_REQUEST_CODE
import com.example.appointment.data.ToastMessage
import com.example.appointment.ui.activity.schedule.ScheduleMapActivity
import com.example.appointment.databinding.FragmentScheduleSetBinding
import com.example.appointment.model.SelectTransport
import com.example.appointment.model.StartCheckAlarmData
import com.example.appointment.viewmodel.MainViewModel


class ScheduleSet_Fragment : BaseSceduleSet_Fragment<FragmentScheduleSetBinding>() {
    private val mainViewmodel: MainViewModel by activityViewModels()

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
            mainViewmodel.scheduleSetDataReset()
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
        mainViewmodel.handleActivityResult(requestCode, resultCode, data)
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
                val startCheckData = StartCheckAlarmData(
                    mainViewmodel.startX.value!!,
                    mainViewmodel.startY.value!!,
                    mainViewmodel.scheduleEmailPath.value!!)
                setStartAlarm(startCheckData,mainViewmodel.scheduleStartAlarmTime)//출발인듯//고쳐두긴 했는데 이상하면 다른2개랑 같이 바꿔야할듯
            }
        }

        mainViewmodel.appointmentRequestSuccess.observe(viewLifecycleOwner){
            if(it){
                fragmentClose()
                setAlarm(mainViewmodel.selectFriendProfile.value!!.nickname,mainViewmodel.scheduleAlarmTime)
                Toast.makeText(activity,"${mainViewmodel.selectFriendProfile.value!!.nickname}님에게 약속 요청을 보냈습니다.",Toast.LENGTH_SHORT).show()
            }
        }
    }
}