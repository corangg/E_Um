package com.example.appointment.ui.fragment

import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.appointment.ui.adapter.BaseAdapter


abstract class AdapterFragment<B : ViewDataBinding>: BaseFragment<B>() {
    fun setAdapter(recyclerView: RecyclerView, adapter: BaseAdapter<*, *>, list: MutableList<*>,line : Boolean) {
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.adapter = adapter
        @Suppress("UNCHECKED_CAST")
        (adapter as? BaseAdapter<Any, *>)?.setList(list as MutableList<Any>)
        if(line == true){
            recyclerView.addItemDecoration(
                DividerItemDecoration(requireContext(), LinearLayoutManager.VERTICAL)
            )
        }
    }
}