package com.example.appointment.ui.fragment.schedule

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat.startActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.appointment.R
import com.example.appointment.ui.adapter.ScheduleListAdapter
import com.example.appointment.ui.activity.schedule.ScheduleAlarmActivity
import com.example.appointment.databinding.FragmentScheduleBinding
import com.example.appointment.viewmodel.MainViewmodel
import com.example.appointment.model.ScheduleSet
import com.example.appointment.ui.adapter.OnItemLongClickListener


class Schedule_Fragment : Fragment(), ScheduleListAdapter.OnItemClickListener, OnItemLongClickListener{
    private val mainViewmodel: MainViewmodel by activityViewModels()
    private lateinit var binding: FragmentScheduleBinding
    private lateinit var adapter: ScheduleListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentScheduleBinding.inflate(inflater,container,false)
        binding.viewmodel = mainViewmodel
        binding.lifecycleOwner = this

        val toolbar: androidx.appcompat.widget.Toolbar = binding.toolbar
        (requireActivity() as AppCompatActivity).setSupportActionBar(toolbar)

        mainViewmodel.fnStartingPointSet()

        setObserve()
        return binding.root
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
            val intent: Intent=Intent(requireActivity(), ScheduleAlarmActivity::class.java)
            intent.putExtra("address",mainViewmodel.profileAddress.value)
            startActivity(intent)
            true
        }
        else-> super.onOptionsItemSelected(item)
    }

    override fun onItemClick(position: Int, status: String) {
        if(status == "refuse"){
            binding.layoutRefuseCheck.visibility = View.VISIBLE
            mainViewmodel.selectPosition.value = position

        }else if(status == "consent"||status == "wait"){
            val fragmentManager = requireActivity().supportFragmentManager
            val transaction = fragmentManager.beginTransaction()

            mainViewmodel.fnSchedulEditDataSet(position)
            transaction.replace(R.id.fragment_Schedule_edit, ScheduleEdit_Fragment()).addToBackStack(null).commit()
        }

    }

    override fun onItemLongClick(position: Int) {
        binding.layoutLongClickAction.visibility = View.VISIBLE
        mainViewmodel.fnDeleteTextSet(position)
        mainViewmodel.selectPosition.value = position
    }

    private fun setObserve(){
        mainViewmodel.scheduleDataList.observe(viewLifecycleOwner){
            binding.recycleSchedule.layoutManager = LinearLayoutManager(context)
            adapter = ScheduleListAdapter(it,this,this)
            binding.recycleSchedule.adapter=adapter

            binding.recycleSchedule.addItemDecoration(
                DividerItemDecoration(context, LinearLayoutManager.VERTICAL)
            )
            adapter.setList(it)
        }

        mainViewmodel.scheduleAlarmDataList.observe(viewLifecycleOwner){
                if(mainViewmodel.scheduleAlarmDataList.value!!.size > 0){
                    binding.scheduleAlarmIcOn.visibility = View.VISIBLE
                }else{
                    binding.scheduleAlarmIcOn.visibility = View.GONE
                }
        }

        mainViewmodel.scheduleListDelete.observe(viewLifecycleOwner){
            if(it){
                binding.layoutLongClickAction.visibility = View.GONE
            }else{
                binding.layoutLongClickAction.visibility = View.GONE
            }
        }

        mainViewmodel.scheduleRefuseOkView.observe(viewLifecycleOwner){
            if(it){
                binding.layoutRefuseCheck.visibility = View.GONE
            }
        }
    }

}