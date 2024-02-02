package com.example.appointment.ui.activity.schedule

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import androidx.activity.viewModels
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.appointment.R
import com.example.appointment.ui.adapter.ScheduleAlarmListAdapter
import com.example.appointment.databinding.ActivityScheduleAlarmBinding
import com.example.appointment.ui.activity.BaseActivity
import com.example.appointment.ui.adapter.OnItemClickListener
import com.example.appointment.viewmodel.MainViewmodel
import com.example.appointment.ui.fragment.schedule.ScheduleAccept_Fragment

class ScheduleAlarmActivity : BaseActivity<ActivityScheduleAlarmBinding>(), OnItemClickListener {
    private lateinit var adapter : ScheduleAlarmListAdapter
    private val mainViewmodel: MainViewmodel by viewModels()

    override fun layoutResId(): Int {
        return R.layout.activity_schedule_alarm
    }

    override fun initializeUI() {
        binding.viewmodel = mainViewmodel

        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        mainViewmodel.fnScheduleListData()

        adapter = ScheduleAlarmListAdapter(mainViewmodel.scheduleAlarmDataList.value!!,this)
        binding.recycleScheduleAlarm.adapter=adapter
    }

    override fun onItemClick(position: Int) {
        supportFragmentManager.beginTransaction().replace(R.id.fragment_Schedule_Accept,
            ScheduleAccept_Fragment()
        ).addToBackStack(null).commit()
        mainViewmodel.fnScheduleAcceptDataSet(position, intent.getStringExtra("address"))
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
            binding.recycleScheduleAlarm.layoutManager = LinearLayoutManager(this)
            adapter = ScheduleAlarmListAdapter(it,this)
            binding.recycleScheduleAlarm.adapter=adapter
            adapter.setList(it)
            binding.recycleScheduleAlarm.addItemDecoration(
                DividerItemDecoration(this, LinearLayoutManager.VERTICAL)
            )
        }

    }
}