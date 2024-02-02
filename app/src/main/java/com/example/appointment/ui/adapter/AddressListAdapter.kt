package com.example.appointment.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.appointment.R
import com.example.appointment.databinding.ItemAddressBinding
import com.example.appointment.model.AddressData
import com.google.firebase.firestore.model.mutation.ArrayTransformOperation

/*
class AddressListViewHolder(val binding: ItemAddressBinding): RecyclerView.ViewHolder(binding.root)

class AddressListAdapter(val addressTitledatas: MutableList<String>?, val addressdatas: MutableList<String>?, val mainaddress: MutableList<Boolean>?, val onItemClickListener: OnItemClickListener):
    RecyclerView.Adapter<RecyclerView.ViewHolder>(){
    private var editBoolean = false

    interface OnItemClickListener {
        fun onItemClickDelete(position: Int)
        fun onItemClickTitle(address : String)
    }

    override fun getItemCount(): Int {
        return addressTitledatas?.size ?:0
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder
            = AddressListViewHolder(ItemAddressBinding.inflate(LayoutInflater.from(parent.context),parent,false))

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val binding=(holder as AddressListViewHolder).binding

        binding.addressTitle.text=addressTitledatas!![position]
        binding.address.text=addressdatas!![position]
        mainaddress!![position]
        if(mainaddress!![position]){
            holder.binding.btnTitle.visibility = View.VISIBLE
            holder.binding.btnTitleAddress.setBackgroundResource(R.drawable.btn_round_gray)
            holder.binding.btnDelAddress.setBackgroundResource(R.drawable.btn_round_gray)
        }else{
            holder.binding.btnTitle.visibility = View.INVISIBLE
            holder.binding.btnTitleAddress.setBackgroundResource(R.drawable.btn_round)
            holder.binding.btnDelAddress.setBackgroundResource(R.drawable.btn_round)
        }

        binding.btnDelAddress.setOnClickListener {
            if(!mainaddress!![position]){
                addressTitledatas.removeAt(position)
                addressdatas.removeAt(position)
                notifyItemRemoved(position)
                onItemClickListener.onItemClickDelete(position)
            }
        }

        binding.btnTitleAddress.setOnClickListener {

            if(!mainaddress!![position]){
                holder.binding.btnTitle.visibility = View.VISIBLE
                mainaddress[position] = true
                for (i in 0..mainaddress.size-1)
                {
                    if(i != position){
                        mainaddress[i] = false
                    }
                }

                holder.binding.btnTitleAddress.setBackgroundResource(R.drawable.btn_round_gray)
                holder.binding.btnDelAddress.setBackgroundResource(R.drawable.btn_round_gray)
            }
            onItemClickListener.onItemClickTitle(addressdatas[position])
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
*//*

class AddressListViewHolder(binding: ItemAddressBinding): BaseViewHolder<ItemAddressBinding>(binding){
    fun bindAddressItem(
        addressData: AddressData,
        position: Int,
        editable: Boolean,
        onItemClickListener: AddressListAdapter.OnItemClickListener
    ){
        binding.addressdata = addressData

        if(addressData.mainaddress){
            binding.btnTitle.visibility = View.VISIBLE
            binding.btnTitleAddress.setBackgroundResource(R.drawable.btn_round_gray)
            binding.btnDelAddress.setBackgroundResource(R.drawable.btn_round_gray)
        }else{
            binding.btnTitle.visibility = View.INVISIBLE
            binding.btnTitleAddress.setBackgroundResource(R.drawable.btn_round)
            binding.btnDelAddress.setBackgroundResource(R.drawable.btn_round)
        }

        binding.btnDelAddress.setOnClickListener {
            if(!addressData.mainaddress){
                addressData.addressTitledatas
                addressData.addressdatas
                onItemClickListener.onItemClickDelete(position)
            }
        }

        binding.btnTitleAddress.setOnClickListener {
            if(!addressData.mainaddress){
                binding.btnTitle.visibility = View.VISIBLE
                addressData.mainaddress = true
                *//* for (i in 0..addressData.size-1)
                 {
                     if(i != position){
                         addressData.mainaddress = false
                     }
                 }
 *//*
                binding.btnTitleAddress.setBackgroundResource(R.drawable.btn_round_gray)
                binding.btnDelAddress.setBackgroundResource(R.drawable.btn_round_gray)
            }else{
                addressData.mainaddress = false
            }
            onItemClickListener.onItemClickTitle(addressData.addressdatas)
        }

        if (editable) {
            binding.btnDelAddress.visibility = View.VISIBLE
            binding.btnTitleAddress.visibility = View.VISIBLE
        } else {
            binding.btnDelAddress.visibility = View.INVISIBLE
            binding.btnTitleAddress.visibility = View.INVISIBLE
        }
    }

}


class AddressListAdapter(
    private val addressData : MutableList<AddressData>,
    private val onItemClickListener: OnItemClickListener)
    : BaseAdapter<AddressData,AddressListViewHolder>(){
    private var editBoolean = false

    interface OnItemClickListener {
        fun onItemClickDelete(position: Int)
        fun onItemClickTitle(address : String)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AddressListViewHolder{
        val binding = ItemAddressBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return  AddressListViewHolder(binding)
    }

    override fun onBindViewHolder(holder: AddressListViewHolder, item: AddressData, position: Int) {
        val addressData = itemList[position]


        holder.bindAddressItem(addressData,position,editBoolean,onItemClickListener)
    }

    fun setItemEditable(editable: Boolean) {
        editBoolean = editable
    }
}*/

class AddressListViewHolder(val binding: ItemAddressBinding): RecyclerView.ViewHolder(binding.root)

class AddressListAdapter(val addressTitledatas: MutableList<String>?, val addressdatas: MutableList<String>?, val mainaddress: MutableList<Boolean>?, val onItemClickListener: OnItemClickListener):
    RecyclerView.Adapter<RecyclerView.ViewHolder>(){
    private var editBoolean = false

    interface OnItemClickListener {
        fun onItemClickDelete(position: Int)
        fun onItemClickTitle(address : String)
    }

    override fun getItemCount(): Int {
        return addressTitledatas?.size ?:0
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder
            = AddressListViewHolder(ItemAddressBinding.inflate(LayoutInflater.from(parent.context),parent,false))

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val binding=(holder as AddressListViewHolder).binding

        binding.addressTitle.text=addressTitledatas!![position]
        binding.address.text=addressdatas!![position]
        mainaddress!![position]
        if(mainaddress!![position]){
            holder.binding.btnTitle.visibility = View.VISIBLE
            holder.binding.btnTitleAddress.setBackgroundResource(R.drawable.btn_round_gray)
            holder.binding.btnDelAddress.setBackgroundResource(R.drawable.btn_round_gray)
        }else{
            holder.binding.btnTitle.visibility = View.INVISIBLE
            holder.binding.btnTitleAddress.setBackgroundResource(R.drawable.btn_round)
            holder.binding.btnDelAddress.setBackgroundResource(R.drawable.btn_round)
        }

        binding.btnDelAddress.setOnClickListener {
            if(!mainaddress!![position]){
                addressTitledatas.removeAt(position)
                addressdatas.removeAt(position)
                notifyItemRemoved(position)
                onItemClickListener.onItemClickDelete(position)
            }
        }

        binding.btnTitleAddress.setOnClickListener {

            if(!mainaddress!![position]){
                holder.binding.btnTitle.visibility = View.VISIBLE
                mainaddress[position] = true
                for (i in 0..mainaddress.size-1)
                {
                    if(i != position){
                        mainaddress[i] = false
                    }
                }

                holder.binding.btnTitleAddress.setBackgroundResource(R.drawable.btn_round_gray)
                holder.binding.btnDelAddress.setBackgroundResource(R.drawable.btn_round_gray)
            }
            onItemClickListener.onItemClickTitle(addressdatas[position])
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