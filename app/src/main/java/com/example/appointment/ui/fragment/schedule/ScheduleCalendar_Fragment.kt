package com.example.appointment.ui.fragment.schedule

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import com.example.appointment.R
import com.example.appointment.databinding.FragmentSchduleCalendarBinding
import com.example.appointment.ui.fragment.BaseFragment
import com.example.appointment.viewmodel.MainViewmodel

class ScheduleCalendar_Fragment : BaseFragment<FragmentSchduleCalendarBinding>() {

    val mainViewmodel: MainViewmodel by activityViewModels()

    override fun layoutResId(): Int {
        return R.layout.fragment_schdule_calendar_
    }

    override fun initializeUI() {
        binding.viewmodel = mainViewmodel
        binding.calendar.setOnClickListener {

        }
        binding.viewCalendar.setOnDateChangedListener { widget, date, selected ->
            mainViewmodel.fnScheduleDateSet(date.year,date.month,date.day)
            mainViewmodel.scheduleSetFragmentStart.value = true
        }
    }
    override fun setObserve(){
        mainViewmodel.scheduleSetFragmentStart.observe(viewLifecycleOwner){
            if(it){
                mainViewmodel.scheduleSetFragmentStart.value = false

                val fragmentManager = requireActivity().supportFragmentManager
                val transaction = fragmentManager.beginTransaction()

                transaction.replace(R.id.fragment_Schedule_set, ScheduleSet_Fragment()).addToBackStack(null).commit()
            }
        }
    }
}