package com.example.appointment.ui.fragment.schedule

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import androidx.activity.addCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.activityViewModels
import com.example.appointment.R
import com.example.appointment.ui.adapter.ScheduleListAdapter
import com.example.appointment.ui.activity.schedule.ScheduleAlarmActivity
import com.example.appointment.databinding.FragmentScheduleBinding
import com.example.appointment.ui.adapter.OnItemLongClickListener
import com.example.appointment.ui.fragment.AdapterFragment
import com.example.appointment.viewmodel.MainViewModel


class Schedule_Fragment : AdapterFragment<FragmentScheduleBinding>(), ScheduleListAdapter.OnItemClickListener, OnItemLongClickListener{
    private val mainViewmodel: MainViewModel by activityViewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun layoutResId(): Int {
        return R.layout.fragment_schedule_
    }

    override fun initializeUI() {
        binding.viewmodel = mainViewmodel
        val toolbar: androidx.appcompat.widget.Toolbar = binding.toolbar
        (requireActivity() as AppCompatActivity).setSupportActionBar(toolbar)

        mainViewmodel.startingPointGet()

        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner) {
            if(binding.layoutRefuseCheck.visibility == View.VISIBLE){
                binding.layoutRefuseCheck.visibility = View.GONE
            }else if(binding.layoutLongClickAction.visibility == View.VISIBLE){
                binding.layoutLongClickAction.visibility = View.GONE
            }else{
                isEnabled = false
                requireActivity().onBackPressed()
            }
        }
    }

    override fun onResume() {
        mainViewmodel.updateScheduleList()
        super.onResume()
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_schedule, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean =when(item.itemId){
        R.id.menu_schedule_alarm->{
            mainViewmodel.selectScheduleAlarmItem()
            true
        }
        else-> super.onOptionsItemSelected(item)
    }

    override fun onItemClick(position: Int, status: String) {
        mainViewmodel.scheduleClick(position, status)
    }

    override fun onItemLongClick(position: Int) {
        mainViewmodel.scheduleLongClick(position)
    }

    override fun setObserve(){
        mainViewmodel.startScheduleEditFragment.observe(viewLifecycleOwner){
            if(it){
               getTransaction().replace(R.id.fragment_Schedule_edit, ScheduleEdit_Fragment()).addToBackStack(null).commit()
            }
        }

        mainViewmodel.viewRefuseView.observe(this){
            if(it){
                binding.layoutRefuseCheck.visibility = View.VISIBLE
            }
        }

        mainViewmodel.viewScheduleDelteView.observe(viewLifecycleOwner){
            if(it){
                binding.layoutLongClickAction.visibility = View.VISIBLE
            }
        }

        mainViewmodel.startScheduleAlarmActivity.observe(viewLifecycleOwner){
            if(it){
                val intent: Intent=Intent(requireActivity(), ScheduleAlarmActivity::class.java)
                intent.putExtra("address",mainViewmodel.profileAddress.value)
                startActivity(intent)
            }
        }

        mainViewmodel.scheduleRefuseOkView.observe(viewLifecycleOwner){
            if(it){
                binding.layoutRefuseCheck.visibility = View.GONE
            }
        }

        mainViewmodel.scheduleListDelete.observe(viewLifecycleOwner){
            if(it){
                binding.layoutLongClickAction.visibility = View.GONE
            }
        }

        mainViewmodel.scheduleDataList.observe(viewLifecycleOwner){
            setAdapter(binding.recycleSchedule, ScheduleListAdapter(this,this),it,true)
        }

        mainViewmodel.scheduleAlarmDataList.observe(viewLifecycleOwner){
            if(mainViewmodel.scheduleAlarmDataList.value!!.size > 0){
                binding.scheduleAlarmIcOn.visibility = View.VISIBLE
            }else{
                binding.scheduleAlarmIcOn.visibility = View.GONE
            }
        }
    }
}