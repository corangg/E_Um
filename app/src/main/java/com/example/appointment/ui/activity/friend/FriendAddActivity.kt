package com.example.appointment.ui.activity.friend

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.databinding.DataBindingUtil
import com.bumptech.glide.Glide
import com.example.appointment.R
import com.example.appointment.databinding.ActivityFriendAddBinding
import com.example.appointment.ui.activity.BaseActivity
import com.example.appointment.viewmodel.MainViewmodel
import com.example.appointment.viewmodel.friend.FriendAddViewModel

class FriendAddActivity : BaseActivity<ActivityFriendAddBinding>() {

    val viewmodel: FriendAddViewModel by viewModels()

    override fun layoutResId(): Int {
        return R.layout.activity_friend_add
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

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_search_friend,menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onPrepareOptionsMenu(menu: Menu?): Boolean {
        return super.onPrepareOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean =when(item.itemId){
        R.id.menu_search_friend->{
            viewmodel.fnSearchFriend()
            true
        }
        R.id.menu_friend_request->{
            val nickName = intent.getStringExtra("nickname")
            viewmodel.fnSelectFriendRequestItem(nickName)
            true
        }else-> super.onOptionsItemSelected(item)
    }

    override fun setObserve() {
        viewmodel.searchFriend.observe(this){
            if(it){
                binding.layoutFriendCheck.visibility = View.VISIBLE
                Glide.with(this).load(viewmodel.searchFriendImgURL.value).into(binding.imgProfile)
            }else{
                Toast.makeText(this,"이메일을 확인해 주세요.",Toast.LENGTH_SHORT).show()
            }
        }

        viewmodel.friendRequestCheck.observe(this){
            when(it){
                1 -> {
                    finish()
                    Toast.makeText(this,"친구 신청 완료.",Toast.LENGTH_SHORT).show()
                }
                2 -> Toast.makeText(this,"본인 계정 입니다.",Toast.LENGTH_SHORT).show()
                3 -> Toast.makeText(this,"검색된 계정이 없습니다.",Toast.LENGTH_SHORT).show()
                4 -> Toast.makeText(this,"친구 신청 실패.",Toast.LENGTH_SHORT).show()
            }
        }
    }
}