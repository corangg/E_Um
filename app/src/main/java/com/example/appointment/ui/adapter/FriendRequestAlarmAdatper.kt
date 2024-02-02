package com.example.appointment.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.appointment.databinding.ItemFriendrequestalarmBinding
import com.example.appointment.model.FriendRequestAlarmData


class FriendRequestAlarmViewHolder(binding: ItemFriendrequestalarmBinding): BaseViewHolder<ItemFriendrequestalarmBinding>(binding){
    fun bindFriendRequestAlarmItem(
        alarmList: FriendRequestAlarmData,
        position: Int,
        onItemClickListener: FriendRequestAlarmAdapter.OnItemClickListener
    ){
        binding.textRequest.text = alarmList.nickname + "님에게 친구 요청이 들어왔습니다."

        binding.btnAccept.setOnClickListener {
            onItemClickListener.onItemClickAccept(alarmList.email, alarmList.nickname)
        }
        binding.btnRefuse.setOnClickListener {
            onItemClickListener.onItemClickRefuse(alarmList.email, alarmList.nickname)
        }
    }
}

class FriendRequestAlarmAdapter(
    private val alarmList: MutableList<FriendRequestAlarmData>,
    private val onItemClickListener: OnItemClickListener)
    : BaseAdapter<FriendRequestAlarmData,FriendRequestAlarmViewHolder>(){
    interface OnItemClickListener {
        fun onItemClickAccept(email: String,nickName:String)
        fun onItemClickRefuse(email: String,nickName:String)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FriendRequestAlarmViewHolder{
        val binding = ItemFriendrequestalarmBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return FriendRequestAlarmViewHolder(binding)
    }

    override fun onBindViewHolder(
        holder: FriendRequestAlarmViewHolder,
        item: FriendRequestAlarmData,
        position: Int
    ) {
        val friendRequestAlarmData = itemList[position]

        holder.bindFriendRequestAlarmItem(friendRequestAlarmData,position,onItemClickListener)
    }

}
