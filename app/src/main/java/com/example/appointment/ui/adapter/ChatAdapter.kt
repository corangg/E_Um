package com.example.appointment.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.example.appointment.data.Utils
import com.example.appointment.databinding.ItemMessageGetBinding
import com.example.appointment.databinding.ItemMessageGetProfileBinding
import com.example.appointment.databinding.ItemMessageSendBinding
import com.example.appointment.data.ChatCreateData
import com.example.appointment.data.ChatDataModel

class ChatSendViewHolder(binding: ItemMessageSendBinding): BaseViewHolder<ItemMessageSendBinding>(binding){
    fun sendMessage(chatDataModel: ChatDataModel){
        binding.textSendMessage.text = chatDataModel.message
    }
}
class ChatGetFirstViewHolder(binding: ItemMessageGetProfileBinding): BaseViewHolder<ItemMessageGetProfileBinding>(binding){
    fun getFirstMessage(chatDataModel: ChatDataModel, createData: ChatCreateData){
        binding.textMessage.text = chatDataModel.message
        binding.textNickname.text = createData.friendNickname
        if(createData.profileURL != ""){
            Glide.with(itemView).load(createData.profileURL).into(binding.imgProfile)
        }
    }
}
class ChatGetViewHolder(binding: ItemMessageGetBinding): BaseViewHolder<ItemMessageGetBinding>(binding){
    fun getMessage(chatDataModel: ChatDataModel){
        binding.textMessage.text = chatDataModel.message
    }
}

class ChatAdapter(
    private val chatMessage:MutableList<ChatDataModel>,
    private val chatCreateData: ChatCreateData
)
    : BaseAdapter<ChatDataModel,BaseViewHolder<*>>(){

    private val TYPE_SEND = 1
    private val TYPE_GETFIRST = 2
    private val TYPE_GET = 3

    override fun getItemViewType(position: Int): Int {
        return if(chatMessage[position].email== Utils.auth.currentUser!!.email){
            TYPE_SEND
        }else if(position == 0||chatMessage[position].email!=chatMessage[position-1].email){//position-1이 없음
            TYPE_GETFIRST
        }else{
            TYPE_GET
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder<*> {
        val inflater = LayoutInflater.from(parent.context)
        return when (viewType) {
            TYPE_SEND -> {
                val binding = ItemMessageSendBinding.inflate(inflater, parent, false)
                ChatSendViewHolder(binding)
            }
            TYPE_GETFIRST -> {
                val binding = ItemMessageGetProfileBinding.inflate(inflater, parent, false)
                ChatGetFirstViewHolder(binding)
            }
            TYPE_GET -> {
                val binding = ItemMessageGetBinding.inflate(inflater, parent, false)
                ChatGetViewHolder(binding)
            }
            else -> throw IllegalArgumentException("Invalid view type")
        }
    }

    override fun onBindViewHolder(holder: BaseViewHolder<*>, item: ChatDataModel, position: Int) {
        when (holder) {
            is ChatSendViewHolder -> {
                val sendData = itemList[position]
                holder.sendMessage(sendData)
            }
            is ChatGetFirstViewHolder -> {
                val sendData = itemList[position]
                holder.getFirstMessage(sendData,chatCreateData)
            }
            is ChatGetViewHolder -> {
                val sendData = itemList[position]
                holder.getMessage(sendData)
            }
        }
    }
}