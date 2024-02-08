package com.example.appointment.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.appointment.R
import com.example.appointment.databinding.ItemAddressBinding
import com.example.appointment.model.AddressData
import com.google.firebase.firestore.model.mutation.ArrayTransformOperation

class AddressListViewHolder(val binding: ItemAddressBinding): RecyclerView.ViewHolder(binding.root)

class AddressListAdapter(val addressList:MutableList<AddressData>, val onItemClickListener: OnItemClickListener):
    RecyclerView.Adapter<RecyclerView.ViewHolder>(){
    private var editBoolean = false

    interface OnItemClickListener {
        fun onItemClickDelete(position: Int)
        fun onItemClickTitle(address : String)
    }

    override fun getItemCount(): Int {
        return addressList?.size ?:0
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder
            = AddressListViewHolder(ItemAddressBinding.inflate(LayoutInflater.from(parent.context),parent,false))

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val binding=(holder as AddressListViewHolder).binding

        binding.addressTitle.text = addressList[position].addressTitledatas
        binding.address.text = addressList[position].addressdatas

        if(addressList[position].titleaddress){
            holder.binding.btnTitle.visibility = View.VISIBLE
            holder.binding.btnTitleAddress.setBackgroundResource(R.drawable.btn_round_gray)
            holder.binding.btnDelAddress.setBackgroundResource(R.drawable.btn_round_gray)
        }else{
            holder.binding.btnTitle.visibility = View.INVISIBLE
            holder.binding.btnTitleAddress.setBackgroundResource(R.drawable.btn_round)
            holder.binding.btnDelAddress.setBackgroundResource(R.drawable.btn_round)
        }

        binding.btnDelAddress.setOnClickListener {
            if(!addressList[position].titleaddress){
                addressList.removeAt(position)
                notifyItemRemoved(position)
                onItemClickListener.onItemClickDelete(position)
            }
        }

        binding.btnTitleAddress.setOnClickListener {

            if(!addressList[position].titleaddress){
                holder.binding.btnTitle.visibility = View.VISIBLE
                addressList[position].titleaddress = true
                for (i in 0..addressList.size-1)
                {
                    if(i != position){
                        addressList[i].titleaddress = false
                    }
                }

                holder.binding.btnTitleAddress.setBackgroundResource(R.drawable.btn_round_gray)
                holder.binding.btnDelAddress.setBackgroundResource(R.drawable.btn_round_gray)
            }
            onItemClickListener.onItemClickTitle(addressList[position].addressdatas)
        }

        if (editBoolean) {
            holder.binding.btnDelAddress.visibility = View.VISIBLE
            holder.binding.btnTitleAddress.visibility = View.VISIBLE
        } else {
            holder.binding.btnDelAddress.visibility = View.INVISIBLE
            holder.binding.btnTitleAddress.visibility = View.INVISIBLE
        }
    }

    fun setItemEditable(editable: Boolean) {
        editBoolean = editable
    }
}
