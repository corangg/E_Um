package com.example.appointment.ui.activity

import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.appointment.ui.adapter.BaseAdapter

abstract class AdapterActivity<B : ViewDataBinding>:BaseActivity<B>() {
    fun setAdapter(recyclerView: RecyclerView, adapter: BaseAdapter<*, *>, list: MutableList<*>,line : Boolean) {
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter
        @Suppress("UNCHECKED_CAST")
        (adapter as? BaseAdapter<Any, *>)?.setList(list as MutableList<Any>)
        if(line == true){
            recyclerView.addItemDecoration(
                DividerItemDecoration(this, LinearLayoutManager.VERTICAL)
            )
        }
    }

}