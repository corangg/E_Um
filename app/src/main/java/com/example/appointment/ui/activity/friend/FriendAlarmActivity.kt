package com.example.appointment.ui.activity.friend

import android.app.Activity
import android.content.Intent
import androidx.activity.viewModels
import com.example.appointment.ui.adapter.FriendRequestAlarmAdapter
import com.example.appointment.R
import com.example.appointment.databinding.ActivityFriendAlarmBinding
import com.example.appointment.ui.activity.AdapterActivity
import com.example.appointment.viewmodel.friend.FriendAlarmViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FriendAlarmActivity : AdapterActivity<ActivityFriendAlarmBinding>() ,FriendRequestAlarmAdapter.OnItemClickListener{

    val viewmodel : FriendAlarmViewModel by viewModels()

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
        viewmodel.friendRequestAccept(
            email,
            nickName,
            intent.getStringExtra("nickname")!!
        )
        val intent = Intent()
        setResult(Activity.RESULT_OK, intent)
    }

    override fun onItemClickRefuse(email: String, nickName: String) {
        viewmodel.friendRequestRefuse(nickName)
        val intent = Intent()
        setResult(Activity.RESULT_OK, intent)
    }

    override fun setObserve() {
        viewmodel.friendRequestAlarmDataList.observe(this) {
            setAdapter(binding.recycleAlarm,FriendRequestAlarmAdapter(this),it,true)
        }
    }
}