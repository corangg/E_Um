package com.example.appointment.main.fragment.chat

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import androidx.activity.viewModels
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.appointment.ChatAdapter
import com.example.appointment.R
import com.example.appointment.databinding.ActivityChatBinding
import com.example.appointment.main.MainViewmodel

class ChatActivity : AppCompatActivity() {
    private lateinit var binding: ActivityChatBinding
    private val mainViewmodel:MainViewmodel by viewModels()
    private lateinit var adapter: ChatAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_chat)
        binding.viewmodel = mainViewmodel
        binding.lifecycleOwner = this

        setSupportActionBar(binding.toolbar)
        binding.toolbar.title = intent.getStringExtra("friendnickname")
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        setChatRoomData()
        setObserve()
    }

    override fun onPrepareOptionsMenu(menu: Menu?): Boolean {
        return super.onPrepareOptionsMenu(menu)
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return super.onSupportNavigateUp()
    }

    private fun setChatRoomData(){
        mainViewmodel.fnChatMessageSet(intent.getStringExtra("chatRoomName"), intent.getIntExtra("chatCount",0))
        mainViewmodel.chatRoomName.value = intent.getStringExtra("chatRoomName")//수정 필요
    }

    private fun setObserve(){
        mainViewmodel.chatData.observe(this, Observer {
            if(it!=null){
                binding.recycleMessage.layoutManager = LinearLayoutManager(this)
                adapter = ChatAdapter(it,mainViewmodel.auth.currentUser?.email.toString(),intent.getStringExtra("friendProfileURL"),intent.getStringExtra("friendnickname"))
                binding.recycleMessage.adapter=adapter
                binding.recycleMessage.scrollToPosition(it.size -1)
            }
        })
    }
}