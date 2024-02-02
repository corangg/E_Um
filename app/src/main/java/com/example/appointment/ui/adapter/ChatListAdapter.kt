package com.example.appointment.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.example.appointment.databinding.ItemChatListBinding
import com.example.appointment.model.ChatRoomData

class ChatListViewHolder(binding: ItemChatListBinding):BaseViewHolder<ItemChatListBinding>(binding){

    fun bindChatListItem(
        chatRoomData: ChatRoomData,
        chatRoomProfile: String?,
        position: Int,
        onItemClickListener: OnItemClickListener){
        binding.chatroom = chatRoomData

        if(chatRoomData.notCheckChat == 0){
            binding.notCheckMessageCount.visibility = View.GONE
        }

        if(chatRoomProfile != ""){
            Glide.with(binding.root).load(chatRoomProfile).into(binding.imgProfile)
        }

        binding.profile.setOnClickListener {
            onItemClickListener.onItemClick(position)
        }
    }
}
class ChatListAdapter(
    private val chatRoomData:MutableList<ChatRoomData>?,
    private val chatRoomProfile:MutableList<String?>?,
    private val onItemClickListener: OnItemClickListener)
    : BaseAdapter<ChatRoomData,ChatListViewHolder>(){
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChatListViewHolder {
        val binding = ItemChatListBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return ChatListViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ChatListViewHolder, item: ChatRoomData, position: Int) {
        val chatRoomData = itemList[position]
        val chatRoomProfile = chatRoomProfile?.get(position)

        holder.bindChatListItem(chatRoomData, chatRoomProfile,position, onItemClickListener)
    }
}

