package com.example.appointment.ui.fragment.schedule

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.appointment.R
import com.example.appointment.ui.adapter.ScheduleListAdapter
import com.example.appointment.ui.activity.schedule.ScheduleAlarmActivity
import com.example.appointment.databinding.FragmentScheduleBinding
import com.example.appointment.ui.adapter.OnItemLongClickListener
import com.example.appointment.ui.fragment.BaseFragment
import com.example.appointment.viewmodel.MainViewModel


class Schedule_Fragment : BaseFragment<FragmentScheduleBinding>(), ScheduleListAdapter.OnItemClickListener, OnItemLongClickListener{
    private val mainViewmodel: MainViewModel by activityViewModels()
    private lateinit var adapter: ScheduleListAdapter

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

        mainViewmodel.fnStartingPointSet()
    }

    override fun onResume() {
        mainViewmodel.fnScheduleListData()
        super.onResume()
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_schedule, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean =when(item.itemId){
        R.id.menu_schedule_alarm->{
            mainViewmodel.fnSelectScheduleAlarmItem()
            true
        }
        else-> super.onOptionsItemSelected(item)
    }

    override fun onItemClick(position: Int, status: String) {
        mainViewmodel.fnScheduleClick(position, status)
    }

    override fun onItemLongClick(position: Int) {
        mainViewmodel.fnScheduleLongClick(position)
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
            binding.recycleSchedule.layoutManager = LinearLayoutManager(context)
            adapter = ScheduleListAdapter(it,this,this)
            binding.recycleSchedule.adapter=adapter
            adapter.setList(it)
            binding.recycleSchedule.addItemDecoration(
                DividerItemDecoration(context, LinearLayoutManager.VERTICAL)
            )
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