package com.example.appointment.ui.activity.friend

import android.app.Activity
import android.content.Intent
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
import com.example.appointment.viewmodel.friend.FriendAlarmViewModel

class FriendAlarmActivity : BaseActivity<ActivityFriendAlarmBinding>() ,FriendRequestAlarmAdapter.OnItemClickListener{

    val viewmodel : FriendAlarmViewModel by viewModels()
    private lateinit var adapter: FriendRequestAlarmAdapter

    override fun layoutResId(): Int {
        return R.layout.activity_friend_alarm
    }

    override fun initializeUI() {
        binding.viewmodel = viewmodel
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return super.onSupportNavigateUp()
    }

    override fun onItemClickAccept(email: String, nickName: String) {
        viewmodel.fnFriendRequestAccept(
            email,
            nickName,
            intent.getStringExtra("nickname")!!
        )
        val intent = Intent()
        setResult(Activity.RESULT_OK, intent)
    }

    override fun onItemClickRefuse(email: String, nickName: String) {
        viewmodel.fnFriendRequestRefuse(nickName, email)
        val intent = Intent()
        setResult(Activity.RESULT_OK, intent)
    }



    override fun setObserve() {
        viewmodel.friendRequestAlarmDataList.observe(this) {
            binding.recycleAlarm.layoutManager = LinearLayoutManager(this)
            adapter = FriendRequestAlarmAdapter(it, this)
            binding.recycleAlarm.adapter = adapter
            adapter.setList(it)
            binding.recycleAlarm.addItemDecoration(
                DividerItemDecoration(this, LinearLayoutManager.VERTICAL)
            )
        }
    }
}