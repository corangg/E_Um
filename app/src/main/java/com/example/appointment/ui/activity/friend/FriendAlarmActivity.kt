package com.example.appointment.ui.activity.friend

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.appointment.ui.adapter.FriendRequestAlarmAdapter
import com.example.appointment.R
import com.example.appointment.databinding.ActivityFriendAlarmBinding
import com.example.appointment.viewmodel.MainViewmodel

class FriendAlarmActivity : AppCompatActivity() , FriendRequestAlarmAdapter.OnItemClickListener{
    private lateinit var binding :ActivityFriendAlarmBinding
    private val mainViewmodel : MainViewmodel by viewModels()
    private lateinit var adapter: FriendRequestAlarmAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this,R.layout.activity_friend_alarm)
        binding.viewmodel= mainViewmodel
        binding.lifecycleOwner = this

        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        mainViewmodel.fnFriendAlarmReceive()

        mainViewmodel.friendRequestAlarmDataList.observe(this){
            binding.recycleAlarm.layoutManager = LinearLayoutManager(this)
            adapter = FriendRequestAlarmAdapter(it,this)
            binding.recycleAlarm.adapter=adapter
            adapter.setList(it)
            binding.recycleAlarm.addItemDecoration(
                DividerItemDecoration(this,LinearLayoutManager.VERTICAL)
            )
        }
    }

    override fun onItemClickAccept(friendEmail: String, friendNickName: String) {
        mainViewmodel.fnFriendRequestAccept(friendEmail, friendNickName, intent.getStringExtra("nickname")!!)
    }

    override fun onItemClickRefuse(email: String, nickName: String) {
        mainViewmodel.fnFriendRequestRefuse(nickName,email)
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return super.onSupportNavigateUp()
    }

}