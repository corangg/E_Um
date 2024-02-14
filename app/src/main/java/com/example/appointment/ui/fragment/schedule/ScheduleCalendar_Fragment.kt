package com.example.appointment.ui.fragment.schedule

import androidx.fragment.app.activityViewModels
import com.example.appointment.R
import com.example.appointment.databinding.FragmentSchduleCalendarBinding
import com.example.appointment.ui.fragment.BaseFragment
import com.example.appointment.viewmodel.MainViewModel

class ScheduleCalendar_Fragment : BaseFragment<FragmentSchduleCalendarBinding>() {

    val mainViewmodel: MainViewModel by activityViewModels()

    override fun layoutResId(): Int {
        return R.layout.fragment_schdule_calendar_
    }

    override fun initializeUI() {
        binding.viewmodel = mainViewmodel
        binding.calendar.setOnClickListener {

        }
        mainViewmodel.scheduleSetDataReset()
        binding.viewCalendar.setOnDateChangedListener { widget, date, selected ->
            mainViewmodel.fnScheduleDateSet(date)
        }
    }
    override fun setObserve(){
        mainViewmodel.startScheduleSetFragment.observe(viewLifecycleOwner){
            if(it){
                getTransaction().replace(R.id.fragment_Schedule_set, ScheduleSet_Fragment()).addToBackStack(null).commit()
            }
        }
    }
}