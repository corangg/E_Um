package com.example.appointment.ui.activity.chat

import android.view.Menu
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.appointment.R
import com.example.appointment.ui.adapter.ChatAdapter
import com.example.appointment.databinding.ActivityChatBinding
import com.example.appointment.model.ChatCreateData
import com.example.appointment.model.ChatDataModel
import com.example.appointment.ui.activity.AdapterActivity
import com.example.appointment.ui.activity.BaseActivity
import com.example.appointment.viewmodel.chat.ChatViewModel

class ChatActivity : AdapterActivity<ActivityChatBinding>() {
    val viewModel: ChatViewModel by viewModels()

    lateinit var chatCreateData : ChatCreateData

    override fun layoutResId(): Int {
        return R.layout.activity_chat
    }

    override fun initializeUI() {
        val chatRoomName = intent.getStringExtra("chatRoomName")
        val chatCount = intent.getIntExtra("chatCount",0)

        binding.viewmodel = viewModel
        setSupportActionBar(binding.toolbar)
        binding.toolbar.title = intent.getStringExtra("friendnickname")
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        chatCreateData = ChatCreateData(intent.getStringExtra("friendProfileURL")!!,intent.getStringExtra("friendnickname")!!)

        viewModel.fnChatMessageSet(chatRoomName, chatCount)
    }

    override fun setObserve(){
        viewModel.chatData.observe(this, Observer {
            if(it!=null){
                setAdapter(binding.recycleMessage,ChatAdapter(it, chatCreateData),it,false)
            }
        })
    }

    override fun onPrepareOptionsMenu(menu: Menu?): Boolean {
        return super.onPrepareOptionsMenu(menu)
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return super.onSupportNavigateUp()
    }
}