package com.example.appointment.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.appointment.databinding.ItemChatListBinding
import com.example.appointment.model.ChatRoomData

class ChatListViewHolder(val binding: ItemChatListBinding): RecyclerView.ViewHolder(binding.root)

class ChatListAdapter(val chatRoomData:MutableList<ChatRoomData>?, val chatRoomProfile:MutableList<String?>?, val onItemClickListener: OnItemClickListener):
    RecyclerView.Adapter<RecyclerView.ViewHolder>(){
    interface OnItemClickListener {
        fun onItemClick(position: Int)
    }

    override fun getItemCount(): Int {
        return chatRoomData?.size?:0
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return ChatListViewHolder(ItemChatListBinding.inflate(LayoutInflater.from(parent.context),parent,false))
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val binding: ItemChatListBinding =(holder as ChatListViewHolder).binding
        binding.textNickname.text = chatRoomData!![position].nickname
        binding.textLastMessage.text = chatRoomData!![position].lastMessage
        binding.textNotCheckMessageCount.text = chatRoomData!![position].notCheckChat.toString()

        if(chatRoomData!![position].notCheckChat == 0){
            binding.notCheckMessageCount.visibility = View.GONE
        }

        if(chatRoomProfile!![position] != ""){
            Glide.with(holder.itemView).load(chatRoomProfile!![position]).into(binding.imgProfile)
        }

        binding.profile.setOnClickListener {
            onItemClickListener.onItemClick(position)
        }
    }
}