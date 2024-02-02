package com.example.appointment.ui.activity.profile

import android.app.Activity
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.databinding.DataBindingUtil
import com.example.appointment.R
import com.example.appointment.databinding.ActivityNickNameBinding
import com.example.appointment.ui.activity.BaseActivity
import com.example.appointment.viewmodel.MainViewmodel

class NickNameActivity : BaseActivity<ActivityNickNameBinding>() {
    val mainViewmodel: MainViewmodel by viewModels()

    override fun layoutResId(): Int {
        return R.layout.activity_nick_name
    }

    override fun initializeUI() {
        binding.viewmodle = mainViewmodel

        mainViewmodel.profileNickname.value = intent.getStringExtra("nickname")
        mainViewmodel.profileStatusMessage.value = intent.getStringExtra("statusmessage")
    }

    override fun setObserve(){
        mainViewmodel.nickNameEditSave.observe(this){
            if(it){
                intent.putExtra("nickname",mainViewmodel.profileNickname.value)
                intent.putExtra("statusmessage",mainViewmodel.profileStatusMessage.value)
                setResult(Activity.RESULT_OK,intent)
                finish()
            }
        }

    }
}