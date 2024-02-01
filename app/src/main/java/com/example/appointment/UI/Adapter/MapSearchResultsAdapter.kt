package com.example.appointment.UI.Adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.appointment.databinding.ItemMapAddressBinding
import com.example.appointment.model.PlaceItem

class MapSearchResultsViewHolder(val binding: ItemMapAddressBinding): RecyclerView.ViewHolder(binding.root)

class MapSearchResultsAdapter(val mapinfoList:MutableList<PlaceItem>, val onItemClickListener: OnItemClickListener):
    RecyclerView.Adapter<RecyclerView.ViewHolder>(){
    interface OnItemClickListener {
        fun onItemClick(position: Int)
    }

    override fun getItemCount(): Int {
        return mapinfoList.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return MapSearchResultsViewHolder(ItemMapAddressBinding.inflate(LayoutInflater.from(parent.context),parent,false))
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val binding: ItemMapAddressBinding =(holder as MapSearchResultsViewHolder).binding

        val searchName : String = mapinfoList[position].title.replace("<b>","").replace("</b>","")
        binding.name.text = searchName
        binding.roadAddress.text = mapinfoList[position].roadAddress

        binding.itemSearch.setOnClickListener {
            onItemClickListener.onItemClick(position)
        }
    }
}