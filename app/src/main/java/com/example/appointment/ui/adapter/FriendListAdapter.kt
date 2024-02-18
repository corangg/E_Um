package com.example.appointment.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.example.appointment.databinding.ItemFriendListBinding
import com.example.appointment.data.ProfileDataModel

class FriendListViewHolder(binding: ItemFriendListBinding): BaseViewHolder<ItemFriendListBinding>(binding){
    fun bindFfiendListItem(
        friendProfile : ProfileDataModel,
        position: Int,
        onItemClickListener: OnItemClickListener
    ){
        binding.profiledata = friendProfile

        if(friendProfile.imgURL != ""){
            Glide.with(binding.root).load(friendProfile.imgURL).into(binding.imgProfile)
        }
        binding.profile.setOnClickListener {
            onItemClickListener.onItemClick(position)
        }
    }
}

class FriendListAdapter(
    private val onItemClickListener: OnItemClickListener)
    : BaseAdapter<ProfileDataModel,FriendListViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FriendListViewHolder{
        val binding = ItemFriendListBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return FriendListViewHolder(binding)
    }

    override fun onBindViewHolder(
        holder: FriendListViewHolder,
        item: ProfileDataModel,
        position: Int
    ) {
        val friendData = itemList[position]

        holder.bindFfiendListItem(friendData,position,onItemClickListener)
    }
}