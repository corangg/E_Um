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
import com.example.appointment.ui.activity.BaseActivity
import com.example.appointment.viewmodel.MainViewmodel

class FriendAlarmActivity : BaseActivity<ActivityFriendAlarmBinding>() ,FriendRequestAlarmAdapter.OnItemClickListener{

    val viewModel : MainViewmodel by viewModels()
    private lateinit var adapter: FriendRequestAlarmAdapter

    override fun initializeUI() {
        binding.viewmodel = viewModel
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        viewModel.fnFriendAlarmReceive()
    }

    override fun setObserve() {
        viewModel.friendRequestAlarmDataList.observe(this) {
            binding.recycleAlarm.layoutManager = LinearLayoutManager(this)
            adapter = FriendRequestAlarmAdapter(it, this)
            binding.recycleAlarm.adapter = adapter
            adapter.setList(it)
            binding.recycleAlarm.addItemDecoration(
                DividerItemDecoration(this, LinearLayoutManager.VERTICAL)
            )
        }
    }

    override fun onItemClickAccept(email: String, nickName: String) {
        viewModel.fnFriendRequestAccept(
            email,
            nickName,
            intent.getStringExtra("nickname")!!
        )
    }

    override fun onItemClickRefuse(email: String, nickName: String) {
        viewModel.fnFriendRequestRefuse(nickName, email)
    }
    override fun layoutResId(): Int {
        return R.layout.activity_friend_alarm
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return super.onSupportNavigateUp()
    }
}