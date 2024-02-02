package com.example.appointment.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.appointment.databinding.ItemScheduleAlarmBinding
import com.example.appointment.model.ScheduleSet

class ScheduleAlarmListViewHolder(binding: ItemScheduleAlarmBinding): BaseViewHolder<ItemScheduleAlarmBinding>(binding){
    fun bindScheduleAlarmList(
        scheduleDataList : ScheduleSet,
        position: Int,
        onItemClickListener: OnItemClickListener
    ){
        binding.scheduleset = scheduleDataList

        val YYYY = scheduleDataList.time.substring(0,4)
        val MM = scheduleDataList.time.substring(4,6)
        val DD = scheduleDataList.time.substring(6,8)
        val hh = scheduleDataList.time.substring(8,10)
        val mm = scheduleDataList.time.substring(10,12)
        val date = YYYY+"년"+MM+"월"+DD+"일"+hh+"시"+ mm +"분"

        binding.textScheduleTime.text = date

        binding.itemScheduleAlarm.setOnClickListener {
            onItemClickListener.onItemClick(position)
        }

    }
}

class ScheduleAlarmListAdapter(
    private val scheduleDataList : MutableList<ScheduleSet>,
    private val onItemClickListener: OnItemClickListener)
    : BaseAdapter<ScheduleSet,ScheduleAlarmListViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ScheduleAlarmListViewHolder {
        val binding = ItemScheduleAlarmBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return ScheduleAlarmListViewHolder(binding)
    }

    override fun onBindViewHolder(
        holder: ScheduleAlarmListViewHolder,
        item: ScheduleSet,
        position: Int
    ) {
        val scheduleAlarmData = itemList[position]

        holder.bindScheduleAlarmList(scheduleAlarmData,position,onItemClickListener)
    }

}



