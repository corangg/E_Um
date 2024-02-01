package com.example.appointment.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.appointment.databinding.ItemScheduleBinding
import com.example.appointment.model.ScheduleSet

class ScheduleListViewHolder(val binding: ItemScheduleBinding): RecyclerView.ViewHolder(binding.root)
class ScheduleListAdapter(val scheduleDataList : MutableList<ScheduleSet>, val onItemClickListener: OnItemClickListener, val onItemLongClickListener: OnItemLongClickListener):
    RecyclerView.Adapter<RecyclerView.ViewHolder>(){

    interface OnItemClickListener {
        fun onItemClick(position: Int,status: String)
    }
    interface OnItemLongClickListener {
        fun onItemLongClick(position: Int)
    }

    override fun getItemCount(): Int {
        return scheduleDataList.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return ScheduleListViewHolder(ItemScheduleBinding.inflate(LayoutInflater.from(parent.context),parent,false))
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val binding: ItemScheduleBinding = (holder as ScheduleListViewHolder).binding

        val YYYY = scheduleDataList[position].time.substring(0,4)
        val MM = scheduleDataList[position].time.substring(4,6)
        val DD = scheduleDataList[position].time.substring(6,8)
        val hh = scheduleDataList[position].time.substring(8,10)
        val mm = scheduleDataList[position].time.substring(10,12)
        val date = YYYY+"년"+MM+"월"+DD+"일"+hh+"시"+ mm +"분"

        binding.textNickname.text = scheduleDataList[position].nickname
        binding.textAddress.text = scheduleDataList[position].meetingPlaceKeyword + ", " + scheduleDataList[position].meetingPlaceAddress
        binding.textMeetingtime.text = date
        if(scheduleDataList[position].status == "wait"){
            binding.btnScheduleStatus.visibility = View.VISIBLE
            binding.btnScheduleStatus.text = "대기중"
        }else if(scheduleDataList[position].status == "refuse"){
            binding.btnScheduleStatus.visibility = View.VISIBLE
            binding.btnScheduleStatus.text = "거절됨"
        }

        if(scheduleDataList[position].profileImgURL != ""){
            Glide.with(holder.itemView).load(scheduleDataList[position].profileImgURL).into(binding.imgProfile)
        }
        binding.itemSchedule.setOnClickListener {
            onItemClickListener.onItemClick(position,scheduleDataList[position].status)
        }

        binding.itemSchedule.setOnLongClickListener {
            onItemLongClickListener.onItemLongClick(position)
            true
        }
    }
}