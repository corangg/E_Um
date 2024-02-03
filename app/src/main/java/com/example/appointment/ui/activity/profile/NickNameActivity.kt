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
import com.example.appointment.viewmodel.profile.NickNameEditViewModel

class NickNameActivity : BaseActivity<ActivityNickNameBinding>() {
    val nicknameViewmodel: NickNameEditViewModel by viewModels()

    override fun layoutResId(): Int {
        return R.layout.activity_nick_name
    }

    override fun initializeUI() {
        binding.viewmodle = nicknameViewmodel

        nicknameViewmodel.nickName.value = intent.getStringExtra("nickname")
        nicknameViewmodel.statusMessage.value = intent.getStringExtra("statusmessage")
    }

    override fun setObserve(){
        nicknameViewmodel.nickNameEditSave.observe(this){
            intent.putExtra("nickname",nicknameViewmodel.nickName.value)
            intent.putExtra("statusmessage",nicknameViewmodel.statusMessage.value)
            setResult(Activity.RESULT_OK,intent)
            finish()
        }

    }
}