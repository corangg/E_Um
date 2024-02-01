package com.example.appointment.UI.Adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.appointment.databinding.ItemMessageGetBinding
import com.example.appointment.databinding.ItemMessageGetProfileBinding
import com.example.appointment.databinding.ItemMessageSendBinding
import com.example.appointment.model.ChatDataModel

class ChatSendViewHolder(val binding: ItemMessageSendBinding): RecyclerView.ViewHolder(binding.root)
class ChatGetFirstViewHolder(val binding: ItemMessageGetProfileBinding): RecyclerView.ViewHolder(binding.root)
class ChatGetViewHolder(val binding: ItemMessageGetBinding): RecyclerView.ViewHolder(binding.root)

class ChatAdapter(val chatMessage:MutableList<ChatDataModel>?, val email:String?, val profileURL:String?, val friendNickname:String?):RecyclerView.Adapter<RecyclerView.ViewHolder>(){
    private val TYPE_SEND = 1
    private val TYPE_GETFIRST = 2
    private val TYPE_GET = 3

    override fun getItemViewType(position: Int): Int {
        return if(chatMessage!![position].email==email){
            TYPE_SEND
        }else if(position == 0||chatMessage!![position].email!=chatMessage!![position-1].email){//position-1이 없음
            TYPE_GETFIRST
        }else{
            TYPE_GET
        }
    }

    override fun getItemCount(): Int {
        return chatMessage?.size?:0
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType){
            TYPE_SEND->{
                ChatSendViewHolder(ItemMessageSendBinding.inflate(LayoutInflater.from(parent.context),parent,false))
            }
            TYPE_GETFIRST->{
                ChatGetFirstViewHolder(ItemMessageGetProfileBinding.inflate(LayoutInflater.from(parent.context),parent,false))
            }
            TYPE_GET->{
                ChatGetViewHolder(ItemMessageGetBinding.inflate(LayoutInflater.from(parent.context),parent,false))
            }
            else->throw IllegalArgumentException("Invalid view type")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder.itemViewType){
            TYPE_SEND->{
                val binding:ItemMessageSendBinding=(holder as ChatSendViewHolder).binding
                binding.textSendMessage.text = chatMessage!![position].message
            }
            TYPE_GETFIRST->{
                val binding:ItemMessageGetProfileBinding=(holder as ChatGetFirstViewHolder).binding
                binding.textMessage.text = chatMessage!![position].message
                binding.textNickname.text = friendNickname
                if(profileURL != ""){
                    Glide.with(holder.itemView).load(profileURL).into(binding.imgProfile)
                }
            }
            TYPE_GET->{
                val binding:ItemMessageGetBinding=(holder as ChatGetViewHolder).binding
                binding.textSendMessage.text = chatMessage!![position].message
            }
        }
    }
}