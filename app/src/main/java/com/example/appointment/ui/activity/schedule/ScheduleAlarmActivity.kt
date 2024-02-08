package com.example.appointment.ui.activity.schedule

import android.view.Menu
import androidx.activity.viewModels
import com.example.appointment.R
import com.example.appointment.ui.adapter.ScheduleAlarmListAdapter
import com.example.appointment.databinding.ActivityScheduleAlarmBinding
import com.example.appointment.ui.activity.AdapterActivity
import com.example.appointment.ui.adapter.OnItemClickListener
import com.example.appointment.ui.fragment.schedule.ScheduleAccept_Fragment
import com.example.appointment.viewmodel.MainViewModel


class ScheduleAlarmActivity : AdapterActivity<ActivityScheduleAlarmBinding>(), OnItemClickListener {
    private val mainViewmodel: MainViewModel by viewModels()

    override fun layoutResId(): Int {
        return R.layout.activity_schedule_alarm
    }

    override fun initializeUI() {
        binding.viewmodel = mainViewmodel

        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        mainViewmodel.fnScheduleListData()
    }

    override fun onItemClick(position: Int) {

        mainViewmodel.fnScheduleAcceptClick(position,intent)
    }

    override fun onPrepareOptionsMenu(menu: Menu?): Boolean {
        return super.onPrepareOptionsMenu(menu)
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return super.onSupportNavigateUp()
    }

    override fun setObserve(){
        mainViewmodel.scheduleAlarmDataList.observe(this){
            setAdapter(binding.recycleScheduleAlarm,ScheduleAlarmListAdapter(this),it,true)
        }

        mainViewmodel.startScheduleAcceptFragment.observe(this){
            if (it){
                supportFragmentManager.beginTransaction().replace(R.id.fragment_Schedule_Accept,
                    ScheduleAccept_Fragment()
                ).addToBackStack(null).commit()
            }
        }
    }
}