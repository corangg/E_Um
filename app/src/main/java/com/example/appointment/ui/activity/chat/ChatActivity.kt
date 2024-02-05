package com.example.appointment.ui.activity.chat

import android.view.Menu
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.appointment.R
import com.example.appointment.ui.adapter.ChatAdapter
import com.example.appointment.viewmodel.MainViewmodel
import com.example.appointment.databinding.ActivityChatBinding
import com.example.appointment.model.ChatStartData
import com.example.appointment.ui.activity.BaseActivity

class ChatActivity : BaseActivity<ActivityChatBinding>() {
    val viewModel: MainViewmodel by viewModels()
    //val chatData = intent.getParcelableExtra<ChatStartData>("data")!!
    private lateinit var adapter: ChatAdapter

    override fun layoutResId(): Int {
        return R.layout.activity_chat
    }

    override fun initializeUI() {



        binding.viewmodel = viewModel
        setSupportActionBar(binding.toolbar)
        binding.toolbar.title = intent.getStringExtra("friendnickname")//chatData.friendNickName//
        supportActionBar?.setDisplayHomeAsUpEnabled(true)


        setChatRoomData()
    }

    override fun setObserve(){
        viewModel.chatData.observe(this, Observer {
            if(it!=null){
                binding.recycleMessage.layoutManager = LinearLayoutManager(this)
                adapter = ChatAdapter(it,viewModel.auth.currentUser?.email.toString(),/*chatData.friendProfileImg,chatData.friendNickName*/intent.getStringExtra("friendProfileURL"),intent.getStringExtra("friendnickname"))
                binding.recycleMessage.adapter=adapter
                binding.recycleMessage.scrollToPosition(it.size -1)
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

    private fun setChatRoomData(){
        val chatRoomName = intent.getStringExtra("chatRoomName")//chatData.chatRoomName//
        val chatCount = intent.getIntExtra("chatCount",0)//chatData.chatCount//

        viewModel.fnChatMessageSet(chatRoomName, chatCount)
        viewModel.chatRoomName.value = chatRoomName
    }


}