package com.example.appointment.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import com.example.appointment.databinding.ItemMapAddressBinding
import com.example.appointment.model.PlaceItem

class MapSearchResultsViewHolder(binding: ItemMapAddressBinding): BaseViewHolder<ItemMapAddressBinding>(binding){
    fun bindMapSearchResults(
        mapinfoList: PlaceItem,
        position: Int,
        onItemClickListener: OnItemClickListener
    ){
        binding.placeitem = mapinfoList

        binding.name.text = mapinfoList.title.replace("<b>","").replace("</b>","")

        binding.itemSearch.setOnClickListener {
            onItemClickListener.onItemClick(position)
        }
    }
}

class MapSearchResultsAdapter(
    private val mapinfoList:MutableList<PlaceItem>,
    private val onItemClickListener: OnItemClickListener)
    : BaseAdapter<PlaceItem,MapSearchResultsViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MapSearchResultsViewHolder {
        val binding = ItemMapAddressBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return MapSearchResultsViewHolder(binding)
    }

    override fun onBindViewHolder(
        holder: MapSearchResultsViewHolder,
        item: PlaceItem,
        position: Int
    ) {
        val mapInfoData = itemList[position]

        holder.bindMapSearchResults(mapInfoData,position,onItemClickListener)
    }
}