package com.example.appointment.UI.Adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.appointment.databinding.ItemAlarmBinding
import com.example.appointment.model.FriendRequestAlarmData


class FriendRequestAlarmViewHolder(val binding: ItemAlarmBinding): RecyclerView.ViewHolder(binding.root)

class FriendRequestAlarmAdapter(val alarmList: MutableList<FriendRequestAlarmData>, val onItemClickListener: OnItemClickListener):RecyclerView.Adapter<RecyclerView.ViewHolder>(){
    interface OnItemClickListener {
        fun onItemClickAccept(email: String,nickName:String)
        fun onItemClickRefuse(email: String,nickName:String)
    }

    override fun getItemCount(): Int {
        return alarmList?.size?:0
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder
            = FriendRequestAlarmViewHolder(ItemAlarmBinding.inflate(LayoutInflater.from(parent.context),parent,false))

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val binding = (holder as FriendRequestAlarmViewHolder).binding

        binding.textRequest.text = alarmList!![position].nickname + "님에게 친구 요청이 들어왔습니다."

        binding.btnAccept.setOnClickListener {
            onItemClickListener.onItemClickAccept(alarmList[position].email, alarmList[position].nickname)
        }
        binding.btnRefuse.setOnClickListener {
            onItemClickListener.onItemClickRefuse(alarmList[position].email, alarmList[position].nickname)
        }
    }
}
