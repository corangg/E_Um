package com.example.appointment.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.appointment.databinding.ItemScheduleAlarmBinding
import com.example.appointment.model.ScheduleSet

class ScheduleAlarmListViewHolder(val binding: ItemScheduleAlarmBinding): RecyclerView.ViewHolder(binding.root)

class ScheduleAlarmListAdapter(val scheduleDataList : MutableList<ScheduleSet>,val onItemClickListener: OnItemClickListener):RecyclerView.Adapter<RecyclerView.ViewHolder>(){

    interface OnItemClickListener {
        fun onItemClick(position: Int)
    }

    override fun getItemCount(): Int {
        return scheduleDataList.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return ScheduleAlarmListViewHolder(ItemScheduleAlarmBinding.inflate(LayoutInflater.from(parent.context),parent,false))
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val binding:ItemScheduleAlarmBinding = (holder as ScheduleAlarmListViewHolder).binding

        val YYYY = scheduleDataList[position].time.substring(0,4)
        val MM = scheduleDataList[position].time.substring(4,6)
        val DD = scheduleDataList[position].time.substring(6,8)
        val hh = scheduleDataList[position].time.substring(8,10)
        val mm = scheduleDataList[position].time.substring(10,12)
        val date = YYYY+"년"+MM+"월"+DD+"일"+hh+"시"+ mm +"분"

        binding.textAlarmText.text = scheduleDataList[position].nickname + "님이 약속을 잡고 싶어합니다."
        binding.textScheduleTime.text = date

        binding.itemScheduleAlarm.setOnClickListener {
            onItemClickListener.onItemClick(position)
        }
    }
}



