package com.example.appointment.UI.Activity.Schedule

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import androidx.activity.viewModels
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.appointment.R
import com.example.appointment.UI.Adapter.ScheduleAlarmListAdapter
import com.example.appointment.databinding.ActivityScheduleAlarmBinding
import com.example.appointment.ViewModel.MainViewmodel
import com.example.appointment.UI.Fragment.schedule.ScheduleAccept_Fragment

class ScheduleAlarmActivity : AppCompatActivity(), ScheduleAlarmListAdapter.OnItemClickListener {
    private lateinit var binding : ActivityScheduleAlarmBinding
    private lateinit var adapter : ScheduleAlarmListAdapter
    private val mainViewmodel: MainViewmodel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this,R.layout.activity_schedule_alarm)
        binding.viewmodel = mainViewmodel
        binding.lifecycleOwner = this

        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)


        mainViewmodel.fnScheduleListData()

        adapter = ScheduleAlarmListAdapter(mainViewmodel.scheduleAlarmDataList.value!!,this)
        binding.recycleScheduleAlarm.adapter=adapter

        setObserve()
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

    private fun setObserve(){
        mainViewmodel.scheduleAlarmDataList.observe(this){
            binding.recycleScheduleAlarm.layoutManager = LinearLayoutManager(this)
            adapter = ScheduleAlarmListAdapter(it,this)
            binding.recycleScheduleAlarm.adapter=adapter
            binding.recycleScheduleAlarm.addItemDecoration(
                DividerItemDecoration(this, LinearLayoutManager.VERTICAL)
            )
        }

    }
}