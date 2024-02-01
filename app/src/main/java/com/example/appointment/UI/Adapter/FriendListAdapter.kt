package com.example.appointment.UI.Adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.appointment.databinding.ItemFriendListBinding
import com.example.appointment.model.ProfileDataModel

class FriendListViewHolder(val binding: ItemFriendListBinding): RecyclerView.ViewHolder(binding.root)

class FriendListAdapter(val friendProfile:MutableList<ProfileDataModel>?, val onItemClickListener: OnItemClickListener):RecyclerView.Adapter<RecyclerView.ViewHolder>(){
    interface OnItemClickListener {
        fun onItemClick(position: Int)
    }
    override fun getItemCount(): Int  = friendProfile?.size?:0

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder
            = FriendListViewHolder(ItemFriendListBinding.inflate(LayoutInflater.from(parent.context),parent,false))

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val binding = (holder as FriendListViewHolder).binding

        binding.textNickname.text = friendProfile!![position].nickname
        binding.textStatusmessage.text = friendProfile!![position].statusmessage

        if(friendProfile!![position].imgURL != ""){
            Glide.with(holder.itemView).load(friendProfile!![position].imgURL).into(binding.imgProfile)
        }

        binding.profile.setOnClickListener {
            onItemClickListener.onItemClick(position)
        }
    }
}