package com.example.appointment.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.example.appointment.databinding.ItemScheduleBinding
import com.example.appointment.model.ScheduleSet

class ScheduleListViewHolder(binding: ItemScheduleBinding): BaseViewHolder<ItemScheduleBinding>(binding){
    fun bindScheduleItem(
        scheduleDataList : ScheduleSet,
        position: Int,
        onItemClickListener: ScheduleListAdapter.OnItemClickListener,
        onItemLongClickListener: OnItemLongClickListener){
        binding.scheduleset = scheduleDataList

        val YYYY = scheduleDataList.time.substring(0,4)
        val MM = scheduleDataList.time.substring(4,6)
        val DD = scheduleDataList.time.substring(6,8)
        val hh = scheduleDataList.time.substring(8,10)
        val mm = scheduleDataList.time.substring(10,12)
        val date = YYYY+"년"+MM+"월"+DD+"일"+hh+"시"+ mm +"분"

        binding.textMeetingtime.text = date

        if(scheduleDataList.status == "wait"){
            binding.btnScheduleStatus.visibility = View.VISIBLE
            binding.btnScheduleStatus.text = "대기중"
        }else if(scheduleDataList.status == "refuse"){
            binding.btnScheduleStatus.visibility = View.VISIBLE
            binding.btnScheduleStatus.text = "거절됨"
        }

        if(scheduleDataList.profileImgURL != ""){
            Glide.with(binding.root).load(scheduleDataList.profileImgURL).into(binding.imgProfile)
        }
        binding.itemSchedule.setOnClickListener {
            onItemClickListener.onItemClick(position,scheduleDataList.status)
        }

        binding.itemSchedule.setOnLongClickListener {
            onItemLongClickListener.onItemLongClick(position)
            true
        }
    }

}
class ScheduleListAdapter(
    private val onItemClickListener: OnItemClickListener,
    private val onItemLongClickListener: OnItemLongClickListener)
    : BaseAdapter<ScheduleSet,ScheduleListViewHolder>(){

    interface OnItemClickListener {
        fun onItemClick(position: Int,status: String)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ScheduleListViewHolder{
        val binding = ItemScheduleBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return ScheduleListViewHolder(binding)
    }

    override fun onBindViewHolder(
        holder: ScheduleListViewHolder,
        item: ScheduleSet,
        position: Int
    ) {
        val scheduleListData = itemList[position]

        holder.bindScheduleItem(scheduleListData, position, onItemClickListener, onItemLongClickListener)
    }

}