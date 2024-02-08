package com.example.appointment.ui.adapter

import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView

abstract class BaseViewHolder<B: ViewDataBinding>(val binding: B) :
    RecyclerView.ViewHolder(binding.root) {
}

abstract class BaseAdapter<D, VH : RecyclerView.ViewHolder>(
    protected var itemList: MutableList<D> = mutableListOf()
) : RecyclerView.Adapter<VH>() {

    fun setList(newList: MutableList<D>) {
        this.itemList = newList
        notifyDataSetChanged()
    }

    final override fun onBindViewHolder(holder: VH, position: Int) {
        onBindViewHolder(holder, itemList[position], position)
    }

    abstract fun onBindViewHolder(holder: VH, item: D, position:Int)

    final override fun getItemCount(): Int = itemList.size
}

interface OnItemClickListener {
    fun onItemClick(position: Int)
}
interface OnItemLongClickListener {
    fun onItemLongClick(position: Int)
}