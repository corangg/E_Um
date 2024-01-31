package com.example.appointment.main.fragment.schedule

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import com.example.appointment.R
import com.example.appointment.databinding.FragmentSchduleCalendarBinding
import com.example.appointment.main.MainViewmodel

class ScheduleCalendar_Fragment : Fragment() {

    val mainViewmodel: MainViewmodel by activityViewModels()
    lateinit var binding: FragmentSchduleCalendarBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSchduleCalendarBinding.inflate(inflater,container,false)
        binding.viewmodel = mainViewmodel
        binding.lifecycleOwner = this
        binding.calendar.setOnClickListener {

        }
        binding.viewCalendar.setOnDateChangedListener { widget, date, selected ->
            mainViewmodel.fnScheduleDateSet(date.year,date.month,date.day)
            mainViewmodel.scheduleSetFragmentStart.value = true
        }
        setObserve()

        return binding.root
    }

    private fun setObserve(){
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