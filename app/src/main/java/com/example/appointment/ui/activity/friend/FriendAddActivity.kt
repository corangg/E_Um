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

class FriendAddActivity : BaseActivity<ActivityFriendAddBinding>() {

    val mainViewmodel: MainViewmodel by viewModels()

    override fun layoutResId(): Int {
        return R.layout.activity_friend_add
    }

    override fun initializeUI() {
        binding.viewmodel = mainViewmodel
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    override fun setObserve() {
        mainViewmodel.searchFriend.observe(this){
            if(it){
                binding.layoutFriendCheck.visibility = View.VISIBLE
                Glide.with(this).load(mainViewmodel.searchFriendImgURL.value).into(binding.imgProfile)
            }else{
                Toast.makeText(this,"이메일을 확인해 주세요.",Toast.LENGTH_SHORT).show()
            }
        }

        mainViewmodel.friendRequestCheck.observe(this){
            when(it){
                1 -> {
                    finish()
                    Toast.makeText(this,"친구 신청 완료.",Toast.LENGTH_SHORT).show()
                }
                2 -> Toast.makeText(this,"본인 계정 입니다.",Toast.LENGTH_SHORT).show()
                3 -> Toast.makeText(this,"검색된 계정이 없습니다.",Toast.LENGTH_SHORT).show()
            }
        }
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
            mainViewmodel.fnSearchFriend()//수정
            true
        }
        R.id.menu_friend_request->{
            val nickName = intent.getStringExtra("nickname")
            mainViewmodel.fnFriendRequest(nickName)//수정
            true
        }else-> super.onOptionsItemSelected(item)
    }
}